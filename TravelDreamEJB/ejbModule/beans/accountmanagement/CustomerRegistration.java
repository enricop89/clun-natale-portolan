package beans.accountmanagement;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entities.*;
import beans.accountmanagement.UserDTO;
import java.util.ArrayList;
import java.util.List;

/**
 * Session Bean implementation class CustomerRegistration
 */
@Stateless
public class CustomerRegistration implements CustomerRegistrationInterface{
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public void addNewCustomer(UserDTO user){
		User newUser = new User(user);
		List<Group> groups = new ArrayList<Group>();
		groups.add(new Group("CUSTOMER"));
		newUser.setGroups(groups);
		entityManager.persist(newUser);
	}
}
