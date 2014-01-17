package beans.accountmanagement;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import beans.accountmanagement.UserDTO;
import beans.accountmanagement.ModifyInfo;
import beans.utils.*;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Session Bean implementation class credentialRetrieval
 */
@Stateless
public class CredentialRetrieval implements CredentialRetrievalInterface{	
	@EJB
	private SearchDTO search;
	@EJB
	private ModifyInfo modifyInfo;
	
	@Override
	public boolean retrieveCredentials(String email){
		UserDTO user = search.findUser(email);
		if(user != null)
		{
			// generate new random password
			String temp_password = RandomStringUtils.randomAlphanumeric(8);
			// update user informations with the new password (in order to allow login)
			user.setPassword(temp_password);
			modifyInfo.updateCustomer(user);
			return SendEmail.send(user.getEmail(), "Your TravelDream Credentials", 
						"Here there are your credentials as requested.\n"
					+ 	"\nPassword: " + temp_password
					+	"\nNOTE: the password is a random temporary string.\nPlease update it as you login again.");
		}
		return false;				
	}		
}
