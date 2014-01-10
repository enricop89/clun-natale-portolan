package beans.travelpackage;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import beans.travelcomponent.ComponentType;
import beans.travelcomponent.TravelComponentHandler;
import entities.*;

/**
 * Session Bean implementation class PersonalizedTravelPackageHandler
 */
@Stateless
@LocalBean
public class PersonalizedTravelPackageHandler {
	@PersistenceContext
    private EntityManager entityManager;
	
	public boolean addNewPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		boolean result = Controlconsistency(personalizedTravelPackage);
		if(result)
			entityManager.persist(personalizedTravelPackage);
		return result;
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean updatePersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		boolean result = false;
		for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
			if(personalizedTravelPackage.getTravelComponents().get(i).getTravelElement() != null)
				result = true;
		if(result){	// if it is a confirmed package it does not procede!	
			result = Controlconsistency(personalizedTravelPackage);
			if(result)
				entityManager.merge(personalizedTravelPackage);
		}
		return result;
		
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean deletePersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		boolean result = false;
		for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
			if(personalizedTravelPackage.getTravelComponents().get(i).getTravelElement() != null)
				result = true;
		if(result){ // if it is a confirmed package it does not procede!	
			for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
				entityManager.remove(personalizedTravelPackage.getTravelComponents().get(i));
			entityManager.remove(personalizedTravelPackage);
		}
		return result;
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean confirmPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		boolean result = false;
		for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
			if(personalizedTravelPackage.getTravelComponents().get(i).getTravelElement() == null){
				TravelComponentHandler handler = new TravelComponentHandler();
				personalizedTravelPackage.getTravelComponents().get(i).setTravelElement(handler.payTravelComponent(personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent(), personalizedTravelPackage.getOwner()));
				personalizedTravelPackage.getTravelComponents().get(i).setPersistence(personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent());
				result = true; //the package is not confirmed yet, at least one component is not payed
			}
		return result;
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean copyPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage, User owner){
		PersonalizedTravelPackage newPersonalizedTravelPackage = new PersonalizedTravelPackage();
		newPersonalizedTravelPackage.setName(personalizedTravelPackage.getName());
		newPersonalizedTravelPackage.setOwner(owner);
		List<Components_Helper> components = new ArrayList<Components_Helper>();
		for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
			if(personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent() != null)
			{
				Components_Helper component = new Components_Helper();
				component.setPersonalizedTravelPackage(newPersonalizedTravelPackage);
				component.setTravelElement(null);
				component.setTravelComponent( personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent());
				components.add(component);
			}
			else
				return false; //the travel component associated has been deleted by an employee, the user cannot copy the package!
		newPersonalizedTravelPackage.setTravelComponents(components);
		for(int i = 0; i < components.size(); i++)
			entityManager.persist(components.get(i));
		entityManager.persist(newPersonalizedTravelPackage);
		return true;	
	}
	
	public boolean Controlconsistency(PersonalizedTravelPackage persTP){
		List<Components_Helper> Arr_flight = new ArrayList<Components_Helper>();
		List<Components_Helper> Dep_flight = new ArrayList<Components_Helper>();
		List<Components_Helper> Hotel = new ArrayList<Components_Helper>();
		List<Components_Helper> Excursion = new ArrayList<Components_Helper>();
		for (int i=0; i < persTP.getTravelComponents().size();i++){
			if(persTP.getTravelComponents().get(i).getTravelElement() != null){
				if(persTP.getTravelComponents().get(i).getPersistence().getType() == ComponentType.ARRIVAL_FLIGHT)
					Arr_flight.add(persTP.getTravelComponents().get(i));
				if(persTP.getTravelComponents().get(i).getPersistence().getType() == ComponentType.DEPARTURE_FLIGHT)
					Dep_flight.add(persTP.getTravelComponents().get(i));
				if(persTP.getTravelComponents().get(i).getPersistence().getType() == ComponentType.HOTEL)
					Hotel.add(persTP.getTravelComponents().get(i));
				if(persTP.getTravelComponents().get(i).getPersistence().getType() == ComponentType.EXCURSION)
					Excursion.add(persTP.getTravelComponents().get(i));
			}
			else{
				if(persTP.getTravelComponents().get(i).getTravelComponent().getType() == ComponentType.ARRIVAL_FLIGHT)
					Arr_flight.add(persTP.getTravelComponents().get(i));
				if(persTP.getTravelComponents().get(i).getTravelComponent().getType() == ComponentType.DEPARTURE_FLIGHT)
					Dep_flight.add(persTP.getTravelComponents().get(i));
				if(persTP.getTravelComponents().get(i).getTravelComponent().getType() == ComponentType.HOTEL)
					Hotel.add(persTP.getTravelComponents().get(i));
				if(persTP.getTravelComponents().get(i).getTravelComponent().getType() == ComponentType.EXCURSION)
					Excursion.add(persTP.getTravelComponents().get(i));
			}
		}
		//more than a departure or arrival flyght
		if(Arr_flight.size()>1 || Dep_flight.size()>1) 
			return false;
		
		//departure flight after arrival flight
		if(Arr_flight.get(0).getTravelElement() != null && Dep_flight.get(0).getTravelElement() != null){
			if(Arr_flight.get(0).getPersistence().getFlightArrivalDateTime().compareTo(Dep_flight.get(0).getPersistence().getFlightDepartureDateTime())>0) //compareTo return 1 if data1>data2, 0 if equal, -1 if data1<data2
				return false;
			if(Dep_flight.get(0).getPersistence().getFlightArrivalCity()==Arr_flight.get(0).getPersistence().getFlightDepartureCity())//control if arrival_city is equal to departure_city
				return false;
		}
		else if(Arr_flight.get(0).getTravelElement() != null && Dep_flight.get(0).getTravelElement() == null){
			if(Arr_flight.get(0).getPersistence().getFlightArrivalDateTime().compareTo(Dep_flight.get(0).getTravelComponent().getFlightDepartureDateTime())>0)
				return false; 
			if(Dep_flight.get(0).getPersistence().getFlightArrivalCity()==Arr_flight.get(0).getTravelComponent().getFlightDepartureCity())//control if arrival_city is equal to departure_city
				return false;
		}
		else if(Arr_flight.get(0).getTravelElement() == null && Dep_flight.get(0).getTravelElement() != null){
			if(Arr_flight.get(0).getTravelComponent().getFlightArrivalDateTime().compareTo(Dep_flight.get(0).getPersistence().getFlightDepartureDateTime())>0)
				return false;
			if(Dep_flight.get(0).getTravelComponent().getFlightArrivalCity()==Arr_flight.get(0).getPersistence().getFlightDepartureCity())//control if arrival_city is equal to departure_city
				return false;
		}
		else if(Arr_flight.get(0).getTravelElement() == null && Dep_flight.get(0).getTravelElement() == null){
			if(Arr_flight.get(0).getTravelComponent().getFlightArrivalDateTime().compareTo(Dep_flight.get(0).getTravelComponent().getFlightDepartureDateTime())>0)
				return false;
			if(Dep_flight.get(0).getTravelComponent().getFlightArrivalCity()==Arr_flight.get(0).getTravelComponent().getFlightDepartureCity())//control if arrival_city is equal to departure_city
				return false;
		}
		
		for (int i=0;i<Hotel.size();i++){
			if(Hotel.get(i).getTravelElement() != null){
				if(Arr_flight.get(0).getTravelElement() != null){
					if(Hotel.get(i).getPersistence().getHotelDate().compareTo(Arr_flight.get(0).getPersistence().getFlightArrivalDateTime())<0)
						return false; 	//Data hotel before Data arrival_flight
				}
				else{
					if(Hotel.get(i).getPersistence().getHotelDate().compareTo(Arr_flight.get(0).getTravelComponent().getFlightArrivalDateTime())<0)
						return false; 	//Data hotel before Data arrival_flight
				}
				
				if(Dep_flight.get(0).getTravelElement() != null){
					if(Hotel.get(i).getPersistence().getHotelDate().compareTo(Dep_flight.get(0).getPersistence().getFlightDepartureDateTime())>0)
						return false; 	//Data hotel after Data departure_flight
					if(Hotel.get(i).getPersistence().getHotelCity()!=Dep_flight.get(0).getPersistence().getFlightArrivalCity())
						return false;
				}
				else{
					if(Hotel.get(i).getPersistence().getHotelDate().compareTo(Dep_flight.get(0).getTravelComponent().getFlightDepartureDateTime())>0)
						return false; 	//Data hotel after Data departure_flight
					if(Hotel.get(i).getPersistence().getHotelCity()!=Dep_flight.get(0).getTravelComponent().getFlightArrivalCity())
						return false;
				}	
			}
			else{
				if(Arr_flight.get(0).getTravelElement() != null){
					if(Hotel.get(i).getTravelComponent().getHotelDate().compareTo(Arr_flight.get(0).getPersistence().getFlightArrivalDateTime())<0)
						return false; 	//Data hotel before Data arrival_flight
				}
				else{
					if(Hotel.get(i).getTravelComponent().getHotelDate().compareTo(Arr_flight.get(0).getTravelComponent().getFlightArrivalDateTime())<0)
						return false; 	//Data hotel before Data arrival_flight
				}
				
				if(Dep_flight.get(0).getTravelElement() != null){
					if(Hotel.get(i).getTravelComponent().getHotelDate().compareTo(Dep_flight.get(0).getPersistence().getFlightDepartureDateTime())>0)
						return false; 	//Data hotel after Data departure_flight
					if(Hotel.get(i).getTravelComponent().getHotelCity()!=Dep_flight.get(0).getPersistence().getFlightArrivalCity())
						return false;
				}
				else{
					if(Hotel.get(i).getTravelComponent().getHotelDate().compareTo(Dep_flight.get(0).getTravelComponent().getFlightDepartureDateTime())>0)
						return false; 	//Data hotel after Data departure_flight
					if(Hotel.get(i).getTravelComponent().getHotelCity()!=Dep_flight.get(0).getTravelComponent().getFlightArrivalCity())
						return false;
				}				
			}
		}
		for (int i=0;i<Excursion.size();i++){
			if(Excursion.get(i).getTravelElement() != null){
				if(Arr_flight.get(0).getTravelElement() != null){
					if(Excursion.get(i).getPersistence().getExcursionDateTime().compareTo(Arr_flight.get(0).getPersistence().getFlightArrivalDateTime())<0)
						return false; 	//Data excursion before Data arrival_flight
				}
				else{
					if(Excursion.get(i).getPersistence().getExcursionDateTime().compareTo(Arr_flight.get(0).getTravelComponent().getFlightArrivalDateTime())<0)
						return false; 	//Data excursion before Data arrival_flight					
				}
				
				if(Dep_flight.get(0).getTravelElement() != null){
					if(Excursion.get(i).getPersistence().getExcursionDateTime().compareTo(Dep_flight.get(0).getPersistence().getFlightDepartureDateTime())>0)
						return false; 	//Data excursion after Data departure_flight
					if(Excursion.get(i).getPersistence().getExcursionCity()!=Dep_flight.get(0).getPersistence().getFlightArrivalCity())
						return false;
				}
				else{
					if(Excursion.get(i).getPersistence().getExcursionDateTime().compareTo(Dep_flight.get(0).getTravelComponent().getFlightDepartureDateTime())>0)
						return false; 	//Data excursion after Data departure_flight
					if(Excursion.get(i).getPersistence().getExcursionCity()!=Dep_flight.get(0).getTravelComponent().getFlightArrivalCity())
						return false;
				}
			}
			else{
				if(Arr_flight.get(0).getTravelElement() != null){
					if(Excursion.get(i).getTravelComponent().getExcursionDateTime().compareTo(Arr_flight.get(0).getPersistence().getFlightArrivalDateTime())<0)
						return false; 	//Data excursion before Data arrival_flight
				}
				else{
					if(Excursion.get(i).getTravelComponent().getExcursionDateTime().compareTo(Arr_flight.get(0).getTravelComponent().getFlightArrivalDateTime())<0)
						return false; 	//Data excursion before Data arrival_flight
				}
				
				if(Dep_flight.get(0).getTravelElement() != null){
					if(Excursion.get(i).getTravelComponent().getExcursionDateTime().compareTo(Dep_flight.get(0).getPersistence().getFlightDepartureDateTime())>0)
						return false; 	//Data excursion after Data departure_flight
					if(Excursion.get(i).getTravelComponent().getExcursionCity()!=Dep_flight.get(0).getPersistence().getFlightArrivalCity())
						return false;
				}
				else{
					if(Excursion.get(i).getTravelComponent().getExcursionDateTime().compareTo(Dep_flight.get(0).getTravelComponent().getFlightDepartureDateTime())>0)
						return false; 	//Data excursion after Data departure_flight
					if(Excursion.get(i).getTravelComponent().getExcursionCity()!=Dep_flight.get(0).getTravelComponent().getFlightArrivalCity())
						return false;
				}
			}
		}	
		
		return true; // all the controls are ok	
	}
}
