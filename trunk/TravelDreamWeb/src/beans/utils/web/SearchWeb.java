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
		
	private boolean isCustomer; // otherwise is employee, cannot be another value since the folder is not accessible by unauthenticated users
	
	public SearchWeb(){
		if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("CUSTOMER"))
			isCustomer = true;
		else if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("EMPLOYEE"))
			isCustomer = false;
	}
	
	public boolean getIsCustomer(){
		return isCustomer;
	}
}
