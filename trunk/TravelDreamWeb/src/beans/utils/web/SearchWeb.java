package beans.utils.web;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import beans.accountmanagement.UserDTO;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.PredefinedTravelPackageDTO;
import beans.utils.SearchDTOInterface;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List; 

@ManagedBean(name="SearchWeb")
@RequestScoped
public class SearchWeb {
	@EJB
	private SearchDTOInterface finder;

	@Inject
	private Data_Exchange data;
	public Data_Exchange getData(){
		return data;
	}
	public void setData(Data_Exchange data){
		this.data = data;
	}
	
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
		// initializations
		searchCriteria = new TravelComponentDTO();
		packageName = new String();
		firstName = new String();
		lastName = new String();
	}

	//---------------------------
	// SETTERS AND GETTERS	
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
	// PREDEFINED TRAVEL PACKAGES	
	public void searchPredefinedTravelPackages(){
		if(packageName.isEmpty())
			packageName = null;
		Date departureDateCriteria = null;
		Date returnDateCriteria = null;
		if(departureDate != null)
			departureDateCriteria =  new Date(departureDate.getTime());
		if(returnDate != null)
			returnDateCriteria = new Date(returnDate.getTime());
		
		predefinedTravelPackagesList = finder.findPredefinedTravelPackage(packageName, departureDateCriteria, returnDateCriteria);
		
		if(predefinedTravelPackagesList == null || predefinedTravelPackagesList.isEmpty())
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"No Results", "Your search has given ro results")); 
		else{
			data.setPredefinedTravelPackagesList(predefinedTravelPackagesList);
			RequestContext.getCurrentInstance().openDialog("/misc/search/travelpackage.xhtml");
		}
	}
	
	public void browseAllPredefinedTravelPackages(){
		predefinedTravelPackagesList = finder.findAllPredefinedTravelPackages();
		
		if(predefinedTravelPackagesList == null || predefinedTravelPackagesList.isEmpty())
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"No Results", "Your search has given ro results")); 
		else{
			data.setPredefinedTravelPackagesList(predefinedTravelPackagesList);
			RequestContext.getCurrentInstance().openDialog("/misc/search/travelpackage.xhtml");
		}
	}
	
	public void onPredefinedTravelPackageChosen(SelectEvent event) throws IOException{
		// open up the predefinedTravelPackage page
		PredefinedTravelPackageDTO predefinedTravelPackage = (PredefinedTravelPackageDTO) event.getObject(); 
		List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
		toSend.add(predefinedTravelPackage);
		data.setPredefinedTravelPackagesList(toSend);
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml"); // TODO: waiting for TravelPackage page	
	}
		
	//---------------------------
	// CUSTOMERS
	public void searchCustomers(){
		if(firstName.isEmpty())
			firstName = null;
		
		if(lastName.isEmpty())
			lastName = null;
		
		usersList = finder.findUser(firstName, lastName);
		
		if(usersList == null || usersList.isEmpty())
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"No Results", "Your search has given ro results")); 
		else{
			data.setUsersList(usersList);
			RequestContext.getCurrentInstance().openDialog("/misc/search/customer.xhtml");
		}
	}
	public void onCustomerChosen(SelectEvent event) throws IOException{
		UserDTO user = (UserDTO) event.getObject(); 
		// if the user is selecting is own name, then shows his personal page. This is indeed also to prevent from unexpected behavior (such paying his own components in the gift list)
		if(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().equals(user.getEmail()))
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personal_page.xhtml");
			
		else // open up customer info page
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");//TODO: Waiting for Customer Info Page
	}
	
	//---------------------------
	// TRAVEL COMPONENTS
	public void searchTravelComponents(){
		//TODO TBD
	}
	public void browseAllTravelComponents(){
		//TODO TBD
	}
	
	
}
