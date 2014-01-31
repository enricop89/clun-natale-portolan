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
		TravelComponent flight = new TravelComponent();
		for (int i=0;i<predefinedTravelPackage.getTravelComponents().size();i++){ //collect components in the three Component_Type
			TravelComponent component = predefinedTravelPackage.getTravelComponents().get(i);
			switch(component.getType()){
			case FLIGHT:
				flights.add(component);
				if (component.getFlightDepartureDateTime().before(predefinedTravelPackage.getDepartureDate()))
					return "There is a flight before the departure date of the package.";
				if (component.getFlightArrivalDateTime().after(predefinedTravelPackage.getReturnDate()))
					return "There is a flight arriving after the return date of the package.";
				break;
			case HOTEL:
				hotels.add(component);
				if (component.getHotelDate().before(predefinedTravelPackage.getDepartureDate()))
					return "There is a hotel before the departure date of the package.";
				if (component.getHotelDate().after(predefinedTravelPackage.getReturnDate()))
					return "There is a hotel after the return date of the package.";
				break;
			case EXCURSION:
				excursions.add(component);
				if (component.getExcursionDateTime().before(predefinedTravelPackage.getDepartureDate()))
					return "There is a excursion before the departure date of the package.";
				if (component.getExcursionDateTime().after(predefinedTravelPackage.getReturnDate()))
					return "There is a excursion after the return date of the package.";
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
			int i = 0;		
			for (i=0;i<hotels.size();i++){
				if(hotels.get(i).getHotelDate().before(new Date(departureFlight.getFlightArrivalDateTime().getTime())))
					return "one hotel has its date before the date of the departure flight"; 	// date hotel before date departureFlight
							
				if(hotels.get(i).getHotelDate().after(new Date(returnFlight.getFlightDepartureDateTime().getTime())))
					return "one hotel has its date after the date of the return flight"; 	// date hotel after date returnFlight
				
				if(!hotels.get(i).getHotelCity().equals(returnFlight.getFlightDepartureCity()))
					return "one hotel has an invalid city";  // city control
				
			}
			
			for (i=0;i<excursions.size();i++){
				if(departureFlight.getFlightArrivalDateTime().after(excursions.get(i).getExcursionDateTime()))
					return "one excursion has its date before the date of the departure flight"; 	// date excursion before date departureFlight
				
				if(returnFlight.getFlightDepartureDateTime().before(excursions.get(i).getExcursionDateTime()))
					return "one excursion has its date after the date of the return flight"; 	// date excursion after date returnFlight
				
				if(!excursions.get(i).getExcursionCity().equals(returnFlight.getFlightDepartureCity()))
					return "one excursion has an invald city";  // city control
			}
		}
		else if(flights.size() == 1)
		{
			flight = flights.get(0);
			for (int i=0;i<excursions.size();i++){
				// the date of every excursion must be after the departure date of the package
				if (excursions.get(i).getExcursionDateTime().before(predefinedTravelPackage.getDepartureDate()))
					return "the date of an excursion is before the departure date of the package";
				if (excursions.get(i).getExcursionDateTime().after(predefinedTravelPackage.getReturnDate()))
					return "the date of an excursion is after the return date of the package";
				
				if (excursions.get(i).getExcursionDateTime().before(flight.getFlightDepartureDateTime()))
				{
					//if the excursion is before the flight departure
					if (!excursions.get(i).getExcursionCity().equalsIgnoreCase(flight.getFlightDepartureCity()))
					{
						//the city of an excursion before the flight is different from the departure city of the flight
						return "the city of an excursion before the flight is different from the departure city of the flight";
					}	
				}
				else if (excursions.get(i).getExcursionDateTime().after(flight.getFlightArrivalDateTime()))
				{
					//if the excursion is after the flight arrival
					if (!excursions.get(i).getExcursionCity().equalsIgnoreCase(flight.getFlightArrivalCity()))
					{
						//the city of an excursion after the flight is different from the arrival city of the flight
						return "the city of an excursion after the flight is different from the arrival city of the flight";
					}
				}
				else
				{
					return "there is an overlap between an excurion and the flight";
				}
			}
			
			for (int i=0;i<hotels.size();i++){
				// the date of every hotel must be after the departure date of the package
				if (hotels.get(i).getHotelDate().before(predefinedTravelPackage.getDepartureDate()))
					return "an hotel date is before the departure date of the package";
				if (hotels.get(i).getHotelDate().after(predefinedTravelPackage.getReturnDate()))
					return "an hotel date is after the return date of the package";
				
				if (hotels.get(i).getHotelDate().before(new Date(flight.getFlightDepartureDateTime().getTime() - 86400000)))
				{
					//if the hotel is before the flight departure
					if (!hotels.get(i).getHotelCity().equalsIgnoreCase(flight.getFlightDepartureCity()))
					{
						//the city of an hotel before the flight is different from the departure city of the flight
						return "the city of an hotel before the flight is different from the departure city of the flight";
					}	
				}
				else if (hotels.get(i).getHotelDate().after(new Date(flight.getFlightArrivalDateTime().getTime() + 86400000)))
				{
					//if the hotel is after the flight arrival
					if (!hotels.get(i).getHotelCity().equalsIgnoreCase(flight.getFlightArrivalCity()))
					{
						//the city of an hotel after the flight is different from the arrival city of the flight
						return "the city of an hotel after the flight is different from the arrival city of the flight";
					}
				}
				else
				{
					//no check
				}
			}		
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









