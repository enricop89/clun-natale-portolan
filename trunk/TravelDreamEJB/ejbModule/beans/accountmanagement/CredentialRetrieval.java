package beans.accountmanagement;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import beans.accountmanagement.UserDTO;
import beans.utils.*;

import org.apache.commons.lang3.RandomStringUtils;

import entities.User;

/**
 * Session Bean implementation class credentialRetrieval
 */
@Stateless
public class CredentialRetrieval implements CredentialRetrievalInterface{	
	@EJB
	private SearchDTOInterface searchDTO;
	@EJB
	private Search search;
	@EJB
	private ModifyInfoInterface modifyInfo;
	
	@Override
	public boolean retrieveCredentials(String email){
		UserDTO user = searchDTO.findUser(email);
		User realUser = search.findUser(email);
		if(user != null && realUser.getGroups().get(0).getGroupName().equals("CUSTOMER"))
		{
			// generate new random password
			String temp_password = RandomStringUtils.randomAlphanumeric(8);
			// update user informations with the new password (in order to allow login)
			user.setPassword(temp_password);
			modifyInfo.updateCustomer(user);
			return SendEmail.send(user.getEmail(), "Your TravelDream Credentials", 
						"Dear " + user.getFirstName() + " " + user.getLastName()
					+	",\nHere there are your credentials as requested.\n"
					+ 	"\nPassword: " + temp_password
					+	"\n\nNOTE: the password is a random temporary string.\nPlease update it as you login again.");
		}
		return false;				
	}		
}
