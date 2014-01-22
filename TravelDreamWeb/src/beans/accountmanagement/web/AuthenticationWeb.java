package beans.accountmanagement.web;

import java.io.IOException;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import beans.accountmanagement.AuthenticationInterface;

@ManagedBean(name="AuthenticationWeb")
@RequestScoped
public class AuthenticationWeb  {
	
	@EJB
	private AuthenticationInterface authentication;

	public String deauthenticate()
	{
		authentication.deauthenticate();
		return "/index.xhtml?faces-redirect=true";
	}
	
	public void checkRole() throws IOException{
		if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("CUSTOMER"))
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personal_page.xhtml");
		else if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("EMPLOYEE"))
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/employee/control_panel.xhtml");
	}
	
}
