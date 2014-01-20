package beans.utils.web;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.bean.ManagedBean;

import beans.accountmanagement.UserDTO;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.PersonalizedTravelPackageDTO;
import beans.travelpackage.PredefinedTravelPackageDTO;

@ManagedBean(name="Data_Exchange")
@SessionScoped
public class Data_Exchange {
	private List<PersonalizedTravelPackageDTO> personalizedTravelPackagesList;
	private List<PredefinedTravelPackageDTO> predefinedTravelPackagesList;
	private List<UserDTO> usersList;
	private List<TravelComponentDTO> travelComponentsList;
	
	public Data_Exchange(){
		personalizedTravelPackagesList = new ArrayList<PersonalizedTravelPackageDTO>();
		predefinedTravelPackagesList = new ArrayList<PredefinedTravelPackageDTO>();
		usersList = new ArrayList<UserDTO>();
		travelComponentsList = new ArrayList<TravelComponentDTO>();
	}
	
	
	public List<PersonalizedTravelPackageDTO> getPersonalizedTravelPackagesList() {
		return personalizedTravelPackagesList;
	}
	public void setPersonalizedTravelPackagesList(
			List<PersonalizedTravelPackageDTO> personalizedTravelPackagesList) {
		this.personalizedTravelPackagesList = personalizedTravelPackagesList;
	}
	public List<PredefinedTravelPackageDTO> getPredefinedTravelPackagesList() {
		return predefinedTravelPackagesList;
	}
	public void setPredefinedTravelPackagesList(
			List<PredefinedTravelPackageDTO> predefinedTravelPackagesList) {
		this.predefinedTravelPackagesList = predefinedTravelPackagesList;
	}
	public List<UserDTO> getUsersList() {
		return usersList;
	}
	public void setUsersList(List<UserDTO> usersList) {
		this.usersList = usersList;
	}
	public List<TravelComponentDTO> getTravelComponentsList() {
		return travelComponentsList;
	}
	public void setTravelComponentsList(List<TravelComponentDTO> travelComponentsList) {
		this.travelComponentsList = travelComponentsList;
	}
}
