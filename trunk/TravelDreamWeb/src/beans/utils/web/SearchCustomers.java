package beans.utils.web;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import beans.accountmanagement.UserDTO;

@ManagedBean(name="SearchCustomers")
@RequestScoped
public class SearchCustomers {
	@ManagedProperty(value = "#{Data_Exchange}")
	private Data_Exchange data;
	
	private List<UserDTO> usersList;
	
	@PostConstruct
	public void init(){
		usersList = data.getUsersList();
	}
	
	public Data_Exchange getData(){
		return data;
	}
	public void setData(Data_Exchange data){
		this.data = data;
	}
	
	public List<UserDTO> getUsersList(){
		return usersList;
	}
	
	public void selectFromDialog(UserDTO user){
		// if the user is selecting is own name, then shows his personal page. This is indeed also to prevent from unexpected behavior (such paying his own components in the gift list)
		/*if(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser() == user.getEmail())
		
		TODO: Waiting for UserPage	
			
		else // open up customer info page
		*/	
	}
	
}
