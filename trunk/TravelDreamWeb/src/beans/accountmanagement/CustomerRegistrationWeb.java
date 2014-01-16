package beans.accountmanagement;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name="CustomerRegistrationWeb")
@RequestScoped
public class CustomerRegistrationWeb {
@EJB
private CustomerRegistrationInterface customerRegistration;
private UserDTO userDTO;

public CustomerRegistrationWeb() {
	userDTO = new UserDTO();
}
public UserDTO getUser() {
	return userDTO;
}

public void setUser(UserDTO userDTO) {
	this.userDTO = userDTO;
}
public String register() {
	customerRegistration.addNewCustomer(userDTO);
	return "index?faces-redirect=true";
}

}
