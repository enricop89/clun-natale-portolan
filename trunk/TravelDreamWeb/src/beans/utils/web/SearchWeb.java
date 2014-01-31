package beans.utils.web;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import beans.accountmanagement.UserDTO;
import beans.employeehandler.EmployeeHandlerInterface;
import beans.travelpackage.PredefinedTravelPackageDTO;
import beans.utils.SearchDTOInterface;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List; 
import java.util.Map;

@ManagedBean(name="SearchWeb")
@ViewScoped
public class SearchWeb {
	@EJB
	private SearchDTOInterface finder;
	@EJB
	private EmployeeHandlerInterface employeeHandler;

	@Inject
	private Data_Exchange data;
	public Data_Exchange getData(){
		return data;
	}
	public void setData(Data_Exchange data){
		this.data = data;
	}
	
	private String packageName;
	private java.util.Date departureDate;
	private java.util.Date returnDate;
	private String firstName;
	private String lastName;
	
	private List<PredefinedTravelPackageDTO> predefinedTravelPackagesList;
	private List<UserDTO> usersList;
	
	public SearchWeb(){
		// initializations
		packageName = new String();
		firstName = new String();
		lastName = new String();
	}
	//---------------------------
	// SETTERS AND GETTERS	
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
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"No Results", "Your search has given no results")); 
		else{
			data.setPredefinedTravelPackagesList(predefinedTravelPackagesList);
	        Map<String,Object> options = new HashMap<String, Object>();  
	        options.put("resizable", false);
			RequestContext.getCurrentInstance().openDialog("/misc/search/travelpackage_search.xhtml",options,null);
		}
	}
	
	public void browseAllPredefinedTravelPackages(){
		predefinedTravelPackagesList = finder.findAllPredefinedTravelPackages();
		
		if(predefinedTravelPackagesList == null || predefinedTravelPackagesList.isEmpty())
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"No Results", "Your search has given no results")); 
		else{
			data.setPredefinedTravelPackagesList(predefinedTravelPackagesList);
	        Map<String,Object> options = new HashMap<String, Object>();  
	        options.put("resizable", false);
			RequestContext.getCurrentInstance().openDialog("/misc/search/travelpackage_search.xhtml",options,null);
		}
	}
	
	public void onPredefinedTravelPackageChosen(SelectEvent event) throws IOException{
		// open up the predefinedTravelPackage page
		@SuppressWarnings("unchecked")
		Map<Boolean, PredefinedTravelPackageDTO> sent = (Map<Boolean, PredefinedTravelPackageDTO>) event.getObject(); 
		if(sent.containsKey(false)){ // visualization
			List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
			toSend.add(sent.get(false));
			data.setPredefinedTravelPackagesList(toSend);
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/predefinedtravelpackage.xhtml");
		}
		else if(sent.containsKey(true)){ // deletion
			employeeHandler.deletePredefinedTravelPackage(sent.get(true));
	    	FacesContext facesContext = FacesContext.getCurrentInstance();
			Flash flash = facesContext.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			flash.setRedirect(true);	
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "Predefined travel package deleted!")); 
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/employee/control_panel.xhtml");
		}
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
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"No Results", "Your search has given no results")); 
		else{
			data.setUsersList(usersList);
	        Map<String,Object> options = new HashMap<String, Object>();  
	        options.put("resizable", false);
			RequestContext.getCurrentInstance().openDialog("/misc/search/customer_search.xhtml",options,null);
		}
	}
	public void onCustomerChosen(SelectEvent event) throws IOException{
		UserDTO user = (UserDTO) event.getObject(); 
		// if the user is selecting is own name, then shows his personal page. This is indeed also to prevent from unexpected behavior (such paying his own components in the gift list)
		if(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser().equals(user.getEmail()))
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personal_page.xhtml");
			
		else{ // open up customer info page
			List<UserDTO> toSend = new ArrayList<UserDTO>();
			toSend.add(user);
			data.setUsersList(toSend);
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/other_customer/personal_page.xhtml");
		}
	}
}
