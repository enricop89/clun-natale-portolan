package beans.travelpackage.web;


import java.io.IOException;
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

import beans.customerhandler.CustomerHandlerInterface;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.Components_HelperDTO;
import beans.travelpackage.PersonalizedTravelPackageDTO;
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
	private CustomerHandlerInterface customerhandler;
	private PersonalizedTravelPackageDTO persTP;
	
	@PostConstruct
	public void init(){	
		persTP=data.getPersonalizedTravelPackagesList().get(0);
	}
	
	public boolean checkStatus(Components_HelperDTO helper){
			if(helper.getTravelElement()!=null)
				return true;
		
		return false;
	}
	public void deletePackage(Components_HelperDTO helper) throws IOException{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		boolean result=customerhandler.removeTravelComponentFromPersonalizedTravelPackage(persTP, helper);
				if(result==true){
					facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have succesfully deleted your component from your own travel package!")); 
					FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/personalizedtravelpackage.xhtml"); //delete a TravelComponent
			}
				else
					facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "You cannot delete a payed component")); 
	}
	public void showComponent(Components_HelperDTO helper) throws IOException{
		//select package and go to personal_package_home
		List<TravelComponentDTO> toSend = new ArrayList<TravelComponentDTO>();
		toSend.add(helper.getTravelComponent());
		data.setTravelComponentsList(toSend);
		RequestContext.getCurrentInstance().openDialog("/index.xhtml"); // TODO: waiting for TravelComponent page	
	}
	
	public String save(){
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		boolean result=customerhandler.updatePersonalizedTravelPackage(persTP);
		if(result==true){
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "Your package has been succesfully updated!")); 
		return "/misc/personalizedtravelpackage.xhtml?faces-redirect=true";
		}
		else{
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Something went wrong"));
		return "/misc/personalizedtravelpackage.xhtml?faces-redirect=true";
		}
	}
		
		public boolean checkRole() throws IOException{
			
			if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("CUSTOMER"))
				if(persTP.getOwner().getEmail().equals(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser()))
					return true; //the remote user is also the owner of the travel package
				
				return false;  
	}
		public boolean checkIfRegistered() throws IOException{
				if(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser()==null)
					return true; //the remote user is not registered
				
				return false;
		}
	
	public String joinComponent(Components_HelperDTO helper)	{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("CUSTOMER"))
			if(persTP.getOwner().getEmail().equals(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser())==false){
				boolean result=customerhandler.joinPersonalizedTravelPackage(persTP.getOwner(), persTP);	//faccio join
					if(result==true){
						facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have successfully joined the package!"));	
						return "/customer/personal_travel_package.xhtml?faces-redirect=true";
					}	
					else {
						facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Error", "Something went worng!"));	
						return "/misc/personalizedtravelpackage.xhtml?faces-redirect=true";	
						
					}

			}	//join for a registered User 
		
		if(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser()==null)
		{
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Error", "You must be registered to be able to join a package"));	
			return "registration.xhtml?faces-redirect=true";
		}
				
		else {	//for any reason the if condition is not respected
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Error", "Something went wrong!"));	
			return "/misc/personalizedtravelpackage.xhtml?faces-redirect=true";	
			
		}
		
	}
		
		
	public PersonalizedTravelPackageDTO getPersTP() {
		return persTP;
	}
	public void setPersTP(PersonalizedTravelPackageDTO persTP) {
		this.persTP = persTP;
	}
	
	
	
}
