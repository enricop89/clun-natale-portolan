package beans.accountmanagement;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entities.*;
import beans.accountmanagement.UserDTO;
import beans.utils.SendEmail;

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
		SendEmail.send(user.getEmail(), "Welcome to TravelDream", 
				"The staff wants to welcome you on TravelDream!\n"
			+	"Please keep note of your credentials, you will use them to login on the website.\n"
			+ 	"\nPassword: " + user.getPassword()
			+	"\nEnjoy your TravelDream experience!");
	}
}
