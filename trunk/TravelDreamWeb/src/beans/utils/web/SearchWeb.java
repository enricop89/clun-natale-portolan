package beans.utils.web;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import beans.utils.SearchDTOInterface;

@ManagedBean(name="SearchWeb")
@RequestScoped
public class SearchWeb {
	@EJB
	private SearchDTOInterface search;
	
	private String auth;
	
	public SearchWeb(){
		if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("CUSTOMER"))
			auth = "CUSTOMER";
		else if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("EMPLOYEE"))
			auth = "EMPLOYEE";
		else
			auth = null;
	}
	
	public String getAuth(){
		return auth;
	}
}
