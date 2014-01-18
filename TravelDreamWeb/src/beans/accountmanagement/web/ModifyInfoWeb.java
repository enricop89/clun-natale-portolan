package beans.accountmanagement.web;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

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
		
	// important directive since beans are not initialized until the constructor is called.
	// to use them, like the search in this function, is sufficient to use the @PostConstruct directive
	// and this function will be called automatically right after the constructor
	@PostConstruct
	public void init(){
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
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "Your info has been succesfully updated!")); 
		return "/index.xhtml?faces-redirect=true"; //just for now - see SDD flow
	}
}
