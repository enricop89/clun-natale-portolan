package beans.travelpackage.web;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import beans.accountmanagement.UserDTO;
import beans.customerhandler.CustomerHandlerInterface;
import beans.employeehandler.EmployeeHandlerInterface;
import beans.travelcomponent.ComponentType;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.PersonalizedTravelPackageDTO;
import beans.travelpackage.PredefinedTravelPackageDTO;
import beans.utils.SearchDTOInterface;
import beans.utils.web.Data_Exchange;

@ManagedBean(name="PredefinedTravelPackageWeb")
@ViewScoped
public class PredefinedTravelPackageWeb {
	@EJB
	private SearchDTOInterface search;
	@EJB
	private EmployeeHandlerInterface employee;
	@EJB
	private CustomerHandlerInterface customerHandler;
	
	private PredefinedTravelPackageDTO predTP;
	private UserDTO user;
	private java.util.Date departureDate;
	private java.util.Date returnDate;
	
	private TreeNode root;
	private List<TreeNode> hotelsRoot;

	@Inject
	private Data_Exchange data;
	public Data_Exchange getData(){
		return data;
	}
	public void setData(Data_Exchange data){
		this.data = data;
	}
	
	@PostConstruct
	public void init(){	
		predTP=data.getPredefinedTravelPackagesList().get(0);
		departureDate=new java.util.Date (predTP.getDepartureDate().getTime());
		returnDate=new java.util.Date (predTP.getReturnDate().getTime());
		//construct the root
		root = new DefaultTreeNode("root", null);
		for(int i = 0; i < predTP.getTravelComponents().size(); i++){
			TravelComponentDTO component = predTP.getTravelComponents().get(i);
			ComponentType type = component.getType();
			switch(type){
			case FLIGHT:
				new DefaultTreeNode(component,root);
				break;
			case EXCURSION:
				new DefaultTreeNode(component,root);
				break;
			case HOTEL:
				if(hotelsRoot == null){
					hotelsRoot = new ArrayList<TreeNode>();
				}
				boolean found = false;
				for(int j = 0; j < hotelsRoot.size(); j++)
				{
					TravelComponentDTO hotel;
					TravelComponentDTO newHotel;
					
						hotel = (TravelComponentDTO) hotelsRoot.get(j).getData();
					newHotel = component;
					
					
					if(hotel.getSupplyingCompany().equals(newHotel.getSupplyingCompany()) && hotel.getHotelCity().equals(newHotel.getHotelCity())){
						new DefaultTreeNode(component,hotelsRoot.get(j));
						found = true;
					}							
				}
				if(found == false){
					TravelComponentDTO rootHotel = new TravelComponentDTO();
					rootHotel.setType(type);
					rootHotel.setSupplyingCompany(component.getSupplyingCompany());
					rootHotel.setId(-1);
					TreeNode hotel = new DefaultTreeNode(rootHotel,root);
					new DefaultTreeNode(component,hotel);
					hotelsRoot.add(hotel);
				}

				break;
			}
		}			
	}
	
	public String fieldOne(TravelComponentDTO component){
		
		switch(component.getType()){
		case EXCURSION:
			return "Excursion";
		case FLIGHT:
			return "Flight";
		case HOTEL:
			if(isHotelRoot(component) == true){
				return "Hotel";
				
			}
			else{
				return "City: " + component.getHotelCity();
				
			}
		}
		return ""; // dummy return
	}
	
	public String fieldTwo(TravelComponentDTO component){
		
		if(component.getType() == ComponentType.HOTEL){
			if(isHotelRoot(component) == true){
				return component.getSupplyingCompany();	
				
			}
			else{
				return "Date: " + component.getHotelDate();
				
			}
		}
		return component.getSupplyingCompany();	
	}
public String fieldThree(TravelComponentDTO component){
		
		if(component.getType() == ComponentType.HOTEL){
			if(isHotelRoot(component) == true){
				return component.getSupplyingCompany();	
				
			}
			else{
				return "Availability: " + component.getAvailability();
				
			}
		}
		return component.getSupplyingCompany();	
	}
	
	public boolean isHotelRoot(TravelComponentDTO component){
		if(component.getId() == -1)
			return true;
		
		else 
			return false;
		
	}
	
