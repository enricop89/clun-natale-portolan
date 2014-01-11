package beans.travelpackage;

import java.util.ArrayList;
import java.util.List;



import javax.persistence.PersistenceContext;

import beans.travelcomponent.ComponentType;
import beans.travelcomponent.FlightType;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.annotation.security.RolesAllowed;

import entities.*;

import javax.persistence.EntityManager;

/**
 * Session Bean implementation class PredefinedTravelPackageHandler
 */
@Stateless
@LocalBean
public class PredefinedTravelPackageHandler {
	@PersistenceContext
    private EntityManager entityManager;

	@RolesAllowed("EMPLOYEE")
	public boolean addNewPredefinedTravelPackage(PredefinedTravelPackage predefinedTravelPackage){			
		if(predefinedTravelPackage.getTravelComponents().isEmpty())
			return false;
		else{
			boolean result = consistencyCheck(predefinedTravelPackage);
			if(result)
				entityManager.persist(predefinedTravelPackage);		
			return result;
		}

	}
	
	@RolesAllowed("EMPLOYEE")
	public boolean updatePredefinedTravelPackage(PredefinedTravelPackage predefinedTravelPackage){	
		if(predefinedTravelPackage.getTravelComponents().isEmpty())
			return false;
		else{
			boolean result = consistencyCheck(predefinedTravelPackage);
			if(result)
				entityManager.merge(predefinedTravelPackage);	
			return result;
		}
	}
	
	@RolesAllowed("EMPLOYEE")
	public void deletePredefinedTravelPackage (PredefinedTravelPackage prefedefinedTravelPackage){
		entityManager.remove(prefedefinedTravelPackage);	
	}
	
	public void copyPredefinedTravelPackage(PredefinedTravelPackage predefinedTravelPackage, User owner){		
		PersonalizedTravelPackage persTP = new PersonalizedTravelPackage();
		persTP.setName(predefinedTravelPackage.getName());
		persTP.setOwner(owner);
		List<Components_Helper> constructor = new ArrayList<Components_Helper>();
		for(int i=0;i<predefinedTravelPackage.getTravelComponents().size();i++){
			Components_Helper component = new Components_Helper();
			component.setTravelElement(null);
			component.setPersonalizedTravelPackage(persTP);   
			component.setTravelComponent(predefinedTravelPackage.getTravelComponents().get(i));
			constructor.add(component);	
		}
		persTP.setTravelComponents(constructor);
		PersonalizedTravelPackageHandler handler = new PersonalizedTravelPackageHandler();
		handler.addNewPersonalizedTravelPackage(persTP);
	}

	private boolean consistencyCheck(PredefinedTravelPackage predefinedTravelPackage){
		List<TravelComponent> Arr_flight = new ArrayList<TravelComponent>();
		List<TravelComponent> Dep_flight = new ArrayList<TravelComponent>();
		List<TravelComponent> Hotel = new ArrayList<TravelComponent>();
		List<TravelComponent> Excursion = new ArrayList<TravelComponent>();
		for (int i=0;i<predefinedTravelPackage.getTravelComponents().size();i++){ //divide components in the four Component_type
			ComponentType aux=predefinedTravelPackage.getTravelComponents().get(i).getType();
			if(aux==ComponentType.FLIGHT){
				if(predefinedTravelPackage.getTravelComponents().get(i).getFlightType() == FlightType.ARRIVAL_FLIGHT)
					Arr_flight.add(predefinedTravelPackage.getTravelComponents().get(i));
				if(predefinedTravelPackage.getTravelComponents().get(i).getFlightType() == FlightType.DEPARTURE_FLIGHT)
					Dep_flight.add(predefinedTravelPackage.getTravelComponents().get(i));
			}
			if(aux==ComponentType.HOTEL)
				Hotel.add(predefinedTravelPackage.getTravelComponents().get(i));
			if(aux==ComponentType.EXCURSION)
				Excursion.add(predefinedTravelPackage.getTravelComponents().get(i));
		}
		
		if(Arr_flight.size()>1 || Dep_flight.size()>1) 
			return false;	// more than a departure or arrival flight
		
		if(Arr_flight.get(0).getFlightArrivalDateTime().compareTo(Dep_flight.get(0).getFlightDepartureDateTime())>0 ) //compareTo returns 1 if data1>data2, 0 if equal, -1 if data1<data2
			return false; 	// departure flight after arrival flight
		if(Arr_flight.get(0)!=null && Dep_flight.get(0)!=null)
			if(Dep_flight.get(0).getFlightArrivalCity()==Arr_flight.get(0).getFlightDepartureCity())//controls if arrival_city is equal to departure_city
				return false;
		
		for (int i=0;i<Hotel.size();i++){
			if(Hotel.get(i).getHotelDate().compareTo(Arr_flight.get(0).getFlightArrivalDateTime())<0)
				return false; 	// Data hotel before Data arrival_flight
			if(Hotel.get(i).getHotelDate().compareTo(Dep_flight.get(0).getFlightDepartureDateTime())>0)
				return false; 	// Data hotel after Data departure_flight
			if(Hotel.get(i).getHotelCity()!=Dep_flight.get(0).getFlightArrivalCity() )
				return false;
		}
		
		for (int i=0;i<Excursion.size();i++){
			if(Excursion.get(i).getExcursionDateTime().compareTo(Arr_flight.get(0).getFlightArrivalDateTime())<0)
				return false; 	// Data excursion before Data arrival_flight
			if(Excursion.get(i).getExcursionDateTime().compareTo(Dep_flight.get(0).getFlightDepartureDateTime())>0)
				return false; 	// Data excursion after Data departure_flight
			if(Excursion.get(i).getExcursionCity()!=Dep_flight.get(0).getFlightArrivalCity())
				return false;
		}
				
		return true; // all the controls are OK	
	}
	
}









