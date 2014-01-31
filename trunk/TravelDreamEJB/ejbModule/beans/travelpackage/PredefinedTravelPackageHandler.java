package beans.travelpackage;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
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
		
		Calendar cal = Calendar.getInstance();
		
		cal.setTime(predefinedTravelPackage.getDepartureDate());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date packageDeparture = new Date(cal.getTimeInMillis());
		
		cal.setTime(predefinedTravelPackage.getReturnDate());
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		Date packageReturn = new Date(cal.getTimeInMillis());
		
		if (packageDeparture.after(packageReturn))
			return "The package departure date must be before the package return date.";
				
		for (int i=0; i < predefinedTravelPackage.getTravelComponents().size();i++){
			TravelComponent component = predefinedTravelPackage.getTravelComponents().get(i);
			
			switch(component.getType()){
			case FLIGHT:
				flights.add(component);
				if (component.getFlightDepartureDateTime().before(packageDeparture))
					return "There is a flight before the departure date of the package.";
				if (component.getFlightArrivalDateTime().after(packageReturn))
					return "There is a flight arriving after the return date of the package.";
				break;
			case HOTEL:
				hotels.add(component);
				if (component.getHotelDate().before(packageDeparture))
					return "There is an hotel which checkin date is before the departure date of the package.";
				cal.setTime(component.getHotelDate());
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				cal.add(Calendar.DATE, 1);
				Date hotelCheckout = new Date(cal.getTimeInMillis());
				if (hotelCheckout.after(packageReturn))
					return "There is an hotel which checkout date is after the return date of the package.";
				break;
			case EXCURSION:
				excursions.add(component);
				if (component.getExcursionDateTime().before(packageDeparture))
					return "There is a excursion before the departure date of the package.";
				if (component.getExcursionDateTime().after(packageReturn))
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
				return "flights dates are equal. it's not possible!"; 
			
			cal.setTime(departureFlight.getFlightDepartureDateTime());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date depFlightDepDate = new Date(cal.getTimeInMillis()); //departure DATE of the departure flight
			
			cal.setTime(returnFlight.getFlightArrivalDateTime());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date retFlightArrDate = new Date(cal.getTimeInMillis()); //arrival DATE of the return flight
			
			cal.setTime(departureFlight.getFlightArrivalDateTime());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date arrivalDate = new Date(cal.getTimeInMillis());
			
			cal.setTime(returnFlight.getFlightDepartureDateTime());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date departureDate = new Date(cal.getTimeInMillis());
			
			if (depFlightDepDate!=packageDeparture)
				return "The departure date of the package must be the departure date of the departure flight.";
				
			if (retFlightArrDate!=packageReturn)
				return "The return date of the package must be the arrival date of the return flight.";
			
			if(!departureFlight.getFlightArrivalCity().equalsIgnoreCase(returnFlight.getFlightDepartureCity()))
				return "flights cities mismatch"; // city mismatch, error!
					
			for (int i=0;i<hotels.size();i++){
				TravelComponent currentHotel = hotels.get(i);
				cal.setTime(currentHotel.getHotelDate());
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				Date hotelCheckin = new Date(cal.getTimeInMillis());
				cal.setTime(currentHotel.getHotelDate());
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				cal.add(Calendar.DATE, 1);
				Date hotelCheckout = new Date(cal.getTimeInMillis());
				
				if (hotelCheckin.before(arrivalDate))
					return "there is an hotel check-in before the first flight arrival";
				
				if (departureDate.before(hotelCheckout))
					return "the departure date of the return flight is before the checkout date of an hotel component";		
				
				if(!currentHotel.getHotelCity().equalsIgnoreCase(returnFlight.getFlightDepartureCity()))
					return "one hotel has an invalid city";  // city control	
			}
			
			for (int i=0;i<excursions.size();i++){
				if(departureFlight.getFlightArrivalDateTime().after(excursions.get(i).getExcursionDateTime()))
					return "one excursion is before the departure flight arrival"; 	// date excursion before date departureFlight
				
				if(returnFlight.getFlightDepartureDateTime().before(excursions.get(i).getExcursionDateTime()))
					return "one excursion is after the return flight departure"; 	// date excursion after date returnFlight
				
				if(!excursions.get(i).getExcursionCity().equalsIgnoreCase(returnFlight.getFlightDepartureCity()))
					return "one excursion has an invald city";  // city control
			}
		}
		else if(flights.size() == 1){
			flight = flights.get(0);
			cal.setTime(flight.getFlightDepartureDateTime());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date flightDepartureDate = new Date(cal.getTimeInMillis());
			cal.setTime(flight.getFlightArrivalDateTime());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date flightArrivalDate = new Date(cal.getTimeInMillis());
			
			for (int i=0;i<excursions.size();i++){
				
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
				TravelComponent currentHotel = hotels.get(i);
				cal.setTime(currentHotel.getHotelDate());
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				Date hotelCheckin = new Date(cal.getTimeInMillis());
				cal.setTime(currentHotel.getHotelDate());
				cal.set(Calendar.HOUR_OF_DAY, 0);
				cal.set(Calendar.MINUTE, 0);
				cal.set(Calendar.SECOND, 0);
				cal.set(Calendar.MILLISECOND, 0);
				cal.add(Calendar.DATE, 1);
				Date hotelCheckout = new Date(cal.getTimeInMillis());
				
				// the date of every hotel must be after the departure date of the package
				if (hotelCheckin.before(packageDeparture))
					return "an hotel date is before the departure date of the package";
				if (hotelCheckout.after(packageReturn))
					return "an hotel checkout date is after the return date of the package";
				
				if (hotelCheckin.before(flightDepartureDate))
				{
					//if the hotel is before the flight departure
					if (!currentHotel.getHotelCity().equalsIgnoreCase(flight.getFlightDepartureCity()))
					{
						//the city of an hotel before the flight is different from the departure city of the flight
						return "the city of an hotel before the flight is different from the departure city of the flight";
					}	
				}
				else if (hotelCheckin.after(flightArrivalDate))
				{
					//if the hotel is after the flight arrival
					if (!currentHotel.getHotelCity().equalsIgnoreCase(flight.getFlightArrivalCity()))
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
				if(!hotels.get(i).getHotelCity().equalsIgnoreCase(city)){
					city = hotels.get(i).getHotelCity();
					changes++;
				}
			}
			for (int i=0;i<excursions.size();i++){		
				if(!excursions.get(i).getExcursionCity().equalsIgnoreCase(city)){
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









