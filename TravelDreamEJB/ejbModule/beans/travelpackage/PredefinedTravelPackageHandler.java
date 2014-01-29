package beans.travelpackage;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceContext;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.annotation.security.RolesAllowed;

import entities.*;

import javax.persistence.EntityManager;

import beans.utils.Search;

/**
 * Session Bean implementation class PredefinedTravelPackageHandler
 */
@Stateless
@LocalBean
public class PredefinedTravelPackageHandler {
	@PersistenceContext
    private EntityManager entityManager;
	
	@EJB
	PersonalizedTravelPackageHandler handler;
	
	@EJB
	Search finder;
	
	@RolesAllowed({"EMPLOYEE"})
	public String addNewPredefinedTravelPackage(PredefinedTravelPackage predefinedTravelPackage){			
		if(predefinedTravelPackage.getTravelComponents().isEmpty())
			return "the package cannot be empty";
		
		else{
			String result = consistencyCheck(predefinedTravelPackage);
			if(result.isEmpty())
				entityManager.persist(predefinedTravelPackage);		
			return result;
		}
	}
	
	@RolesAllowed({"EMPLOYEE","CUSTOMER"})
	public String updatePredefinedTravelPackage(PredefinedTravelPackage predefinedTravelPackage){	
		if(predefinedTravelPackage.getTravelComponents().isEmpty())
			return "the package cannot be empty";
		
		else{
			String result = consistencyCheck(predefinedTravelPackage);
			if(result.isEmpty())
				entityManager.merge(predefinedTravelPackage);	
			return result;
		}
	}
	
	@RolesAllowed({"EMPLOYEE","CUSTOMER"})
	public void deletePredefinedTravelPackage (PredefinedTravelPackage prefedefinedTravelPackage){
		entityManager.remove(prefedefinedTravelPackage);	
	}
	
	public String copyPredefinedTravelPackage(PredefinedTravelPackage predefinedTravelPackage, User owner){		
		PersonalizedTravelPackage personalizedTravelPackage = new PersonalizedTravelPackage();
		personalizedTravelPackage.setName(predefinedTravelPackage.getName());
		personalizedTravelPackage.setOwner(owner);
		personalizedTravelPackage.setDepartureDate(predefinedTravelPackage.getDepartureDate());
		personalizedTravelPackage.setReturnDate(predefinedTravelPackage.getReturnDate());
		
		List<PersonalizedTravelPackage> personalizedOwned = finder.findAllPersonalizedTravelPackages(owner);
		for(int i = 0; i < personalizedOwned.size(); i++)
			if(personalizedOwned.get(i).getName().equals(predefinedTravelPackage.getName()))
				return "a personalized travel package with the same name already exists";
				
		List<Components_Helper> constructor = new ArrayList<Components_Helper>();
		for(int i=0;i<predefinedTravelPackage.getTravelComponents().size();i++){
			Components_Helper component = new Components_Helper();
			component.setTravelElement(null);
			component.setPersonalizedTravelPackage(personalizedTravelPackage);   
			component.setTravelComponent(predefinedTravelPackage.getTravelComponents().get(i));
			constructor.add(component);	
		}
		personalizedTravelPackage.setTravelComponents(constructor);
		return handler.addNewPersonalizedTravelPackage(personalizedTravelPackage);
	}

	private String consistencyCheck(PredefinedTravelPackage predefinedTravelPackage){
		List<TravelComponent> flights = new ArrayList<TravelComponent>();
		List<TravelComponent> hotels = new ArrayList<TravelComponent>();
		List<TravelComponent> excursions = new ArrayList<TravelComponent>();
		TravelComponent departureFlight = new TravelComponent();
		TravelComponent returnFlight = new TravelComponent();
		for (int i=0;i<predefinedTravelPackage.getTravelComponents().size();i++){ //collect components in the three Component_Type
			TravelComponent component = predefinedTravelPackage.getTravelComponents().get(i);
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
			
			if(predefinedTravelPackage.getDepartureDate().after(new Date(departureFlight.getFlightDepartureDateTime().getTime())))
				return "departure date after the date of the departure flight"; // dates mismatch
		
			if(predefinedTravelPackage.getReturnDate().before(new Date(returnFlight.getFlightArrivalDateTime().getTime())))
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
			if(predefinedTravelPackage.getDepartureDate().equals(new Date((departureFlight = flights.get(0)).getFlightDepartureDateTime().getTime()))){
				for (int i=0;i<hotels.size();i++){
					if(hotels.get(i).getHotelDate().after(new Date(departureFlight.getFlightArrivalDateTime().getTime())))
						return "one hotel has its date before the date of the departure flight"; 	// date hotel before date departureFlight
					
					if(predefinedTravelPackage.getReturnDate().before(hotels.get(i).getHotelDate()))
						return "one hotel has its date after the return date"; 	// date hotel after date returnDate
					
					if(!hotels.get(i).getHotelCity().equals(departureFlight.getFlightArrivalCity()))
						return "city control";  // city control
				}
				for (int i=0;i<excursions.size();i++){
					if(departureFlight.getFlightArrivalDateTime().after(excursions.get(i).getExcursionDateTime()))
						return "one excursion has its date before the date of the departure flight"; 	// date excursion before date departureFlight
					
					if(predefinedTravelPackage.getReturnDate().before(new Date(excursions.get(i).getExcursionDateTime().getTime())))
						return "one excursion has its date after the return date"; 	// date excursion after date returnDate
					
					if(!excursions.get(i).getExcursionCity().equals(departureFlight.getFlightArrivalCity()))
						return "one excursion has an invald city";  // city control
				}				
			}
			if(predefinedTravelPackage.getReturnDate().equals(new Date((returnFlight = flights.get(0)).getFlightArrivalDateTime().getTime()))){
				for (int i=0;i<hotels.size();i++){
					if(predefinedTravelPackage.getDepartureDate().before(hotels.get(i).getHotelDate()))
						return "one hotel has its date before the date of the departure date"; 	// date hotel before date departureDate
					
					if(hotels.get(i).getHotelDate().after(new Date(returnFlight.getFlightDepartureDateTime().getTime())))
						return "one hotel has its date after the date of the return flight"; 	// date hotel after date returnFlight
					
					if(!hotels.get(i).getHotelCity().equals(returnFlight.getFlightDepartureCity()))
						return "one excursion has an invald city";  // city control
					
				}
				for (int i=0;i<excursions.size();i++){
					if(predefinedTravelPackage.getDepartureDate().after(new Date(excursions.get(i).getExcursionDateTime().getTime())))
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
				if(predefinedTravelPackage.getDepartureDate().after(hotels.get(i).getHotelDate()))
					return "one hotel has its date before the departure date"; 	// date hotel before date departure
				
				if(predefinedTravelPackage.getReturnDate().before(hotels.get(i).getHotelDate()))
					return "one hotel has its date after the return date";	// date hotel after date return
				
				if(!hotels.get(i).getHotelCity().equals(city)){
					city = hotels.get(i).getHotelCity();
					changes++;
				}
			}
			for (int i=0;i<excursions.size();i++){
				if(predefinedTravelPackage.getDepartureDate().after(new Date(excursions.get(i).getExcursionDateTime().getTime())))
					return "one excursion has its date before the departure date"; 	// date excursion before date departure
				if(predefinedTravelPackage.getReturnDate().before(new Date(excursions.get(i).getExcursionDateTime().getTime())))
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









