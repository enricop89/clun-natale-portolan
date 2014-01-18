package beans.accountmanagement.web;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import beans.accountmanagement.ModifyInfoInterface;
import beans.accountmanagement.UserDTO;
import beans.utils.SearchDTOInterface;

@ManagedBean(name="ModifyInfoWeb")
@RequestScoped
public class ModifyInfoWeb {
	@EJB
	private ModifyInfoInterface modifyInfo;
	@EJB
	private SearchDTOInterface search;
	
	private UserDTO user;
	
	public ModifyInfoWeb() {
		user = search.findUser(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
	}
	public UserDTO getUser() {
		return user;
	}
	
	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	public String save(){
		modifyInfo.updateCustomer(user);
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "Your info has been succesfully updated!")); 
		return "index?faces-redirect=true"; //just for now - see SDD flow
	}
}
