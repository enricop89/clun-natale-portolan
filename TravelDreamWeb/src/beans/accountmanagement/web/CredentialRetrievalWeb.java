package beans.accountmanagement.web;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.application.FacesMessage;  
import javax.faces.context.FacesContext; 
import javax.faces.event.ActionEvent;

import beans.accountmanagement.CredentialRetrievalInterface;

@ManagedBean(name="CredentialRetrievalWeb")
public class CredentialRetrievalWeb {
	@EJB
	private CredentialRetrievalInterface credentialRetrieval;

	private String email;
	
	public String getEmail(){
		return email;
	}
	
	public void setEmail(String email){
		this.email = email;
	}
	
	public void send(ActionEvent actionEvent) {
		FacesMessage message;
		if(credentialRetrieval.retrieveCredentials(email))
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Done",  "an email with the credential has been sent to " + email + ".\nPlease check your inbox.");  	          
		else
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  "Invalid email.");  	          	
		FacesContext.getCurrentInstance().addMessage(null, message);  
	}
}