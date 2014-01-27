package beans.travelpackage;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceContext;
import javax.ejb.EJB;
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
	
	@EJB
	PersonalizedTravelPackageHandler handler;
	
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
			if(result.length()>0)
				entityManager.merge(predefinedTravelPackage);	
			return result;
		}
	}
	
	@RolesAllowed({"EMPLOYEE","CUSTOMER"})
	public void deletePredefinedTravelPackage (PredefinedTravelPackage prefedefinedTravelPackage){
		entityManager.remove(prefedefinedTravelPackage);	
	}
	
	public void copyPredefinedTravelPackage(PredefinedTravelPackage predefinedTravelPackage, User owner){		
		PersonalizedTravelPackage persTP = new PersonalizedTravelPackage();
		persTP.setName(predefinedTravelPackage.getName());
		persTP.setOwner(owner);
		persTP.setDepartureDate(predefinedTravelPackage.getDepartureDate());
		persTP.setReturnDate(predefinedTravelPackage.getReturnDate());
		List<Components_Helper> constructor = new ArrayList<Components_Helper>();
		for(int i=0;i<predefinedTravelPackage.getTravelComponents().size();i++){
			Components_Helper component = new Components_Helper();
			component.setTravelElement(null);
			component.setPersonalizedTravelPackage(persTP);   
			component.setTravelComponent(predefinedTravelPackage.getTravelComponents().get(i));
			constructor.add(component);	
			entityManager.persist(component);
		}
		persTP.setTravelComponents(constructor);
		handler.addNewPersonalizedTravelPackage(persTP);
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
			else // equal, not possible!
				return "equal, not possible!"; 			
			
			if(departureFlight.getFlightDepartureDateTime().compareTo(predefinedTravelPackage.getDepartureDate()) != 0)
				return "flights dates mismatch"; // dates mismatch
			
			if(returnFlight.getFlightArrivalDateTime().compareTo(predefinedTravelPackage.getReturnDate()) != 0)
				return "flights dates mismatch"; // dates mismatch	
			
			if(!departureFlight.getFlightArrivalCity().equals(returnFlight.getFlightDepartureCity()))
				return "flights cities mismatch"; // city mismatch, error!
					
			for (int i=0;i<hotels.size();i++){
				if(departureFlight.getFlightArrivalDateTime().compareTo(hotels.get(i).getHotelDate())>0)
					return "one hotel has its date before the date of the departure flight"; 	// date hotel before date departureFlight
							
				if(returnFlight.getFlightDepartureDateTime().compareTo(hotels.get(i).getHotelDate())<0)
					return "one hotel has its date after the date of the return flight"; 	// date hotel after date returnFlight
				
				if(!hotels.get(i).getHotelCity().equals(returnFlight.getFlightDepartureCity()))
					return "one hotel has an invalid city";  // city control
				
			}
			for (int i=0;i<excursions.size();i++){
				if(departureFlight.getFlightArrivalDateTime().compareTo(excursions.get(i).getExcursionDateTime())>0)
					return "one excursion has its date before the date of the departure flight"; 	// date excursion before date departureFlight
				
				if(returnFlight.getFlightDepartureDateTime().compareTo(excursions.get(i).getExcursionDateTime())<0)
					return "one excursion has its date after the date of the return flight"; 	// date excursion after date returnFlight
				
				if(!excursions.get(i).getExcursionCity().equals(returnFlight.getFlightDepartureCity()))
					return "one excursion has an invald city";  // city control
			}
		}
		else if(flights.size() == 1){
			if((departureFlight = flights.get(0)).getFlightDepartureDateTime().compareTo(predefinedTravelPackage.getDepartureDate()) == 0){
				for (int i=0;i<hotels.size();i++){
					if(departureFlight.getFlightArrivalDateTime().compareTo(hotels.get(i).getHotelDate())>0)
						return "one hotel has its date before the date of the departure flight"; 	// date hotel before date departureFlight
					
					if(predefinedTravelPackage.getReturnDate().compareTo(hotels.get(i).getHotelDate())<0)
						return "one hotel has its date after the return date"; 	// date hotel after date returnDate
					
					if(!hotels.get(i).getHotelCity().equals(departureFlight.getFlightArrivalCity()))
						return "city control";  // city control
				}
				for (int i=0;i<excursions.size();i++){
					if(departureFlight.getFlightArrivalDateTime().compareTo(excursions.get(i).getExcursionDateTime())>0)
						return "one excursion has its date before the date of the departure flight"; 	// date excursion before date departureFlight
					
					if(predefinedTravelPackage.getReturnDate().compareTo(excursions.get(i).getExcursionDateTime())<0)
						return "one excursion has its date after the return date"; 	// date excursion after date returnDate
					
					if(!excursions.get(i).getExcursionCity().equals(departureFlight.getFlightArrivalCity()))
						return "one excursion has an invald city";  // city control
				}				
			}
			if((returnFlight = flights.get(0)).getFlightArrivalDateTime().compareTo(predefinedTravelPackage.getReturnDate()) == 0){
				for (int i=0;i<hotels.size();i++){
					if(predefinedTravelPackage.getDepartureDate().compareTo(hotels.get(i).getHotelDate())>0)
						return "one hotel has its date before the date of the departure date"; 	// date hotel before date departureDate
					
					if(returnFlight.getFlightDepartureDateTime().compareTo(hotels.get(i).getHotelDate())<0)
						return "one hotel has its date after the date of the return flight"; 	// date hotel after date returnFlight
					
					if(!hotels.get(i).getHotelCity().equals(returnFlight.getFlightDepartureCity()))
						return "one excursion has an invald city";  // city control
					
				}
				for (int i=0;i<excursions.size();i++){
					if(predefinedTravelPackage.getDepartureDate().compareTo(excursions.get(i).getExcursionDateTime())>0)
						return "date excursion before the date of the departure date"; 	// date excursion before date departureDate
					
					if(returnFlight.getFlightDepartureDateTime().compareTo(excursions.get(i).getExcursionDateTime())<0)
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
				if(predefinedTravelPackage.getDepartureDate().compareTo(hotels.get(i).getHotelDate())>0)
					return "one hotel has its date before the departure date"; 	// date hotel before date departure
				
				if(predefinedTravelPackage.getReturnDate().compareTo(hotels.get(i).getHotelDate())<0)
					return "one hotel has its date after the return date";	// date hotel after date return
				
				if(!hotels.get(i).getHotelCity().equals(city)){
					city = hotels.get(i).getHotelCity();
					changes++;
				}
			}
			for (int i=0;i<excursions.size();i++){
				if(predefinedTravelPackage.getDepartureDate().compareTo(excursions.get(i).getExcursionDateTime())>0)
					return "one excursion has its date before the departure date"; 	// date excursion before date departure
				if(predefinedTravelPackage.getReturnDate().compareTo(excursions.get(i).getExcursionDateTime())<0)
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









