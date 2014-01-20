package beans.accountmanagement.web;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.application.FacesMessage;  
import javax.faces.context.FacesContext; 
import javax.faces.context.Flash;

import beans.accountmanagement.CredentialRetrievalInterface;

@ManagedBean(name="CredentialRetrievalWeb")
@RequestScoped
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
	
	public String send() {
		FacesMessage message;
		if(credentialRetrieval.retrieveCredentials(email))
			message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Done",  "An email has been sent to: " + email + "\nPlease check your inbox.");  	          
		else
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error",  "Invalid email.");  	          	
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		facesContext.addMessage(null, message);  
		return "/index.xhtml?faces-redirect=true";
	}
}