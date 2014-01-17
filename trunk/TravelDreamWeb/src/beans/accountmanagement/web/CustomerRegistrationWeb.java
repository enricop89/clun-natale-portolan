package beans.accountmanagement.web;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import beans.accountmanagement.CustomerRegistrationInterface;
import beans.accountmanagement.UserDTO;

@ManagedBean(name="CustomerRegistrationWeb")
@RequestScoped
public class CustomerRegistrationWeb {
	@EJB
	private CustomerRegistrationInterface customerRegistration;
	
	private UserDTO user;
	
	public CustomerRegistrationWeb() {
		user = new UserDTO();
	}
	public UserDTO getUser() {
		return user;
	}
	
	public void setUser(UserDTO user) {
		this.user = user;
	}
	public String register() {
		customerRegistration.addNewCustomer(user);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Successful", "Registration succesful!")); 
		return "index?faces-redirect=true";
	}
}
