package beans.accountmanagement.web;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;

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
	
}
