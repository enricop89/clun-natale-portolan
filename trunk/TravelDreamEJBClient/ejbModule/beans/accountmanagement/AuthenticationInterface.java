package beans.accountmanagement;

import javax.ejb.Local;

@Local
public interface AuthenticationInterface {
	public void deauthenticate();
}
