package beans.utils.web;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;

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
	
	public void selectFromDialog(UserDTO user){
		RequestContext.getCurrentInstance().closeDialog(user); 
	}
	
}
