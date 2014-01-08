package beans.accountmanagement;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.annotation.security.RolesAllowed;
import entities.*;

/**
 * Session Bean implementation class ModifyInfo
 */
@Stateless
@LocalBean
public class ModifyInfo {
	@PersistenceContext
    private EntityManager entityManager;
	
	@RolesAllowed({"CUSTOMER"})
	public void updateCustomer(UserDTO user) {
		entityManager.merge(new User(user));
	}
}
