package beans.accountmanagement;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import entities.*;

/**
 * Session Bean implementation class ModifyInfo
 */
@Stateless
public class ModifyInfo implements ModifyInfoInterface{
	@PersistenceContext
    private EntityManager entityManager;
	
	@Override
	public void updateCustomer(UserDTO user) {
		entityManager.merge(new User(user));
	}
}
