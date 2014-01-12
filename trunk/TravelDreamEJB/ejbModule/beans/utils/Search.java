package beans.utils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import entities.*;
import beans.accountmanagement.UserDTO;
import beans.customerhandler.GiftElements_HelperDTO;
import beans.customerhandler.GiftListDTO;
import beans.travelcomponent.ComponentType;
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
	for(int i = 0; i < list.size(); i++)
		 if(list.get(i).getOwner()!=owner)
			 list.remove(i);
	 return list;
	}
	
	public List<PersonalizedTravelPackage> findAllPersonalizedTravelPackages(){
		return entityManager.createNamedQuery(PersonalizedTravelPackage.FIND_ALL, PersonalizedTravelPackage.class).getResultList();
		
	}
	
	public List<PredefinedTravelPackage> findPredefinedTravelPackage(String name){
		// name can also be only a part of the real package name
		if(name.length() < 3)
			return null;
		 List<PredefinedTravelPackage> list = findAllPredefinedTravelPackages();
		 for(int i = 0; i < list.size(); i++)
			 if(!list.get(i).getName().contains(name))
				 list.remove(i);
		 return list;
		
	}
	
	public List<PredefinedTravelPackage> findAllPredefinedTravelPackages(){
		return entityManager.createNamedQuery(PredefinedTravelPackage.FIND_ALL, PredefinedTravelPackage.class).getResultList();
	}
	
	public List<TravelComponent> findTravelComponent(TravelComponent searchCriteria){
	// a fictitious TravelComponent is used to specify the search criteria
	// the unused field must be set to null, even though it will consider only the field associated
	// to the specific ComponentType
		
		
		// nota che uno dei due campi può essere nullo
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
		if(firstName == ""){
			if(lastName == "")
				return null;
			else{ //only lastName
				for(int i = 0; i < listUser.size(); i++)
					if(listUser.get(i).getLastName() != lastName)
						listUser.remove(i);
			}
		}
		else{
			if(lastName == ""){ //only firstName
				for(int i = 0; i < listUser.size(); i++)
					if(listUser.get(i).getFirstName() != firstName)
						listUser.remove(i);
			}
			else{ //both
				for(int i = 0; i < listUser.size(); i++)
					if(listUser.get(i).getFirstName() != firstName && listUser.get(i).getLastName() != lastName)
						listUser.remove(i);
			}				
		}
		return listUser;
	}
	public List<User> findAllUser(){
		return entityManager.createNamedQuery(User.FIND_ALL, User.class).getResultList();
	}
	
	//DTOs
	public GiftElements_Helper findGiftElements_Helper(GiftElements_HelperDTO giftElement){
		
	}
	public Components_Helper findComponents_Helper(Components_HelperDTO component){
		
	}
	public TravelComponent findTravelComponent(TravelComponentDTO travelComponent){
		
	}
	public PersonalizedTravelPackage findPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage){
		
	}
	public PredefinedTravelPackage findPredefinedTravelPackage(PredefinedTravelPackageDTO predefinedTravelPackage){
		
	}
	public GiftList findGiftList(GiftListDTO giftList){
		
	}
	public User findUser(UserDTO user) {
		
	}
}