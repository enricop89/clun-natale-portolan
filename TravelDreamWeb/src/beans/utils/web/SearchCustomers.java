package beans.utils.web;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import beans.accountmanagement.UserDTO;

@ManagedBean(name="SearchCustomers")
@ViewScoped
public class SearchCustomers {
	@Inject
	private Data_Exchange data;
	public Data_Exchange getData(){
		return data;
	}
	public void setData(Data_Exchange data){
		this.data = data;
	}
	
	private List<UserDTO> usersList;
	
	@PostConstruct
	public void init(){
		usersList = data.getUsersList();
	}
	
	public List<UserDTO> getUsersList(){
		return usersList;
	}
	
	public String selectFromDialog(UserDTO user){
		// if the user is selecting is own name, then shows his personal page. This is indeed also to prevent from unexpected behavior (such paying his own components in the gift list)
		if(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser() == user.getEmail())
			return "/customer/personal_page.xhtml?faces-redirect=true";
			
		//else // open up customer info page
		return null;//TODO: Waiting for Customer Info Page
	}
	
}
