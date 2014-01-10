package beans.utils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import entities.*;
import beans.accountmanagement.UserDTO;
import beans.travelcomponent.ComponentType;

/**
 * Session Bean implementation class Search
 */
@Stateless
@LocalBean
public class Search {
	@PersistenceContext
    private EntityManager entityManager;
	
	public GiftList findGiftList(UserDTO owner){
		
	}
	
	public PersonalizedTravelPackage findPersonalizedTravelPackage(long id){
		
	}
	
	public List<PersonalizedTravelPackage> findAllPersonalizedTravelPackages(UserDTO owner){
		
	}
	
	public List<PredefinedTravelPackage> findPredefinedTravelPackage(String name){
		// nota che name può anche essere solo PARTE del nome del package
	}
	
	public List<PredefinedTravelPackage> findAllPredefinedTravelPackages(){
		
	}
	
	public List<TravelComponent> findTravelComponent(ComponentType type, String supplyingCompany){
		// nota che uno dei due campi può essere nullo
	}
	
	public List<TravelComponent> findAllTravelComponents(){
		
	}
	
	public UserDTO findUser(String email) {
    	return convertToDTO(entityManager.find(User.class, email));
    } 
	public List<UserDTO> findUser(String firstName, String lastName){
		//considerare anche i casi in cui uno dei valori sia nullo
		// eg. solo nome o solo cognome
	}
	public List<UserDTO> findAllUser(){
		
	}
	// helper function
    private UserDTO convertToDTO(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setEmail(user.getEmail());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		return userDTO;
	}
}