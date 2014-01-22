package beans.utils.web;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

import beans.accountmanagement.UserDTO;
import beans.travelcomponent.ComponentType;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.PredefinedTravelPackageDTO;
import beans.utils.SearchDTOInterface;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
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
	private String flightSupplyingCompany;
	private String hotelSupplyingCompany;
	private String excursionSupplyingCompany;	
	private String packageName;
	private java.util.Date departureDate;
	private java.util.Date returnDate;
	private java.util.Date flightDepartureDateTime;
	private java.util.Date flightArrivalDateTime;
	private java.util.Date hotelStartingDate;
	private java.util.Date hotelEndingDate;
	private java.util.Date excursionDateTime;
	private String firstName;
	private String lastName;
	
	private List<PredefinedTravelPackageDTO> predefinedTravelPackagesList;
	private List<UserDTO> usersList;
	private List<TravelComponentDTO> travelComponentsList;
	
	public SearchWeb(){
		// initializations
		searchCriteria = new TravelComponentDTO();
		searchCriteria.setType(ComponentType.FLIGHT); // default value (since is the first tab displayed)
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
	public String getFlightSupplyingCompany() {
		return flightSupplyingCompany;
	}
	public void setFlightSupplyingCompany(String flightSupplyingCompany) {
		this.flightSupplyingCompany = flightSupplyingCompany;
	}
	public String getHotelSupplyingCompany() {
		return hotelSupplyingCompany;
	}
	public void setHotelSupplyingCompany(String hotelSupplyingCompany) {
		this.hotelSupplyingCompany = hotelSupplyingCompany;
	}
	public String getExcursionSupplyingCompany() {
		return excursionSupplyingCompany;
	}
	public void setExcursionSupplyingCompany(String excursionSupplyingCompany) {
		this.excursionSupplyingCompany = excursionSupplyingCompany;
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
	public java.util.Date getFlightDepartureDateTime() {
		return flightDepartureDateTime;
	}
	public void setFlightDepartureDateTime(java.util.Date flightDepartureDateTime) {
		this.flightDepartureDateTime = flightDepartureDateTime;
	}
	public java.util.Date getFlightArrivalDateTime() {
		return flightArrivalDateTime;
	}
	public void setFlightArrivalDateTime(java.util.Date flightArrivalDateTime) {
		this.flightArrivalDateTime = flightArrivalDateTime;
	}
	public java.util.Date getHotelStartingDate() {
		return hotelStartingDate;
	}
	public void setHotelStartingDate(java.util.Date hotelStartingDate) {
		this.hotelStartingDate = hotelStartingDate;
	}
	public java.util.Date getHotelEndingDate() {
		return hotelEndingDate;
	}
	public void setHotelEndingDate(java.util.Date hotelEndingDate) {
		this.hotelEndingDate = hotelEndingDate;
	}
	public java.util.Date getExcursionDateTime() {
		return excursionDateTime;
	}
	public void setExcursionDateTime(java.util.Date excursionDateTime) {
		this.excursionDateTime = excursionDateTime;
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
			RequestContext.getCurrentInstance().openDialog("/misc/search/travelpackage_search.xhtml");
		}
	}
	
	public void browseAllPredefinedTravelPackages(){
		predefinedTravelPackagesList = finder.findAllPredefinedTravelPackages();
		
		if(predefinedTravelPackagesList == null || predefinedTravelPackagesList.isEmpty())
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"No Results", "Your search has given ro results")); 
		else{
			data.setPredefinedTravelPackagesList(predefinedTravelPackagesList);
			RequestContext.getCurrentInstance().openDialog("/misc/search/travelpackage_search.xhtml");
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
			RequestContext.getCurrentInstance().openDialog("/misc/search/customer_search.xhtml");
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
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");//TODO: waiting for Customer Info Page
		}
	}
	
	//---------------------------
	// TRAVEL COMPONENTS
	public void tabChangeListener(TabChangeEvent event){
		searchCriteria = new TravelComponentDTO(); // flushes previous criteria
		switch(event.getTab().getId()){
		case "flights":
			searchCriteria.setType(ComponentType.FLIGHT);
			break;
		case "hotels":
			searchCriteria.setType(ComponentType.HOTEL);
			break;
		case "excursions":
			searchCriteria.setType(ComponentType.EXCURSION);
			break;
		}
	}
	
	public void searchTravelComponents(){
		switch(searchCriteria.getType()){
		case FLIGHT:
			if(flightSupplyingCompany.isEmpty())
				searchCriteria.setSupplyingCompany(null);
			if(flightDepartureDateTime != null)
				searchCriteria.setFlightDepartureDateTime(new Timestamp(flightDepartureDateTime.getTime()));		
			else
				searchCriteria.setFlightDepartureDateTime(null);
		
			if(flightArrivalDateTime != null)
				searchCriteria.setFlightArrivalDateTime(new Timestamp(flightArrivalDateTime.getTime()));	
			else
				searchCriteria.setFlightArrivalDateTime(null);
			
			if(searchCriteria.getFlightDepartureCity().isEmpty())
				searchCriteria.setFlightDepartureCity(null);
			
			if(searchCriteria.getFlightArrivalCity().isEmpty())
				searchCriteria.setFlightArrivalCity(null);
			
			if(searchCriteria.getFlightCode().isEmpty())
				searchCriteria.setFlightCode(null);
			
			travelComponentsList = finder.findTravelComponent(searchCriteria);
			
			break;
		case HOTEL:
			if(hotelSupplyingCompany.isEmpty())
				searchCriteria.setSupplyingCompany(null);
			if(searchCriteria.getHotelCity().isEmpty())
				searchCriteria.setHotelCity(null);
			if(hotelStartingDate != null && hotelEndingDate != null){
				Calendar start = Calendar.getInstance();
				start.setTime(hotelStartingDate);
				Calendar end = Calendar.getInstance();
				end.setTime(hotelEndingDate);
				travelComponentsList.clear();
				for (java.util.Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
				    searchCriteria.setHotelDate(new Date(date.getTime()));
				    travelComponentsList.addAll(finder.findTravelComponent(searchCriteria));
				}
			}
			else{
				if(hotelStartingDate != null)
					searchCriteria.setHotelDate(new Date(hotelStartingDate.getTime()));
				else if(hotelEndingDate != null)
					searchCriteria.setHotelDate(new Date(hotelEndingDate.getTime()));
				
				travelComponentsList = finder.findTravelComponent(searchCriteria);
			}
			
			break;
		case EXCURSION:
			if(excursionSupplyingCompany.isEmpty())
				searchCriteria.setSupplyingCompany(null);
			if(searchCriteria.getExcursionCity().isEmpty())
				searchCriteria.setExcursionCity(null);
			
			if(searchCriteria.getExcursionDescription().isEmpty())
				searchCriteria.setExcursionDescription(null);
			
			if(excursionDateTime != null)
				searchCriteria.setExcursionDateTime(new Timestamp(excursionDateTime.getTime()));
			else
				searchCriteria.setExcursionDateTime(null);
			
			travelComponentsList = finder.findTravelComponent(searchCriteria);
			
			break;
		}
		
		if(travelComponentsList == null || travelComponentsList.isEmpty())
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"No Results", "Your search has given ro results")); 
		else{
			data.setTravelComponentsList(travelComponentsList);
			RequestContext.getCurrentInstance().openDialog("/misc/search/travelcomponent_search.xhtml");
		}
	}
	public void browseAllTravelComponents(){
		travelComponentsList = finder.findAllTravelComponents();
		
		if(travelComponentsList == null || travelComponentsList.isEmpty())
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"No Results", "Your search has given ro results")); 
		else{
			data.setTravelComponentsList(travelComponentsList);
			RequestContext.getCurrentInstance().openDialog("/misc/search/travelcomponent_search.xhtml");
		}
	}
	public void onTravelComponentChosen(SelectEvent event) throws IOException{
		// open up the travelComponent page
		TravelComponentDTO travelComponent = (TravelComponentDTO) event.getObject(); 
		List<TravelComponentDTO> toSend = new ArrayList<TravelComponentDTO>();
		toSend.add(travelComponent);
		data.setTravelComponentsList(toSend);
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml"); // TODO: waiting for TravelComponent page
	}
}
