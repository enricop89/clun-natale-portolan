
package beans.customerhandler.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import beans.accountmanagement.UserDTO;
import beans.customerhandler.CustomerHandlerInterface;
import beans.customerhandler.GiftElements_HelperDTO;
import beans.customerhandler.GiftListDTO;
import beans.travelpackage.PersonalizedTravelPackageDTO;
import beans.utils.SearchDTOInterface;
//TODO:inserire visualizza travel component nel'xml
import beans.utils.web.Data_Exchange;

@ManagedBean(name="UserPersonalPage")
@RequestScoped
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
	
	public String deletebutton(GiftElements_HelperDTO helper){
		
		customerhandler.removeTravelComponentFromGiftList(helper);
		
		return "gift_list?faces-redirect=true";
	}
	public String visualizeTP(PersonalizedTravelPackageDTO persTP){
		List<PersonalizedTravelPackageDTO> helper= new ArrayList<PersonalizedTravelPackageDTO>();
		helper.add(persTP);
		data.setPersonalizedTravelPackagesList(helper);
		return "link alla pagina che farò";
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

