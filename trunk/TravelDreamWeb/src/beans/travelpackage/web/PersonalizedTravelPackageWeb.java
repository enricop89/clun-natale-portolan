package beans.travelpackage.web;


import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

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

import beans.customerhandler.CustomerHandlerInterface;
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
	private CustomerHandlerInterface customerhandler;
	
	private PersonalizedTravelPackageDTO personalizedPackage;
	private java.util.Date departureDate;
	private java.util.Date returnDate;
	
	@PostConstruct
	public void init(){	
		personalizedPackage = data.getPersonalizedTravelPackagesList().get(0);
		departureDate = new java.util.Date (personalizedPackage.getDepartureDate().getTime());
		returnDate = new java.util.Date (personalizedPackage.getReturnDate().getTime());		
	}
	
	//------------------------
	// SETTERS AND GETTERS
	
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
	
	public boolean checkStatus(Components_HelperDTO helper){
			if(helper.getTravelElement()!=null)
				return true;
		
		return false;
	}
	
	public boolean isOwner(){
		if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("CUSTOMER"))
			if(personalizedPackage.getOwner().getEmail().equals(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser()))
				return true; //the remote user is also the owner of the travel package
				
		return false;  
	}
		
	public void deleteComponent(Components_HelperDTO component) throws IOException{
		boolean result = customerhandler.removeTravelComponentFromPersonalizedTravelPackage(personalizedPackage, component);
		if(result==true){
			FacesContext facesContext = FacesContext.getCurrentInstance();
			Flash flash = facesContext.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			flash.setRedirect(true);
			List<PersonalizedTravelPackageDTO> toSend = new ArrayList<PersonalizedTravelPackageDTO>();
			toSend.add(personalizedPackage);
			data.setPersonalizedTravelPackagesList(toSend);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have deleted the component from your package, click on the save button to submit your changes!")); 
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/personalizedtravelpackage.xhtml"); //delete a TravelComponent
		}
		else
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "You cannot delete a payed component")); 
	}
	
	public void showComponent(Components_HelperDTO component){
		//select package and go to personal_package_home
		if(component.getTravelElement()==null){ //the component is not confirmed
			List<TravelComponentDTO> toSend = new ArrayList<TravelComponentDTO>();
			toSend.add(component.getTravelComponent());
			data.setTravelComponentsList(toSend);
			RequestContext.getCurrentInstance().openDialog("/index.xhtml"); // TODO: waiting for TravelComponent page	
		}
		else 
			if(component.getTravelElement()!=null){	//The component is confirmed
				List<TravelComponentDTO> toSend = new ArrayList<TravelComponentDTO>();
				toSend.add(component.getPersistence());
				data.setTravelComponentsList(toSend);
				RequestContext.getCurrentInstance().openDialog("/index.xhtml"); // TODO: waiting for TravelComponent page	
			}		
	}
	
	public void addToGiftList(Components_HelperDTO component){
		boolean result = customerhandler.addTravelComponentToGiftList(finder.findUser(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser()), component, personalizedPackage);
		if(result==true){
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "Component added succesfully to your gift list")); 
		}
		else
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "An error occured. Maybe your are trying to add a payed travel component")); 		
	}
	
	public void onTravelComponentChosen(CloseEvent event) throws IOException
	{
		try{ // must use try catch, getters in Data_Exchange flushes lists by design! calling it twice is a logical error
			TravelComponentDTO travelComponent = data.getTravelComponentsList().get(0);
			customerhandler.addTravelComponentToPersonalizedTravelPackage(personalizedPackage, travelComponent);
			
			List<PersonalizedTravelPackageDTO> toSend = new ArrayList<PersonalizedTravelPackageDTO>();
			toSend.add(personalizedPackage);
			data.setPersonalizedTravelPackagesList(toSend);
			FacesContext facesContext = FacesContext.getCurrentInstance();
			Flash flash = facesContext.getExternalContext().getFlash();
			flash.setKeepMessages(true);
			flash.setRedirect(true);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have added the component to your package, click on the save button to submit your changes!")); 
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personal_travel_package.xhtml");
		}
		catch (java.lang.IndexOutOfBoundsException e){/* does nothing */}
	}
	
	public void saveChanges() throws IOException{	
		if(departureDate != null){
			personalizedPackage.setDepartureDate(new Date(departureDate.getTime()));
			if(returnDate != null){
				personalizedPackage.setReturnDate(new Date(returnDate.getTime()));
				
				boolean result=customerhandler.updatePersonalizedTravelPackage(personalizedPackage);

				if(result==true){
					List<PersonalizedTravelPackageDTO> toSend = new ArrayList<PersonalizedTravelPackageDTO>();
					toSend.add(personalizedPackage);
					data.setPersonalizedTravelPackagesList(toSend);
					FacesContext facesContext = FacesContext.getCurrentInstance();
					Flash flash = facesContext.getExternalContext().getFlash();
					flash.setKeepMessages(true);
					flash.setRedirect(true);
					facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "Your package has been succesfully updated!")); 
					FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personal_travel_package.xhtml");
				}
				else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Something went wrong. Maybe you are trying to save an unconsistent travel package"));	
				}
			}				
			else{		
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "You must specify the return date"));
			}
		}			
		else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "You must specify the departure date"));
		}
	}
	
	public void joinPackage() throws IOException{

	}	
}
