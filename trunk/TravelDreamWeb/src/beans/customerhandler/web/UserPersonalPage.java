
package beans.customerhandler.web;

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

import beans.accountmanagement.UserDTO;
import beans.customerhandler.CustomerHandlerInterface;
import beans.customerhandler.GiftElements_HelperDTO;
import beans.customerhandler.GiftListDTO;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.PersonalizedTravelPackageDTO;
import beans.utils.SearchDTOInterface;
import beans.utils.web.Data_Exchange;

//TODO:inserire visualizza travel component nel'xml

@ManagedBean(name="UserPersonalPage")
@ViewScoped
public class UserPersonalPage {
	@EJB
	private SearchDTOInterface search;
	@EJB
	private CustomerHandlerInterface customerhandler;
	private GiftListDTO giftList;
	private UserDTO user;
	private List<PersonalizedTravelPackageDTO> persTP;
	
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
		user = search.findUser(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
		giftList= search.findGiftList(user);
		persTP=search.findAllPersonalizedTravelPackages(user);
	}
	
	public void deleteGFelement(GiftElements_HelperDTO helper) throws IOException{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		customerhandler.removeTravelComponentFromGiftList(helper);
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have succesfully deleted your component from GiftList!")); 
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/gift_list.xhtml"); //delete a Personalize Travel Package
		
	}
	
	public void deletePTPelement(PersonalizedTravelPackageDTO helper) throws IOException{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		boolean result=customerhandler.deletePersonalizedTravelPackage(helper);
			if (result==true){
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have succesfully deleted your package!")); 
				FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personal_travel_package.xhtml"); //delete a Personalize Travel Package
			}
			else
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "You cannot delete a confirmed package")); 
			
	}
	
	public void showMessage(PersonalizedTravelPackageDTO helper) {  //get identifier
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Identifier", "SUKA");  
        RequestContext.getCurrentInstance().showMessageInDialog(message);  //TODO
    } 
	
	public void showComponentfromGiftList(GiftElements_HelperDTO gift) throws IOException{
		//select package and go to personal_package_home
		List<TravelComponentDTO> toSend = new ArrayList<TravelComponentDTO>();
		toSend.add(gift.getTravelComponent().getTravelComponent());
		data.setTravelComponentsList(toSend);
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml"); // TODO: waiting for TravelComponent page	
	}
	
	public void showPackagefromGiftList(GiftElements_HelperDTO gift) throws IOException{
		//select package and go to personal_package_home
		List<PersonalizedTravelPackageDTO> toSend = new ArrayList<PersonalizedTravelPackageDTO>();
		toSend.add(gift.getPersonalizedTravelPackage());
		data.setPersonalizedTravelPackagesList(toSend);
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml"); // TODO: waiting for TravelPackage page	
	}
	
	public void showPackage(PersonalizedTravelPackageDTO personalizedTravelPackage) throws IOException{
		// open up the predefinedTravelPackage page
		List<PersonalizedTravelPackageDTO> toSend = new ArrayList<PersonalizedTravelPackageDTO>();
		toSend.add(personalizedTravelPackage);
		data.setPersonalizedTravelPackagesList(toSend);
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "misc/personalized_travel_package.xhtml"); // TODO: waiting for TravelPackage page	
	}
	
	
	public String goToNextpage() {
	    return "personal_travel_package";
	}
	public boolean checkStatusGF(GiftListDTO gift){
		for(int i=0;i<gift.getGiftElements().size();i++)
				if(gift.getGiftElements().get(i).getTravelComponent().getTravelElement()==null)
					return false;
		
		return true;
		
	}
	
	public boolean checkStatus(PersonalizedTravelPackageDTO helper){
		for (int i=0;i<helper.getTravelComponents().size();i++)
			if(helper.getTravelComponents().get(i).getTravelElement()==null)
				return false;
		
		return true;
	}
	
	public UserDTO getUser() {
		return user;
	}
	
	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	public GiftListDTO getGiftList() {
		return this.giftList;
	}

	public void setGiftList(GiftListDTO giftList) {
		this.giftList = giftList;
	}  
	public List<PersonalizedTravelPackageDTO> getPersTP() {
		return this.persTP;
	}

	public void setPersTP(List<PersonalizedTravelPackageDTO> persTP) {
		this.persTP = persTP;
	}  
	
}

