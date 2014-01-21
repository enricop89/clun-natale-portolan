package beans.utils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entities.*;
import beans.accountmanagement.UserDTO;
import beans.customerhandler.GiftElements_HelperDTO;
import beans.customerhandler.GiftListDTO;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.Components_HelperDTO;
import beans.travelpackage.PersonalizedTravelPackageDTO;
import beans.travelpackage.PredefinedTravelPackageDTO;

/**
 * Session Bean implementation class Search
 */
@Stateless
@LocalBean
public class Search {
	
	@PersistenceContext
    private EntityManager entityManager;
	
	public GiftList findGiftList(User owner){
		return entityManager.find(GiftList.class,owner);		
	}
	
	public PersonalizedTravelPackage findPersonalizedTravelPackage(long id){
		return entityManager.find(PersonalizedTravelPackage.class,id);
	}
	
	public List<PersonalizedTravelPackage> findAllPersonalizedTravelPackages(User owner){
		List<PersonalizedTravelPackage> list = findAllPersonalizedTravelPackages();
		List<Integer> toRemove = new ArrayList<Integer>();
		for(int i = 0; i < list.size(); i++)
			 if(list.get(i).getOwner()!=owner)
				 toRemove.add(i);
		
		//remove duplicates
		Set<Integer> noDup = new HashSet<Integer>(toRemove);
		toRemove.clear();
		toRemove.addAll(noDup);
		//actually removes elements
		for(int i = toRemove.size() - 1; i >= 0; i--)
			list.remove(toRemove.get(i).intValue());		
		
		return list;
	}
	
	public List<PersonalizedTravelPackage> findAllPersonalizedTravelPackages(){
		return entityManager.createNamedQuery(PersonalizedTravelPackage.FIND_ALL, PersonalizedTravelPackage.class).getResultList();		
	}
	
	public List<PredefinedTravelPackage> findPredefinedTravelPackage(String name, Date departureDate, Date returnDate){
		List<PredefinedTravelPackage> list = findAllPredefinedTravelPackages();	
		List<Integer> toRemove = new ArrayList<Integer>();
		if(name != null){
			name = name.toLowerCase();
			// name can also be only a part of the real package name
			if(name.length() < 3){
				if(departureDate == null && returnDate == null)
					return null;
				
			}
			else{
				 for(int i = 0; i < list.size(); i++)
					 if(!list.get(i).getName().toLowerCase().contains(name))
						 toRemove.add(i);
			}
		}
		if(departureDate != null){
			 for(int i = 0; i < list.size(); i++)
				 if(list.get(i).getDepartureDate().compareTo(departureDate) != 0)
					 toRemove.add(i);				
		}
		if(returnDate != null){
			 for(int i = 0; i < list.size(); i++)
				 if(list.get(i).getDepartureDate().compareTo(returnDate) != 0)
					 toRemove.add(i);	
		}
		if(name == null && departureDate == null && returnDate == null)
			return null;
		
		//remove duplicates
		Set<Integer> noDup = new HashSet<Integer>(toRemove);
		toRemove.clear();
		toRemove.addAll(noDup);
		//actually removes elements
		for(int i = toRemove.size() - 1; i >= 0; i--)
			list.remove(toRemove.get(i).intValue());	
		
		return list;	
	}
	
	public List<PredefinedTravelPackage> findAllPredefinedTravelPackages(){
		return entityManager.createNamedQuery(PredefinedTravelPackage.FIND_ALL, PredefinedTravelPackage.class).getResultList();
	}
	
