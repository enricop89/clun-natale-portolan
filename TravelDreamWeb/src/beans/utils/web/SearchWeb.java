package beans.utils.web;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import beans.accountmanagement.UserDTO;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.PredefinedTravelPackageDTO;
import beans.utils.SearchDTOInterface;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List; 

@ManagedBean(name="SearchWeb")
@SessionScoped
public class SearchWeb {
	@EJB
	private SearchDTOInterface finder;
		
	private boolean isCustomer; // otherwise is employee, cannot be another value since the folder is not accessible by unauthenticated users
	
	private TravelComponentDTO searchCriteria;
	private String packageName;
	private java.util.Date departureDate;
	private java.util.Date returnDate;
	private String firstName;
	private String lastName;
	
	private List<PredefinedTravelPackageDTO> predefinedTravelPackagesList;
	private List<UserDTO> usersList;
	private List<TravelComponentDTO> travelComponentsList;
	
	public SearchWeb(){
		if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("CUSTOMER"))
			isCustomer = true;
		else if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("EMPLOYEE"))
			isCustomer = false;
		
		// initializations
		searchCriteria = new TravelComponentDTO();
		packageName = new String();
		firstName = new String();
		lastName = new String();
		predefinedTravelPackagesList = new ArrayList<PredefinedTravelPackageDTO>();
		usersList = new ArrayList<UserDTO>();
		travelComponentsList = new ArrayList<TravelComponentDTO>();
	}

	//---------------------------
	// SETTERS AND GETTERS
	public boolean getIsCustomer(){
		return isCustomer;
	}
	
	public TravelComponentDTO getSearchCriteria(){
		return searchCriteria;
	}
	public void setSearchCriteria(TravelComponentDTO searchCriteria){
		this.searchCriteria = searchCriteria;
	}
	public String getPackageName(){
		return packageName;
	}
	public void setPackageName(String packageName){
		this.packageName = packageName;
	}
	public java.util.Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(java.util.Date returnDate) {
		this.returnDate = returnDate;
	}

	public java.util.Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(java.util.Date departureDate) {
		this.departureDate = departureDate;
	}
	public String getFirstName(){
		return firstName;
	}
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	public String getLastName(){
		return lastName;
	}
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	//---------------------------
	// CLEAR FUNCTION - reset all search values, MUST be called after getting the results
	private void clear(){
		searchCriteria = new TravelComponentDTO();
		packageName = "";
		departureDate = null;
		returnDate = null;
		firstName = "";
		lastName = "";
		
		predefinedTravelPackagesList.clear();
		usersList.clear();
		travelComponentsList.clear();
	}
	
	//---------------------------
	// PREDEFINED TRAVEL PACKAGES
	public List<PredefinedTravelPackageDTO> getPredefinedTravelPackagesList(){
		List<PredefinedTravelPackageDTO> result = new ArrayList<PredefinedTravelPackageDTO>(predefinedTravelPackagesList);
		clear();
		return result;
	}
	
	public void searchPredefinedTravelPackages(){
		if(packageName.isEmpty())
			packageName = null;
		Date departureDateCriteria = null;
		Date returnDateCriteria = null;
		if(departureDate != null)
			departureDateCriteria =  new Date(departureDate.getTime());
		if(returnDate != null)
			returnDateCriteria = new Date(returnDate.getTime());
		
		predefinedTravelPackagesList = new ArrayList<PredefinedTravelPackageDTO>(finder.findPredefinedTravelPackage(packageName, departureDateCriteria, returnDateCriteria));
		
		if(predefinedTravelPackagesList == null || predefinedTravelPackagesList.isEmpty())
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"No Results", "Your search has given ro results")); 
		else
			RequestContext.getCurrentInstance().openDialog("travelpackage");
	}
	
	public void browseAllPredefinedTravelPackages(){
		predefinedTravelPackagesList = new ArrayList<PredefinedTravelPackageDTO>(finder.findAllPredefinedTravelPackages());
		if(predefinedTravelPackagesList == null || predefinedTravelPackagesList.isEmpty())
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"No Results", "Your search has given ro results")); 
		else
			RequestContext.getCurrentInstance().openDialog("travelpackage");
	}
		
	//---------------------------
	// CUSTOMERS
	public List<UserDTO> getUsersList(){
		List<UserDTO> result = new ArrayList<UserDTO>(usersList);
		clear();
		return result;
	}
	
	public void searchCustomers(){
		if(firstName.isEmpty())
			firstName = null;
		
		if(lastName.isEmpty())
			lastName = null;
		
		usersList = new ArrayList<UserDTO>(finder.findUser(firstName, lastName));
		
		if(usersList == null || usersList.isEmpty())
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"No Results", "Your search has given ro results")); 
		else
			RequestContext.getCurrentInstance().openDialog("customer");
	}
	
	//---------------------------
	// TRAVEL COMPONENTS
	public List<TravelComponentDTO> getTravelComponentsList(){
		List<TravelComponentDTO> result = new ArrayList<TravelComponentDTO>(travelComponentsList);
		clear();
		return result;
	}
	public void searchTravelComponents(){
		// TBD
	}
	public void browseAllTravelComponents(){
		// TBD
	}
/*	public void selectFromDialog(TravelComponentDTO travelComponent){
		// TBD		
	}
*/
	
}
