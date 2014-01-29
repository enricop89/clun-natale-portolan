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

import beans.customerhandler.CustomerHandlerInterface;
import beans.travelcomponent.ComponentType;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.Components_HelperDTO;
import beans.travelpackage.PersonalizedTravelPackageDTO;
import beans.utils.SearchDTOInterface;
import beans.utils.web.Data_Exchange;

@ManagedBean(name="PersonalizedTravelPackageWeb")
@ViewScoped
public class PersonalizedTravelPackageWeb {

	@Inject
	private Data_Exchange data;
	public Data_Exchange getData(){
		return data;
	}
	public void setData(Data_Exchange data){
		this.data = data;
	}
	
	@EJB
	private SearchDTOInterface finder;
	@EJB
	private CustomerHandlerInterface customerHandler;
	
	private PersonalizedTravelPackageDTO personalizedPackage;
	private java.util.Date departureDate;
	private java.util.Date returnDate;
	
	private TreeNode root;
	private List<TreeNode> hotelsRoot;
	private List<Integer> hotelsRootAlreadyShown;
	
	@PostConstruct
	public void init(){	
		String shareId = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("share");
		boolean ok = true;
		if(shareId != null) // the request has parsed the share parameter, this is "join package" context!			
			try{
				Long id = Long.parseLong(shareId);
				personalizedPackage = finder.findPersonalizedTravelPackage(id);
			}
			catch(NumberFormatException | NullPointerException e){
				ok = false;
			}
		else{
			try{
				personalizedPackage = data.getPersonalizedTravelPackagesList().get(0);
			}
			catch(IndexOutOfBoundsException e){ok = false;/*does nothign*/}
		}
			
		
		if(ok == true){
			departureDate = new java.util.Date (personalizedPackage.getDepartureDate().getTime());
			returnDate = new java.util.Date (personalizedPackage.getReturnDate().getTime());
			
			//Construct the tree
			
			root = new DefaultTreeNode("root", null);
			for(int i = 0; i < personalizedPackage.getTravelComponents().size(); i++){
				Components_HelperDTO component = personalizedPackage.getTravelComponents().get(i);
				ComponentType type;
				if(component.getTravelElement() != null)
					type = component.getPersistence().getType();
				else
					type = component.getTravelComponent().getType();
				
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
						hotelsRootAlreadyShown = new ArrayList<Integer>(); 
					}
					boolean found = false;
					for(int j = 0; j < hotelsRoot.size(); j++)
					{
						TravelComponentDTO hotel;
						TravelComponentDTO newHotel;
						if(((Components_HelperDTO) hotelsRoot.get(j).getData()).getTravelElement() != null)
							hotel = ((Components_HelperDTO) hotelsRoot.get(j).getData()).getPersistence();
						else 
							hotel = ((Components_HelperDTO) hotelsRoot.get(j).getData()).getTravelComponent();
						
						if(component.getTravelElement()!=null)
							newHotel = component.getPersistence();
						else
							newHotel = component.getTravelComponent();
						
						if(hotel.getSupplyingCompany().equals(newHotel.getSupplyingCompany()) && hotel.getHotelCity().equals(newHotel.getHotelCity())){
							new DefaultTreeNode(component,hotelsRoot.get(j));
							found = true;
						}							
					}
					if(found == false){
						TreeNode hotel = new DefaultTreeNode(component,root);
						new DefaultTreeNode(component,hotel);
						hotelsRoot.add(hotel);
						hotelsRootAlreadyShown.add(0);
					}

					break;
				}
			}			
		}
	}
	
	//------------------------
	// SETTERS AND GETTERS
	
	public TreeNode getRoot() {
		return root;
	}
	public void setRoot(TreeNode root) {
		this.root = root;
	}
	public PersonalizedTravelPackageDTO getPersonalizedPackage() {
		return personalizedPackage;
	}
	public void setPersonalizedPackage(PersonalizedTravelPackageDTO personalizedPackage) {
		this.personalizedPackage = personalizedPackage;
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
	
	//-----------------------		
	
	public String fieldOne(Components_HelperDTO element){
		TravelComponentDTO component;
		
		if(element.getTravelElement() != null)
			component = element.getPersistence();
		else
			component = element.getTravelComponent();
		
		switch(component.getType()){
		case EXCURSION:
			return "Excursion";
		case FLIGHT:
			return "Flight";
		case HOTEL:
			if(isHotelRoot(element) == true){
				return "Hotel";
				
			}
			else{
				return "City: " + component.getHotelCity();
				
			}
		}
		return ""; // dummy return
	}
	
	public String fieldTwo(Components_HelperDTO element){
		TravelComponentDTO component;
		
		if(element.getTravelElement() != null)
			component = element.getPersistence();
		else
			component = element.getTravelComponent();
		if(component.getType() == ComponentType.HOTEL){
			if(isHotelRoot(element) == true){
				return component.getSupplyingCompany();	
				
			}
			else{
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				return "Date: " + sdf.format(component.getHotelDate());				
			}
		}
		return component.getSupplyingCompany();	
	}
	
	public boolean isHotelRoot(Components_HelperDTO component){
		ComponentType type;
		
		if(component.getTravelElement() != null)
			type = component.getPersistence().getType();
		else
			type = component.getTravelComponent().getType();
		
		if(type == ComponentType.HOTEL){
			for(int i = 0; i < hotelsRoot.size(); i++){	
				if(component == hotelsRoot.get(i).getData() && hotelsRootAlreadyShown.get(i) < 7){ // this 7 is the number of times this function is called on each line of the TreeTable
					hotelsRootAlreadyShown.set(i, hotelsRootAlreadyShown.get(i) + 1);
					return true;	
					
				}
			}
		}
		return false;
	}
	
	public boolean noPackage(){
		if(personalizedPackage == null)
			return true;
		
		else
			return false;
	}
	
	public boolean checkStatus(Components_HelperDTO toChcek){
			if(toChcek.getTravelElement()!=null)
				return true;
		
		return false;
	}
	
	public boolean checkPackageStatus(){
		if(personalizedPackage == null)
			return false;
		
		if(personalizedPackage.getTravelComponents().isEmpty())
			return true;
		
		for (int i=0;i<personalizedPackage.getTravelComponents().size();i++)
			if(personalizedPackage.getTravelComponents().get(i).getTravelElement()==null)
				return false;
		
		return true;
	}
			
	public boolean isOwner(){
		if(personalizedPackage == null)
			return false;
		
		if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("CUSTOMER"))
			if(personalizedPackage.getOwner().getEmail().equals(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser()))
				return true; //the remote user is also the owner of the travel package
				
		return false;  
	}
		
	public void deleteComponent(Components_HelperDTO component) throws IOException{
		String result = customerHandler.removeTravelComponentFromPersonalizedTravelPackage(personalizedPackage, component);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		List<PersonalizedTravelPackageDTO> toSend = new ArrayList<PersonalizedTravelPackageDTO>();
		toSend.add(personalizedPackage);
		data.setPersonalizedTravelPackagesList(toSend);
		if(result.isEmpty())
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have deleted the component from your package, click on the save button to submit your changes!")); 
		else
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Something went wrong. Server replied: " + result)); 
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personalized_travel_package.xhtml");
	}
	
	public void showComponent(Components_HelperDTO component){
		//select package and go to personal_package_home
		if(component.getTravelElement()==null){ //the component is not payed
			List<TravelComponentDTO> toSend = new ArrayList<TravelComponentDTO>();
			toSend.add(component.getTravelComponent());
			data.setTravelComponentsList(toSend);
	        Map<String,Object> options = new HashMap<String, Object>();  
	        options.put("resizable", false); 
			RequestContext.getCurrentInstance().openDialog("/misc/dialog_travelcomponent.xhtml",options,null);	
		}
		else{	//The component is payed
			List<TravelComponentDTO> toSend = new ArrayList<TravelComponentDTO>();
			toSend.add(component.getPersistence());
			data.setTravelComponentsList(toSend);
	        Map<String,Object> options = new HashMap<String, Object>();  
	        options.put("resizable", false); 
			RequestContext.getCurrentInstance().openDialog("/misc/dialog_travelcomponent.xhtml",options,null);
		}		
	}
	
	public void addToGiftList(Components_HelperDTO component) throws IOException{
		boolean result = customerHandler.addTravelComponentToGiftList(finder.findUser(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser()), component, personalizedPackage);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		List<PersonalizedTravelPackageDTO> toSend = new ArrayList<PersonalizedTravelPackageDTO>();
		toSend.add(personalizedPackage);
		data.setPersonalizedTravelPackagesList(toSend);
		if(result==true)
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "Component added succesfully to your gift list")); 
		else
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "An error occured. Maybe your are trying to add to your gift list a payed travel component, or you are trying to add a new travel component before saving.")); 
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personalized_travel_package.xhtml");
	}
		
	public void addComponent(TravelComponentDTO component) throws IOException
	{
		FacesMessage message = null;
		boolean result = customerHandler.addTravelComponentToPersonalizedTravelPackage(personalizedPackage, component);
		if(result == true){
			message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have added the component to your package, click on the save button to submit your changes!"); 
		}
		else{
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "The component is already in the travel package!");
		}	
		
		List<PersonalizedTravelPackageDTO> toSend = new ArrayList<PersonalizedTravelPackageDTO>();
		toSend.add(personalizedPackage);
		data.setPersonalizedTravelPackagesList(toSend);
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		facesContext.addMessage(null,message);
		
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personalized_travel_package.xhtml");
	}
	
	public void showComponentSearch(TravelComponentDTO component){
		List<TravelComponentDTO> toSend = new ArrayList<TravelComponentDTO>();
		toSend.add(component);
		data.setTravelComponentsList(toSend);
        Map<String,Object> options = new HashMap<String, Object>();  
        options.put("resizable", false); 
		RequestContext.getCurrentInstance().openDialog("/misc/dialog_travelcomponent.xhtml",options,null);
	}
	
	public void onClose(){
		FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove("SearchTravelComponents");
	}
	
	public void confirmPackage() throws IOException{
		String result = customerHandler.confirmPersonalizedTravelPackage(personalizedPackage);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);

		if(result.isEmpty()){
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "Travel package succesfully confirmed!")); 
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personal_travel_package.xhtml");
		}
		else{
			List<PersonalizedTravelPackageDTO> toSend = new ArrayList<PersonalizedTravelPackageDTO>();
			toSend.add(personalizedPackage);
			data.setPersonalizedTravelPackagesList(toSend);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "An error occured. The server replied: " + result));
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personalized_travel_package.xhtml");
		}
	}
	
	public void saveChanges() throws IOException{	
		FacesMessage message = null;
		if(departureDate != null){
			personalizedPackage.setDepartureDate(new Date(departureDate.getTime()));
			if(returnDate != null){
				personalizedPackage.setReturnDate(new Date(returnDate.getTime() + 86340000));
				
				String result=customerHandler.updatePersonalizedTravelPackage(personalizedPackage);

				if(result.isEmpty()){
					message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "Your package has been succesfully updated!"); 
				}
				else{
					message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Something went wrong. Server replied: " + result);	
				}
			}				
			else{		
				message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "You must specify the return date");
			}
		}			
		else{
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "You must specify the departure date");
		}
		List<PersonalizedTravelPackageDTO> toSend = new ArrayList<PersonalizedTravelPackageDTO>();
		toSend.add(personalizedPackage);
		data.setPersonalizedTravelPackagesList(toSend);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		facesContext.addMessage(null,message); 
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personalized_travel_package.xhtml");
	}
	
	public void joinPackage() throws IOException{
		if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("CUSTOMER")){//the joiner is a customer
			String result = customerHandler.joinPersonalizedTravelPackage(finder.findUser(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser()), personalizedPackage);
			FacesContext facesContext = FacesContext.getCurrentInstance();
			Flash flash = facesContext.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			flash.setRedirect(true);
			if(result.isEmpty()){
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have succesfully joined the travel package!"));
				FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personal_travel_package.xhtml");
			}
			else{
				List<PersonalizedTravelPackageDTO> toSend = new ArrayList<PersonalizedTravelPackageDTO>();
				toSend.add(personalizedPackage);
				data.setPersonalizedTravelPackagesList(toSend);
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Something went wrong. Server replies: " + result)); 
				FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/join_package.xhtml");
			}				
		}
		else{//the joiner is not a customer
	        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Registration required", "You must register first. Once logged in, reopen this page with the link given by your friend to join!");  
	        RequestContext.getCurrentInstance().showMessageInDialog(message);
		}
	}	
}
