package beans.accountmanagement;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.codec.digest.DigestUtils;

import beans.utils.Search;
import entities.*;

/**
 * Session Bean implementation class ModifyInfo
 */
@Stateless
public class ModifyInfo implements ModifyInfoInterface{
	@PersistenceContext
    private EntityManager entityManager;
	
	@EJB
	private Search search;
	
	@Override
	public void updateCustomer(UserDTO user) {
		User modified = search.findUser(user.getEmail());
		modified.setFirstName(user.getFirstName());
		modified.setLastName(user.getLastName());
		if(!user.getPassword().isEmpty())
			modified.setPassword(DigestUtils.sha256Hex(user.getPassword()));
		
		entityManager.merge(modified);
	}
}
