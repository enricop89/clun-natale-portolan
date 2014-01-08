package beans.accountmanagement;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.annotation.security.RolesAllowed;

import entities.User;
import beans.utils.*;

/**
 * Session Bean implementation class credentialRetrieval
 */
@Stateless
public class CredentialRetrieval implements CredentialRetrievalInterface{
	@PersistenceContext
    private EntityManager entityManager;
	
	private Search search;
	
	@RolesAllowed({"CUSTOMER"})
	@Override
	public boolean retrieveCredentials(String email){
		User user = search.findUserByEmail(email);
		if(user != null)
			try {
				return SendEmail.send(user.getEmail(), "Your TravelDream Credentials", 
							"Here there are your credentials as requested.\n"
						+ 	"Username: " + user.getUserName()
						+ 	"\nPassword: " + AESencrp.decrypt(user.getLoginfo() ));
			} catch (Exception e) {
				return false;
			}
		return false;				
	}		
}
