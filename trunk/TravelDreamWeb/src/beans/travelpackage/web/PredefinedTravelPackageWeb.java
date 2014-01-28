package beans.travelpackage.web;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
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
	private EmployeeHandlerInterface employeeHandler;
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
		boolean ok = true;
		try{
			predTP=data.getPredefinedTravelPackagesList().get(0);
		}
		catch(IndexOutOfBoundsException e){ok = false;/*does nothign*/}
		
		if(ok == true){
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
						TravelComponentDTO hotel = (TravelComponentDTO) hotelsRoot.get(j).getData(); 
						TravelComponentDTO newHotel = component;
											
						if(hotel.getSupplyingCompany().equals(newHotel.getSupplyingCompany()) && hotel.getHotelCity().equals(newHotel.getHotelCity())){
							new DefaultTreeNode(component,hotelsRoot.get(j));
							found = true;
						}							
					}
					if(found == false){
						TravelComponentDTO rootHotel = new TravelComponentDTO();
						rootHotel.setType(type);
						rootHotel.setSupplyingCompany(component.getSupplyingCompany());
						rootHotel.setHotelCity(component.getHotelCity());
						rootHotel.setId(-1);
						TreeNode hotel = new DefaultTreeNode(rootHotel,root);
						new DefaultTreeNode(component,hotel);
						hotelsRoot.add(hotel);
					}
	
					break;
				}
			}
		}
	}
	
	public boolean noPackage(){
		if(predTP == null)
			return true;
		
		else
			return false;
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
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				return "Date: " + sdf.format(component.getHotelDate());
				
			}
		}
		return component.getSupplyingCompany();	
	}
	
	public String fieldThree(TravelComponentDTO component){		
		if(component.getType() == ComponentType.HOTEL){
			if(isHotelRoot(component) == true){
				return "";	
				
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
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personal_travel_package.xhtml");
	}
	
	public void save() throws IOException{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		if(departureDate != null){
			predTP.setDepartureDate(new Date(departureDate.getTime()));
			if(returnDate != null){
				predTP.setReturnDate(new Date(returnDate.getTime()));		
				if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("CUSTOMER")){//if customer copy in personalized
					user = search.findUser(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
					String result = customerHandler.addNewPersonalizedTravelPackage(user, predTP);
					if(result.isEmpty()){
						facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have succesfully add the travel package in your list of package!"));
						FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personal_travel_package.xhtml");
					}
					else{
						List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
						toSend.add(predTP);
						data.setPredefinedTravelPackagesList(toSend);
						facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error","Something went wrong. Server replied: " + result));	
						FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/modify_predefinedtravelpackage.xhtml");
					}						
				}
				
				else if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("EMPLOYEE")){//if employee update
					String result = employeeHandler.updatePredefinedTravelPackage(predTP);
					if(result.isEmpty()){
						List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
						toSend.add(predTP);
						data.setPredefinedTravelPackagesList(toSend);
						facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have succesfully updated the predefined travel package"));
						FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/predefinedtravelpackage.xhtml");	
					}
					else{
						List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
						toSend.add(predTP);
						data.setPredefinedTravelPackagesList(toSend);
						facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error","Something went wrong. Server replied: " + result));	
						FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/modify_predefinedtravelpackage.xhtml");
					}						
				}
			}
			else{		
				List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
				toSend.add(predTP);
				data.setPredefinedTravelPackagesList(toSend);
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "You must specify the return date"));
				FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/modify_predefinedtravelpackage.xhtml");}
		}			
		else{
			List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
			toSend.add(predTP);
			data.setPredefinedTravelPackagesList(toSend);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "You must specify the departure date"));
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/modify_predefinedtravelpackage.xhtml");
		}
	}

	public void modify(PredefinedTravelPackageDTO helper) throws IOException{		
		List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
		toSend.add(helper);
		data.setPredefinedTravelPackagesList(toSend);
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/modify_predefinedtravelpackage.xhtml");	
	}
	
	public void deleteComponent(TravelComponentDTO component) throws IOException{		
		boolean result = employeeHandler.removeTravelComponentFromPredefinedTravelPackage(predTP, component);
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
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "You cannot delete the last component from the package, it would make it empty")); 
		
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/modify_predefinedtravelpackage.xhtml");
	}
	
	
	public void addComponent(TravelComponentDTO component) throws IOException
	{
		FacesMessage message = null;
		boolean result = employeeHandler.addTravelComponentToPredefinedTravelPackage(predTP, component);
		
		if(result == true)
			message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have added the component to your package, click on the save button to submit your changes!"); 	
		else
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "The component is already in the travel package!"); 	
		
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
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		
		user = search.findUser(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
		String result = customerHandler.addNewPersonalizedTravelPackage(user, predTP);	
		
		if(result.isEmpty()){
			List<PersonalizedTravelPackageDTO> owned = search.findAllPersonalizedTravelPackages(user);
			for(int i = 0; i < owned.size(); i++)
				if(owned.get(i).getName().equals(predTP.getName()))
					result = customerHandler.confirmPersonalizedTravelPackage(owned.get(i));
			if(result.isEmpty()){
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Travel Package succesfully confirmed!")); 
				FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personal_travel_package.xhtml");
			}
			else{
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "An error occured. The server replied: " + result));
				List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
				toSend.add(predTP);
				data.setPredefinedTravelPackagesList(toSend);
				FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/predefinedtravelpackage.xhtml");
			}
		}
		else{
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "An error occured. The server replied: " + result)); 
			List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
			toSend.add(predTP);
			data.setPredefinedTravelPackagesList(toSend);
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/predefinedtravelpackage.xhtml");
		}	
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
