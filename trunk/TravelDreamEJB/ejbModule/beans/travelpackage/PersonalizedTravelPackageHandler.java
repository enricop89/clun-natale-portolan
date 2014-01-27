package beans.travelpackage;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
	@EJB
	private TravelComponentHandler handler;
	
	public String addNewPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		if(personalizedTravelPackage.getTravelComponents().isEmpty())
			return "the package cannot be empty";
		else{
			String result = consistencyCheck(personalizedTravelPackage);
			if(result.isEmpty())
				entityManager.persist(personalizedTravelPackage);
			return result;	
		}
	}
	
	@RolesAllowed({"CUSTOMER"})
	public String updatePersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		if(personalizedTravelPackage.getTravelComponents().isEmpty())
			return "the package cannot be empty";
		
		else {
			String result = "the package is confirmed";
			for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
				if(personalizedTravelPackage.getTravelComponents().get(i).getTravelElement() == null)
					result = "";
			if(result.isEmpty()){	// if it is a confirmed package it does not procede!	
				result = consistencyCheck(personalizedTravelPackage);
				if(result.isEmpty())
					entityManager.merge(personalizedTravelPackage);
			}
			return result;
		}		
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean deletePersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		boolean result = false;
		for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
			if(personalizedTravelPackage.getTravelComponents().get(i).getTravelElement() == null)
				result = true;
		if(result == true){ // if it is a confirmed package it does not procede!	
			for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
				entityManager.remove(personalizedTravelPackage.getTravelComponents().get(i));
			entityManager.remove(personalizedTravelPackage);
		}
		return result;
	}
	
	@RolesAllowed({"CUSTOMER"})
	public String confirmPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		String result = "the package is already confirmed";
		for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
			if(personalizedTravelPackage.getTravelComponents().get(i).getTravelElement() == null){
				personalizedTravelPackage.getTravelComponents().get(i).setTravelElement(handler.payTravelComponent(personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent(), personalizedTravelPackage.getOwner()));
				personalizedTravelPackage.getTravelComponents().get(i).setPersistence(personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent());
				result = ""; //the package is not confirmed yet, at least one component was not yet payed
			}
		return result;
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean copyPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage, User owner){
		PersonalizedTravelPackage newPersonalizedTravelPackage = new PersonalizedTravelPackage();
		newPersonalizedTravelPackage.setName(personalizedTravelPackage.getName());
		newPersonalizedTravelPackage.setOwner(owner);
		List<Components_Helper> components = new ArrayList<Components_Helper>();
		for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++){
			if(personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent() != null)
			{
				Components_Helper component = new Components_Helper();
				component.setPersonalizedTravelPackage(newPersonalizedTravelPackage);
				component.setTravelElement(null);
				component.setTravelComponent(personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent());
				components.add(component);
			}
			else
				return false; //the travel component associated has been deleted by an employee, the user cannot copy the package!
		
		}
		newPersonalizedTravelPackage.setTravelComponents(components);
		for(int i = 0; i < components.size(); i++)
			entityManager.persist(components.get(i));
		entityManager.persist(newPersonalizedTravelPackage);
		return true;	
	}
	
	private String consistencyCheck(PersonalizedTravelPackage personalizedTravelPackage){
		List<TravelComponent> flights = new ArrayList<TravelComponent>();
		List<TravelComponent> hotels = new ArrayList<TravelComponent>();
		List<TravelComponent> excursions = new ArrayList<TravelComponent>();
		TravelComponent departureFlight = new TravelComponent();
		TravelComponent returnFlight = new TravelComponent();
		for (int i=0; i < personalizedTravelPackage.getTravelComponents().size();i++){
			TravelComponent component = personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent();
			if(personalizedTravelPackage.getTravelComponents().get(i).getTravelElement() != null) // this component is bought, need to use getPersistence()
				component = personalizedTravelPackage.getTravelComponents().get(i).getPersistence();
			switch(component.getType()){
			case FLIGHT:
				flights.add(component);
				break;
			case HOTEL:
				hotels.add(component);
				break;
			case EXCURSION:
				excursions.add(component);
				break;
			}
		}
		if(flights.size() > 2)
			return "more than a departure or return flight"; // more than a departure or return flight
		else if(flights.size() == 2){
			int date = flights.get(0).getFlightDepartureDateTime().compareTo(flights.get(1).getFlightDepartureDateTime());
			if(date > 0 ){
				departureFlight = flights.get(1);
				returnFlight = flights.get(0);
			}
			else if (date < 0){
				departureFlight = flights.get(0);
				returnFlight = flights.get(1);
			}
			else // equal, not possible!
				return "equal, not possible!"; 				
			if(departureFlight.getFlightDepartureDateTime().compareTo(personalizedTravelPackage.getDepartureDate()) != 0)
				return "flights dates mismatch"; // dates mismatch
			
			if(returnFlight.getFlightArrivalDateTime().compareTo(personalizedTravelPackage.getReturnDate()) != 0)
				return "flights dates mismatch"; // dates mismatch
			
			if(!departureFlight.getFlightArrivalCity().equals(returnFlight.getFlightDepartureCity()))
				return "flights cities mismatch"; // city mismatch, error!
			
			for (int i=0;i<hotels.size();i++){
				if(departureFlight.getFlightArrivalDateTime().compareTo(hotels.get(i).getHotelDate())>0)
					return "one hotel has its date before the date of the departure flight";  	// date hotel before date departureFlight
				
				if(returnFlight.getFlightDepartureDateTime().compareTo(hotels.get(i).getHotelDate())<0)
					return "one hotel has its date after the date of the return flight"; 	// date hotel after date returnFlight
				
				if(!hotels.get(i).getHotelCity().equals(returnFlight.getFlightDepartureCity()))
					return "one hotel has an invalid city";  // city control
				
			}
			for (int i=0;i<excursions.size();i++){
				if(departureFlight.getFlightArrivalDateTime().compareTo(excursions.get(i).getExcursionDateTime())>0)
					return "one excursion has its date before the date of the departure flight";  	// date excursion before date departureFlight
				
				if(returnFlight.getFlightDepartureDateTime().compareTo(excursions.get(i).getExcursionDateTime())<0)
					return "one excursion has its date after the date of the return flight";  	// date excursion after date returnFlight
				
				if(!excursions.get(i).getExcursionCity().equals(returnFlight.getFlightDepartureCity()))
					return "one hotel has an invalid city";  // city control
				
			}
		}
		else if(flights.size() == 1){
			if((departureFlight = flights.get(0)).getFlightDepartureDateTime().compareTo(personalizedTravelPackage.getDepartureDate()) == 0){
				for (int i=0;i<hotels.size();i++){
					if(departureFlight.getFlightArrivalDateTime().compareTo(hotels.get(i).getHotelDate())>0)
						return "one excursion has its date before the date of the departure flight";   	// date hotel before date departureFlight
					
					if(personalizedTravelPackage.getReturnDate().compareTo(hotels.get(i).getHotelDate())<0)
						return "one excursion has its date after the date of the return flight"; 	 // date hotel after date returnFlight
					
					if(!hotels.get(i).getHotelCity().equals(departureFlight.getFlightArrivalCity()))
						return "one excursion has an invald city"; // city control
					
				}
				for (int i=0;i<excursions.size();i++){
					if(departureFlight.getFlightArrivalDateTime().compareTo(excursions.get(i).getExcursionDateTime())>0)
						return "one excursion has its date before the date of the departure flight";	// date excursion before date departureFlight
					
					if(personalizedTravelPackage.getReturnDate().compareTo(excursions.get(i).getExcursionDateTime())<0)
						return "one excursion has its date after the date of the return flight";	// date excursion after date returnFlight
					
					if(!excursions.get(i).getExcursionCity().equals(departureFlight.getFlightArrivalCity()))
						return "one excursion has an invald city";  // city control
					
				}				
			}
			if((returnFlight = flights.get(0)).getFlightArrivalDateTime().compareTo(personalizedTravelPackage.getReturnDate()) == 0){
				for (int i=0;i<hotels.size();i++){
					if(personalizedTravelPackage.getDepartureDate().compareTo(hotels.get(i).getHotelDate())>0)
						return "one hotel has its date before the date of the departure flight";  	// date hotel before date departureFlight
					
					if(returnFlight.getFlightDepartureDateTime().compareTo(hotels.get(i).getHotelDate())<0)
						return "one hotel has its date after the date of the return flight"; 	// date hotel after date returnFlight
					
					if(!hotels.get(i).getHotelCity().equals(returnFlight.getFlightDepartureCity()))
						return "one excursion has an invald city"; // city control
					
				}
				for (int i=0;i<excursions.size();i++){
					if(personalizedTravelPackage.getDepartureDate().compareTo(excursions.get(i).getExcursionDateTime())>0)
						return "date excursion before the date of the departure flight";  	// date excursion before date departureFlight
					
					if(returnFlight.getFlightDepartureDateTime().compareTo(excursions.get(i).getExcursionDateTime())<0)
						return "date excursion after the date of the return flight"; 	// date excursion after date returnFlight
					
					if(!excursions.get(i).getExcursionCity().equals(returnFlight.getFlightDepartureCity()))
						return "one excursion has an invald city";  // city control
					
				}				
			}		
			else
				return "dates mismatch";// dates mismatch
		}
		else{ // no flights
			String city = null;
			int changes = 0;
			for (int i=0;i<hotels.size();i++){
				if(personalizedTravelPackage.getDepartureDate().compareTo(hotels.get(i).getHotelDate())>0)
					return "one hotel has its date before the date of the departure flight"; 	// date hotel before date departureFlight
				
				if(personalizedTravelPackage.getReturnDate().compareTo(hotels.get(i).getHotelDate())<0)
					return "one hotel has its date after the date of the return flight";	// date hotel after date returnFlight
				
				if(!hotels.get(i).getHotelCity().equals(city)){
					city = hotels.get(i).getHotelCity();
					changes++;
				}
			}
			for (int i=0;i<excursions.size();i++){
				if(personalizedTravelPackage.getDepartureDate().compareTo(excursions.get(i).getExcursionDateTime())>0)
					return "one excursion has its date before the date of the departure flight"; 	// date excursion before date departureFlight
				
				if(personalizedTravelPackage.getReturnDate().compareTo(excursions.get(i).getExcursionDateTime())<0)
					return "one excursion has its date after the date of the return flight"; 	// date excursion after date returnFlight; 	// date excursion after date returnFlight
				
				if(!excursions.get(i).getExcursionCity().equals(city)){
					city = excursions.get(i).getExcursionCity();
					changes++;
				}
			}	
			if(changes > 1)
				return "more than one city, city mismatch!"; // more than one city, city mismatch!
			
		}
		return ""; // all the controls are OK	
	}
}
