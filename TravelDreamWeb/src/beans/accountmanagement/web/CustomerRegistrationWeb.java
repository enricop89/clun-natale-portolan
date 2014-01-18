package beans.accountmanagement.web;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

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
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		if(customerRegistration.addNewCustomer(user) == true)
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "Registration succesful!\n Please check your inbox!")); 
		
		else
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Registration error, user with this email may already exist.\nPlease try again or use the credential retrieval if you forgot your password.")); 
		
		return "index?faces-redirect=true";
	}
}
