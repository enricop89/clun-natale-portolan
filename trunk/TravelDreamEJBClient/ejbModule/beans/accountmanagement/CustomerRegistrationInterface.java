package beans.accountmanagement;

import javax.ejb.Local;
import beans.accountmanagement.UserDTO;

@Local
public interface CustomerRegistrationInterface {
	public void addNewCustomer(UserDTO user);
}
