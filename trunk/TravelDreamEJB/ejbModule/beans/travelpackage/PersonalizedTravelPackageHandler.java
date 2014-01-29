package beans.travelpackage;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import beans.travelcomponent.TravelComponentHandler;
import beans.utils.Search;
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
	@EJB
	private Search finder;
	
	public String addNewPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		if(personalizedTravelPackage.getTravelComponents().isEmpty())
			return "the package cannot be empty";
		else{
			String result = consistencyCheck(personalizedTravelPackage);
			if(result.isEmpty()){
				for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
					entityManager.persist(personalizedTravelPackage.getTravelComponents().get(i));
				
				entityManager.persist(personalizedTravelPackage);
			}
			return result;	
		}
	}
	
	@RolesAllowed({"CUSTOMER","EMPLOYEE"})
	public String updatePersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		if(personalizedTravelPackage.getTravelComponents().isEmpty())
			return "the package cannot be empty";
		
		else {
			String result = "the package is already confirmed";
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
	
	@RolesAllowed({"CUSTOMER","EMPLOYEE"})
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
				TravelElement element = handler.payTravelComponent(personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent(), personalizedTravelPackage.getOwner());
				if(element == null)
					return personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent().getType() + " of the company " + personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent().getSupplyingCompany() + " has no available places";
				
				personalizedTravelPackage.getTravelComponents().get(i).setTravelElement(element);
				personalizedTravelPackage.getTravelComponents().get(i).setPersistence(personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent());
				result = ""; //the package is not confirmed yet, at least one component was not yet payed
			}
		
		return result;
	}
	
	@RolesAllowed({"CUSTOMER"})
	public PersonalizedTravelPackage copyPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage, User owner){
		PersonalizedTravelPackage newPersonalizedTravelPackage = new PersonalizedTravelPackage();
		newPersonalizedTravelPackage.setName(personalizedTravelPackage.getName());
		newPersonalizedTravelPackage.setOwner(owner);
		newPersonalizedTravelPackage.setDepartureDate(personalizedTravelPackage.getDepartureDate());
		newPersonalizedTravelPackage.setReturnDate(personalizedTravelPackage.getReturnDate());
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
				return null; //the travel component associated has been deleted by an employee, the user cannot copy the package!
		
		}
		newPersonalizedTravelPackage.setTravelComponents(components);
		
		List<PersonalizedTravelPackage> personalizedOwned = finder.findAllPersonalizedTravelPackages(owner);
		for(int i = 0; i < personalizedOwned.size(); i++)
			if(personalizedOwned.get(i).getName().equals(personalizedTravelPackage.getName()))
				return null;
		
		for(int i = 0; i < components.size(); i++)
			entityManager.persist(components.get(i));
		entityManager.persist(newPersonalizedTravelPackage);
		return newPersonalizedTravelPackage;	
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
		if(flights.size() > 2) // more than a departure or return flight
			return "more than a departure or return flight";
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
			else
				return "flights date equal, not possible!"; 				
			
			if(personalizedTravelPackage.getDepartureDate().after(new Date(departureFlight.getFlightDepartureDateTime().getTime())))
				return "departure date after the date of the departure flight"; // dates mismatch
		
			if(personalizedTravelPackage.getReturnDate().before(new Date(returnFlight.getFlightArrivalDateTime().getTime())))
				return "rerturn date before the date of the return fligh"; // dates mismatch
			
			if(!departureFlight.getFlightArrivalCity().equals(returnFlight.getFlightDepartureCity()))
				return "flights cities mismatch"; // city mismatch, error!
					
			for (int i=0;i<hotels.size();i++){
				if(hotels.get(i).getHotelDate().before(new Date(departureFlight.getFlightArrivalDateTime().getTime())))
					return "one hotel has its date before the date of the departure flight"; 	// date hotel before date departureFlight
							
				if(hotels.get(i).getHotelDate().after(new Date(returnFlight.getFlightDepartureDateTime().getTime())))
					return "one hotel has its date after the date of the return flight"; 	// date hotel after date returnFlight
				
				if(!hotels.get(i).getHotelCity().equals(returnFlight.getFlightDepartureCity()))
					return "one hotel has an invalid city";  // city control
				
			}
			for (int i=0;i<excursions.size();i++){
				if(departureFlight.getFlightArrivalDateTime().after(excursions.get(i).getExcursionDateTime()))
					return "one excursion has its date before the date of the departure flight"; 	// date excursion before date departureFlight
				
				if(returnFlight.getFlightDepartureDateTime().before(excursions.get(i).getExcursionDateTime()))
					return "one excursion has its date after the date of the return flight"; 	// date excursion after date returnFlight
				
				if(!excursions.get(i).getExcursionCity().equals(returnFlight.getFlightDepartureCity()))
					return "one excursion has an invald city";  // city control
			}
		}
		else if(flights.size() == 1){
			if(personalizedTravelPackage.getDepartureDate().equals(new Date((departureFlight = flights.get(0)).getFlightDepartureDateTime().getTime()))){
				for (int i=0;i<hotels.size();i++){
					if(departureFlight.getFlightArrivalDateTime().after(hotels.get(i).getHotelDate()))
						return "one hotel has its date before the date of the departure flight"; 	// date hotel before date departureFlight
					
					if(personalizedTravelPackage.getReturnDate().before(hotels.get(i).getHotelDate()))
						return "one hotel has its date after the return date"; 	// date hotel after date returnDate
					
					if(!hotels.get(i).getHotelCity().equals(departureFlight.getFlightArrivalCity()))
						return "city control";  // city control
				}
				for (int i=0;i<excursions.size();i++){
					if(departureFlight.getFlightArrivalDateTime().after(excursions.get(i).getExcursionDateTime()))
						return "one excursion has its date before the date of the departure flight"; 	// date excursion before date departureFlight
					
					if(personalizedTravelPackage.getReturnDate().before(new Date(excursions.get(i).getExcursionDateTime().getTime())))
						return "one excursion has its date after the return date"; 	// date excursion after date returnDate
					
					if(!excursions.get(i).getExcursionCity().equals(departureFlight.getFlightArrivalCity()))
						return "one excursion has an invald city";  // city control
				}				
			}
			if(personalizedTravelPackage.getReturnDate().equals(new Date((returnFlight = flights.get(0)).getFlightArrivalDateTime().getTime()))){
				for (int i=0;i<hotels.size();i++){
					if(personalizedTravelPackage.getDepartureDate().before(hotels.get(i).getHotelDate()))
						return "one hotel has its date before the date of the departure date"; 	// date hotel before date departureDate
					
					if(hotels.get(i).getHotelDate().after(new Date(returnFlight.getFlightDepartureDateTime().getTime())))
						return "one hotel has its date after the date of the return flight"; 	// date hotel after date returnFlight
					
					if(!hotels.get(i).getHotelCity().equals(returnFlight.getFlightDepartureCity()))
						return "one excursion has an invald city";  // city control
					
				}
				for (int i=0;i<excursions.size();i++){
					if(personalizedTravelPackage.getDepartureDate().after(new Date(excursions.get(i).getExcursionDateTime().getTime())))
						return "date excursion before the date of the departure date"; 	// date excursion before date departureDate
					
					if(returnFlight.getFlightDepartureDateTime().before(new Date(excursions.get(i).getExcursionDateTime().getTime())))
						return "date excursion after the date of the return flight"; 	// date excursion after date returnFlight
					
					if(!excursions.get(i).getExcursionCity().equals(returnFlight.getFlightDepartureCity()))
						return "one excursion has an invald city";  // city control
					
				}				
			}		
			else
				return "dates mismatch"; // dates mismatch
			
		}
		else{ // no flights
			String city = null;
			int changes = 0;
			for (int i=0;i<hotels.size();i++){
				if(personalizedTravelPackage.getDepartureDate().after(hotels.get(i).getHotelDate()))
					return "one hotel has its date before the departure date"; 	// date hotel before date departure
				
				if(personalizedTravelPackage.getReturnDate().before(hotels.get(i).getHotelDate()))
					return "one hotel has its date after the return date";	// date hotel after date return
				
				if(!hotels.get(i).getHotelCity().equals(city)){
					city = hotels.get(i).getHotelCity();
					changes++;
				}
			}
			for (int i=0;i<excursions.size();i++){
				if(personalizedTravelPackage.getDepartureDate().after(new Date(excursions.get(i).getExcursionDateTime().getTime())))
					return "one excursion has its date before the departure date"; 	// date excursion before date departure
				if(personalizedTravelPackage.getReturnDate().before(new Date(excursions.get(i).getExcursionDateTime().getTime())))
					return "one excursion has its date after the return date ";  // date excursion before date return
				
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
