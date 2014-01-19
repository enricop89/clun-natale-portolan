package beans.utils.web;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import beans.accountmanagement.UserDTO;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.PredefinedTravelPackageDTO;
import beans.utils.SearchDTOInterface;

import java.sql.Date;
import java.util.List; 

@ManagedBean(name="SearchWeb")
@RequestScoped
public class SearchWeb {
	@EJB
	private SearchDTOInterface search;
		
	private boolean isCustomer; // otherwise is employee, cannot be another value since the folder is not accessible by unauthenticated users
	
	private TravelComponentDTO searchCriteria;
	private String packageName;
	private Date departureDate;
	private Date returnDate;
	private String firstName;
	private String lastName;
	
	private List<PredefinedTravelPackageDTO> predefinedTravelPackagesList;
	private List<UserDTO> usersList;
	
	public SearchWeb(){
		if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("CUSTOMER"))
			isCustomer = true;
		else if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("EMPLOYEE"))
			isCustomer = false;
	}
	
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
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public Date getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(Date departureDate) {
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
	
	public List<PredefinedTravelPackageDTO> getPredefinedTravelPackagesList(){
		return predefinedTravelPackagesList;
	}
	
	public void searchPredefinedTravelPackages(){
		if(packageName.isEmpty())
			packageName = null;
		
		predefinedTravelPackagesList = search.findPredefinedTravelPackage(packageName,departureDate,returnDate);
		RequestContext.getCurrentInstance().openDialog("travelpackage");
	}
	
	public void browseAllPredefinedTravelPackages(){
		predefinedTravelPackagesList = search.findAllPredefinedTravelPackages();
		RequestContext.getCurrentInstance().openDialog("travelpackage");
	}
	
	public void selectFromDialog(PredefinedTravelPackageDTO predefinedTravelPackage){
		// open up the predefinedTravelPackage page
	}
	
	public List<UserDTO> getUsersList(){
		return usersList;
	}
	
	public void searchCustomers(){
		if(firstName.isEmpty())
			firstName = null;
		
		if(lastName.isEmpty())
			lastName = null;
		
		usersList = search.findUser(firstName, lastName);
		RequestContext.getCurrentInstance().openDialog("customer");
	}
	
	public void selectFromDialog(UserDTO user){
		// if the user is selecting is own name, then shows his personal page. This is indeed also to prevent from unexpected behavior (such paying his own components in the gift list)
		/*if(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser() == user.getEmail())
			
			
		else // open up customer info page
		*/
		
	}

	
}
