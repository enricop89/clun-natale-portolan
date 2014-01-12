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
		
	}
	
	public PersonalizedTravelPackage findPersonalizedTravelPackage(long id){
		
	}
	
	public List<PersonalizedTravelPackage> findAllPersonalizedTravelPackages(User owner){
		
	}
	
	public List<PersonalizedTravelPackage> findAllPersonalizedTravelPackages(){
		
	}
	
	public List<PredefinedTravelPackage> findPredefinedTravelPackage(String name){
		// nota che name può anche essere solo PARTE del nome del package
		// IMPORTANTE: DISCUTERE ALTRI CRITERI DI RICERCA basati sui componenti dei travel package
	}
	
	public List<PredefinedTravelPackage> findAllPredefinedTravelPackages(){
		
	}
	
	public List<TravelComponent> findTravelComponent(TravelComponent searchCriteria){
	// a fictitious TravelComponent is used to specify the search criteria
	// the unused field must be set to null, even though it will consider only the field associated
	// to the specific ComponentType
		
		
		// nota che uno dei due campi può essere nullo
	}
	
	public List<TravelComponent> findAllTravelComponents(){
		
	}
	
	public User findUser(String email) {
    	return entityManager.find(User.class, email);
    } 
	public User findUser(User user) {
		return entityManager.find(User.class, user.getEmail());
	}
	public List<User> findUser(String firstName, String lastName){
		//considerare anche i casi in cui uno dei valori sia nullo
		// eg. solo nome o solo cognome
	}
	public List<User> findAllUser(){
		
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