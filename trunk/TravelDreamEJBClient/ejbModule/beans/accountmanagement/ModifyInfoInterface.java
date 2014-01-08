package beans.accountmanagement;

import javax.ejb.Local;
import beans.accountmanagement.UserDTO;

@Local
public interface ModifyInfoInterface {
	public void updateCustomer(UserDTO user);
}
