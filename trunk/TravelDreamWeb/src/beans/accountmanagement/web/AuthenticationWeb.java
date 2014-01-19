package beans.accountmanagement.web;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;

import beans.accountmanagement.AuthenticationInterface;

@ManagedBean(name="AuthenticationWeb")
public class AuthenticationWeb  {
	
	@EJB
	private AuthenticationInterface authentication;

	public String deauthenticate()
	{
		authentication.deauthenticate();
		return "index?faces-redirect=true";
	}
	
}