	public List<TravelComponent> findTravelComponent(TravelComponent travelComponent){
	// a fictitious TravelComponent is used to specify the search criteria
	// the unused field must be set to empty, even though it will consider only the field associated
	// to the specific ComponentType
		String query= new String();
		switch(travelComponent.getType()){
		case FLIGHT:
			if(travelComponent.getFlightDepartureDateTime()!=null){
				query=query + "c.flightDepartureDateTime = " + travelComponent.getFlightDepartureDateTime() ;
			}
			if(travelComponent.getFlightDepartureDateTime()!=null){
				if(query.isEmpty())
					query=query + "c.flightArrivalDateTime = " + travelComponent.getFlightArrivalDateTime() ;
				else	query=query + " AND c.flightArrivalDateTime = " + travelComponent.getFlightArrivalDateTime() ;
			}
			if(travelComponent.getFlightDepartureDateTime()!=null){
				if(query.isEmpty())
					query=query + "c.flightDepartureCity = " + travelComponent.getFlightDepartureCity() ;
				else	query=query + " AND c.flightDepartureCity = " + travelComponent.getFlightDepartureCity() ;
			}
			if(travelComponent.getFlightDepartureDateTime()!=null){
				if(query.isEmpty())
					query=query + "c.flightArrivalCity = " + travelComponent.getFlightArrivalCity() ;
				else	query=query + " AND c.flightArrivalCity = " + travelComponent.getFlightArrivalCity() ;
			}
			if(travelComponent.getFlightDepartureDateTime()!=null){
				if(query.isEmpty())
					query=query + "c.flightCode = " + travelComponent.getFlightCode() ;
				else	query=query + "AND flightCode = :" + travelComponent.getFlightCode() ;
			}
			break;
		case HOTEL:
			if(travelComponent.getHotelCity()!=null)
				query= query + "c.hotelCity = " + travelComponent.getHotelCity() ;
			if(travelComponent.getHotelDate()!=null){
				if(query.isEmpty())
					query=query + "c.hotelDate = " + travelComponent.getHotelDate() ;
				else	query=query + " AND c.hotelDate = " + travelComponent.getHotelDate() ;
			}
			break;
		case EXCURSION:
			if(travelComponent.getExcursionDescription()!=null)
				query= query + "CONTAINS(c.excursionDescription," + travelComponent.getExcursionDescription() + ")" ;
			if(travelComponent.getExcursionDateTime()!=null){
				if(query.isEmpty())
					query=query + "c.excursionDateTime = " + travelComponent.getExcursionDateTime() ;
				else	query=query + " AND c.excursionDateTime = " + travelComponent.getExcursionDateTime() ;
			}
			if(travelComponent.getFlightDepartureDateTime()!=null){
				if(query.isEmpty())
					query=query + "c.excursionCity = " + travelComponent.getExcursionCity() ;
				else	query=query + " AND c.excursionCity = " + travelComponent.getExcursionCity() ;
			}
			break;
		}
		return entityManager.createQuery("SELECT c FROM TravelComponent c WHERE " + query, TravelComponent.class).getResultList();
	}
	
	public List<TravelComponent> findAllTravelComponents(){
		return entityManager.createNamedQuery(TravelComponent.FIND_ALL, TravelComponent.class).getResultList();
	}
	
	public User findUser(String email) {
    	return entityManager.find(User.class, email);
    } 
	public User findUser(User user) {
		return entityManager.find(User.class, user.getEmail());
	}
	public List<User> findUser(String firstName, String lastName){
		//it considers both the case where all the fields are filled or only one of them
		List<User> listUser = findAllUser();
		List<Integer> toRemove = new ArrayList<Integer>();
		
		//employees must not be returned in this search!
		for(int i = 0; i < listUser.size(); i++)
			if(listUser.get(i).getGroups().get(0).getGroupName().equals("EMPLOYEE"))
				toRemove.add(i);
				
		if(firstName == null){
			if(lastName == null)
				return null;
			else{ //only lastName
				lastName = lastName.toLowerCase();
				for(int i = 0; i < listUser.size(); i++)
					if(!listUser.get(i).getLastName().toLowerCase().equals(lastName))
						toRemove.add(i);
			}
		}
		else{
			if(lastName == null){ //only firstName
				firstName = firstName.toLowerCase();
				for(int i = 0; i < listUser.size(); i++)
					if(!listUser.get(i).getFirstName().toLowerCase().equals(firstName))
						toRemove.add(i);
			}
			else{ //both
				lastName = lastName.toLowerCase();
				firstName = firstName.toLowerCase();
				for(int i = 0; i < listUser.size(); i++)
					if(!listUser.get(i).getFirstName().toLowerCase().equals(firstName) && !listUser.get(i).getLastName().toLowerCase().equals(lastName))
						toRemove.add(i);
			}				
		}
			
		//remove duplicates
		Set<Integer> noDup = new HashSet<Integer>(toRemove);
		toRemove.clear();
		toRemove.addAll(noDup);
		//actually removes elements
		for(int i = toRemove.size() - 1; i >= 0; i--)
			listUser.remove(toRemove.get(i).intValue());
		
		return listUser;
	}
	public List<User> findAllUser(){
		return entityManager.createNamedQuery(User.FIND_ALL, User.class).getResultList();
	}
	
	//DTOs
	public GiftElements_Helper findGiftElements_Helper(GiftElements_HelperDTO giftElement){
		return entityManager.find(GiftElements_Helper.class, giftElement.getId());
	}
	public Components_Helper findComponents_Helper(Components_HelperDTO component){
		return entityManager.find(Components_Helper.class, component.getId());
	}
	public TravelComponent findTravelComponent(TravelComponentDTO travelComponent){
		return entityManager.find(TravelComponent.class, travelComponent.getId());
	}
	public PersonalizedTravelPackage findPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage){
		return entityManager.find(PersonalizedTravelPackage.class, personalizedTravelPackage.getId());
	}
	public PredefinedTravelPackage findPredefinedTravelPackage(PredefinedTravelPackageDTO predefinedTravelPackage){
		return entityManager.find(PredefinedTravelPackage.class, predefinedTravelPackage.getId());
	}
	public GiftList findGiftList(GiftListDTO giftList){
		return entityManager.find(GiftList.class, giftList.getOwner());
	}
	public User findUser(UserDTO user) {
		return entityManager.find(User.class, user.getEmail());
	}
}