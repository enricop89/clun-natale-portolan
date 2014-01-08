package beans.accountmanagement;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.annotation.security.RolesAllowed;
import entities.*;

/**
 * Session Bean implementation class ModifyInfo
 */
@Stateless

public class ModifyInfo implements ModifyInfoInterface{
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	@RolesAllowed({"CUSTOMER"})
	public void updateCustomer(UserDTO user) {
		entityManager.merge(new User(user));
	}
}
