package beans.accountmanagement;

import javax.ejb.Local;

@Local
public interface CredentialRetrievalInterface {
	public boolean retrieveCredentials(String email);
}