	public void showComponent(TravelComponentDTO helper) throws IOException{
		//select package and go to personal_package_home
		List<TravelComponentDTO> toSend = new ArrayList<TravelComponentDTO>();
		toSend.add(helper);
		data.setTravelComponentsList(toSend);
        Map<String,Object> options = new HashMap<String, Object>();  
        options.put("resizable", false);
		RequestContext.getCurrentInstance().openDialog("/misc/dialog_travelcomponent.xhtml",options,null);	
	}

	
	public void copyInPersonalizedTravelPackage(PredefinedTravelPackageDTO helper) throws IOException{
		user = search.findUser(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
		String result=customerHandler.addNewPersonalizedTravelPackage(user, helper);
		FacesMessage message = null;
		if(result.isEmpty()){
			message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "Your package has been succesfully saved!"); 
		}
		else{
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Something went wrong. Server replied: " + result);	
		}	
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		facesContext.addMessage(null,message); 
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personal_travel_package.xhtml?faces-redirect=true");
	}
	
public void save() throws IOException{
		
	
		if(departureDate != null)
			predTP.setDepartureDate(new Date(departureDate.getTime()));
		if(returnDate != null)
			predTP.setReturnDate(new Date(returnDate.getTime()));

		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		
		
		
		if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("CUSTOMER")){
				user = search.findUser(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
				customerHandler.addNewPersonalizedTravelPackage(user, predTP);
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have succesfully add the travel package in your list of package!"));
				FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personal_travel_package.xhtml");
		   }//if customer copy in personalized
		
		if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("EMPLOYEE")){
			employee.updatePredefinedTravelPackage(predTP);
			List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
			toSend.add(predTP);
			data.setPredefinedTravelPackagesList(toSend);
			//if employee update
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/predefinedtravelpackage.xhtml");
		
	}
}

	public void modify(PredefinedTravelPackageDTO helper) throws IOException{
	
		
		List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
		toSend.add(helper);
		data.setPredefinedTravelPackagesList(toSend);
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/modify_predefinedtravelpackage.xhtml");
	
	}
	
	public void onTravelComponentChosen(CloseEvent event) throws IOException
	{
		try{ // must use try catch, getters in Data_Exchange flushes lists by design! calling it twice is a logical error
			TravelComponentDTO travelComponent = data.getTravelComponentsList().get(0);
			employee.addTravelComponentToPredefinedTravelPackage(predTP, travelComponent);	//TODO:se salva chiamo copy, dipende se customer o employee
			List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
			toSend.add(predTP);
			data.setPredefinedTravelPackagesList(toSend);
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/modify_predefinedtravelpackage.xhtml");
		}
		catch (java.lang.IndexOutOfBoundsException e){/* does nothing */}
	}
	
	public void deleteComponent(TravelComponentDTO component) throws IOException{
		
		boolean result=employee.removeTravelComponentFromPredefinedTravelPackage(predTP, component);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
		toSend.add(predTP);
		data.setPredefinedTravelPackagesList(toSend);
		if(result==true)
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have deleted the component from your package, click on the save button to submit your changes!")); 
		else
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "You cannot delete a payed component")); 
		
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/modify_predefinedtravelpackage.xhtml");
	}
	
	
	public void addComponent(TravelComponentDTO component) throws IOException
	{
		FacesMessage message = null;
		employee.addTravelComponentToPredefinedTravelPackage(predTP, component);
		
			message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have added the component to your package, click on the save button to submit your changes!"); 
		
		
		List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
		toSend.add(predTP);
		data.setPredefinedTravelPackagesList(toSend);
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		facesContext.addMessage(null,message);
		
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/modify_predefinedtravelpackage.xhtml");
	}
	
	public void showComponentSearch(TravelComponentDTO component){
		List<TravelComponentDTO> toSend = new ArrayList<TravelComponentDTO>();
		toSend.add(component);
		data.setTravelComponentsList(toSend);
        Map<String,Object> options = new HashMap<String, Object>();  
        options.put("resizable", false); 
		RequestContext.getCurrentInstance().openDialog("/misc/dialog_travelcomponent.xhtml",options,null);
	}
	
	public void confirmPackage() throws IOException{
		//TODO:DA FINIRE farlo lato EJB (DOMANDA: IN CHE SENSO?)
		PersonalizedTravelPackageDTO personalizedPackage= new PersonalizedTravelPackageDTO();
		
		String result = customerHandler.confirmPersonalizedTravelPackage(personalizedPackage);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		List<PersonalizedTravelPackageDTO> toSend = new ArrayList<PersonalizedTravelPackageDTO>();
		toSend.add(personalizedPackage);
		data.setPersonalizedTravelPackagesList(toSend);
		if(result.isEmpty())
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "Travel package succesfully confirmed!")); 
		else
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "An error occured. The server replied: " + result)); 
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personalized_travel_package.xhtml");
	}
	
	public void onClose(){
		FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove("SearchTravelComponents");
	}
	
	public boolean checkIfCustomer() throws IOException{
		
		if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("CUSTOMER"))
				return true; //the remote user is a customer
		
			return false; 
	}
	public boolean checkIfEmployee() throws IOException{
		if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("EMPLOYEE"))
			return true;
	
		return false;
	
	}
	
	
	//SETTERS AND GETTERS
	public PredefinedTravelPackageDTO getPredTP() {
		return predTP;
	}
	public void setPredTP(PredefinedTravelPackageDTO predTP) {
		this.predTP = predTP;
	}
	public EmployeeHandlerInterface getEmployee() {
		return employee;
	}
	public void setEmployee(EmployeeHandlerInterface employee) {
		this.employee = employee;
	}
	public java.util.Date getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(java.util.Date departureDate) {
		this.departureDate = departureDate;
	}
	public java.util.Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(java.util.Date returnDate) {
		this.returnDate = returnDate;
	}
	
	public TreeNode getRoot() {
		return root;
	}
	public void setRoot(TreeNode root) {
		this.root = root;
	}
	
	
}
