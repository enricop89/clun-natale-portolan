package beans.accountmanagement;

import javax.ejb.Local;
import beans.accountmanagement.UserDTO;

@Local
public interface CustomerRegistrationInterface {
	public boolean addNewCustomer(UserDTO user);
}
