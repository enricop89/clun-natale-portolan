package beans.accountmanagement;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;

/**
 * Session Bean implementation class authentication
 */
@Stateless
@RolesAllowed({"EMPLOYEE","CUSTOMER"})
public class Authentication implements AuthenticationInterface{
	@Override
	public void deauthenticate(){
	    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}
}
