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
import beans.utils.SearchDTOInterface;
import beans.utils.web.Data_Exchange;

@ManagedBean(name="OtherCustomerWeb")
@ViewScoped
public class OtherCustomerWeb {
	@EJB
	private SearchDTOInterface finder;
	@EJB
	private CustomerHandlerInterface customerHandler;
	
	private GiftListDTO giftList;
	private UserDTO user;
	
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
		user = data.getUsersList().get(0);
		giftList= finder.findGiftList(user);
	}
	
	//------------------------
	// SETTERS AND GETTERS
	public GiftListDTO getGiftList() {
		return giftList;
	}
	public void setGiftList(GiftListDTO giftList) {
		this.giftList = giftList;
	}
	public UserDTO getUser() {
		return user;
	}
	public void setUser(UserDTO user) {
		this.user = user;
	}
	
	//-----------------------
	public void showComponent(GiftElements_HelperDTO giftListElement) throws IOException{
		//select package and go to personal_package_home
		List<TravelComponentDTO> toSend = new ArrayList<TravelComponentDTO>();
		toSend.add(giftListElement.getTravelComponent().getTravelComponent());
		data.setTravelComponentsList(toSend);
		RequestContext.getCurrentInstance().openDialog("/index.xhtml"); // TODO: waiting for TravelComponent page	
	}
	public void payComponent(GiftElements_HelperDTO giftListElement) throws IOException{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		boolean result = customerHandler.payTravelComponent(user, finder.findUser(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser()), giftListElement);
			if (result==true){
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have succesfully payed this component!")); 
				List<UserDTO> toSend = new ArrayList<UserDTO>();
				toSend.add(user);
				data.setUsersList(toSend);
				FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/other_customer/gift_list.xhtml");
			}
			else
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Some errors occurred when performing this action")); 			
	}
}
