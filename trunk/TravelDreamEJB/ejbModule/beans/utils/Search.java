package beans.utils;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entities.*;
import beans.accountmanagement.UserDTO;
/**
 * Session Bean implementation class Search
 */
@Stateless
@LocalBean
public class Search {
	@PersistenceContext
    private EntityManager entityManager;
	
	public UserDTO findUser(String email) {
    	return convertToDTO(entityManager.find(User.class, email));
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