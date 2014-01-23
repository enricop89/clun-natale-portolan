package beans.utils.web;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Named;

import org.primefaces.context.RequestContext;

import beans.accountmanagement.UserDTO;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.PersonalizedTravelPackageDTO;
import beans.travelpackage.PredefinedTravelPackageDTO;

@ManagedBean(name="Data_Exchange")
@SessionScoped @Named
public class Data_Exchange implements java.io.Serializable{	
	private static final long serialVersionUID = 1L;
	private List<PersonalizedTravelPackageDTO> personalizedTravelPackagesList;
	private List<PredefinedTravelPackageDTO> predefinedTravelPackagesList;
	private List<UserDTO> usersList;
	private List<TravelComponentDTO> travelComponentsList;
	private RequestContext instance;
	
	public Data_Exchange(){
		personalizedTravelPackagesList = new ArrayList<PersonalizedTravelPackageDTO>();
		predefinedTravelPackagesList = new ArrayList<PredefinedTravelPackageDTO>();
		usersList = new ArrayList<UserDTO>();
		travelComponentsList = new ArrayList<TravelComponentDTO>();
		instance = null;
	}
	
	public List<PersonalizedTravelPackageDTO> getPersonalizedTravelPackagesList() {
		List<PersonalizedTravelPackageDTO> result = new ArrayList<PersonalizedTravelPackageDTO>(personalizedTravelPackagesList);
		personalizedTravelPackagesList.clear();
		return result;
	}
	public void setPersonalizedTravelPackagesList(
			List<PersonalizedTravelPackageDTO> personalizedTravelPackagesList) {
		this.personalizedTravelPackagesList = personalizedTravelPackagesList;
	}
	public List<PredefinedTravelPackageDTO> getPredefinedTravelPackagesList() {
		List<PredefinedTravelPackageDTO> result = new ArrayList<PredefinedTravelPackageDTO>(predefinedTravelPackagesList);
		predefinedTravelPackagesList.clear();
		return result;
	}
	public void setPredefinedTravelPackagesList(
			List<PredefinedTravelPackageDTO> predefinedTravelPackagesList) {
		this.predefinedTravelPackagesList = predefinedTravelPackagesList;
	}
	public List<UserDTO> getUsersList() {
		List<UserDTO> result = new ArrayList<UserDTO>(usersList);
		usersList.clear();
		return result;
	}
	public void setUsersList(List<UserDTO> usersList) {
		this.usersList = usersList;
	}
	public List<TravelComponentDTO> getTravelComponentsList() {
		List<TravelComponentDTO> result = new ArrayList<TravelComponentDTO>(travelComponentsList);
		travelComponentsList.clear();
		return result;
	}
	public void setTravelComponentsList(List<TravelComponentDTO> travelComponentsList) {
		this.travelComponentsList = travelComponentsList;
	}

	public RequestContext getInstance() {
		return instance;
	}

	public void setInstance(RequestContext instance) {
		this.instance = instance;
	}
}
