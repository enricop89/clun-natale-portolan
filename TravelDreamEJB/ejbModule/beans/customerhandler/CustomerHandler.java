package beans.customerhandler;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

import beans.accountmanagement.UserDTO;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.Components_HelperDTO;
import beans.travelpackage.PersonalizedTravelPackageDTO;
import beans.travelpackage.PersonalizedTravelPackageHandler;
import beans.travelpackage.PredefinedTravelPackageDTO;
import beans.travelpackage.PredefinedTravelPackageHandler;
import beans.utils.Search;
import entities.*;

/**
 * Session Bean implementation class CustomerHandler
 */
@Stateless
public class CustomerHandler implements CustomerHandlerInterface{
	private PersonalizedTravelPackageHandler handler;
	private PredefinedTravelPackageHandler predefined_handler;
	private GiftListHandler gift_handler;
	private Search search;

	@RolesAllowed({"CUSTOMER"})
	public boolean addTravelComponentToPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage, TravelComponentDTO travelComponent){
		//control first if it is in the personalizedTravelPackage already
		for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
			if(personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent() == travelComponent)
				return false;
		List<Components_HelperDTO> components = personalizedTravelPackage.getTravelComponents();
		Components_HelperDTO component = new Components_HelperDTO();
		component.setPersonalizedTravelPackage(personalizedTravelPackage);
		component.setTravelElement(null);
		component.setTravelComponent(travelComponent);
		components.add(component);
		personalizedTravelPackage.setTravelComponents(components);
		return true;
	}
	
	@RolesAllowed({"CUSTOMER"})
	public void removeTravelComponentFromPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage, Components_HelperDTO travelComponent){
		List<Components_HelperDTO> components = personalizedTravelPackage.getTravelComponents();
		components.remove(travelComponent);
		personalizedTravelPackage.setTravelComponents(components);
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean confirmPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage){
		return handler.confirmPersonalizedTravelPackage(search.findPersonalizedTravelPackage(personalizedTravelPackage));
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean joinPersonalizedTravelPackage(UserDTO user, PersonalizedTravelPackageDTO personalizedTravelPackage){
		return handler.copyPersonalizedTravelPackage(search.findPersonalizedTravelPackage(personalizedTravelPackage), search.findUser(user));
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean addNewPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage){
		return handler.addNewPersonalizedTravelPackage(new PersonalizedTravelPackage(personalizedTravelPackage));
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean updatePersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage){
		return handler.updatePersonalizedTravelPackage(search.findPersonalizedTravelPackage(personalizedTravelPackage));
	}

	@RolesAllowed({"CUSTOMER"})
	public boolean deletePersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage){
		return handler.deletePersonalizedTravelPackage(search.findPersonalizedTravelPackage(personalizedTravelPackage));
	}
	
	@RolesAllowed({"CUSTOMER"})
	public void addNewPersonalizedTravelPackage(UserDTO user, PredefinedTravelPackageDTO predefinedTravelPackage){
		predefined_handler.copyPredefinedTravelPackage(search.findPredefinedTravelPackage(predefinedTravelPackage), search.findUser(user));
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean addTravelComponentToGiftList(UserDTO user, Components_HelperDTO travelComponent, PersonalizedTravelPackageDTO personalizedTravelPackage){
		return gift_handler.addTravelComponentToGiftList(search.findUser(user), search.findGiftList(search.findUser(user)), search.findComponents_Helper(travelComponent), search.findPersonalizedTravelPackage(personalizedTravelPackage));		
	}
	
	@RolesAllowed({"CUSTOMER"})
	public void removeTravelComponentFromGiftList(GiftElements_HelperDTO giftListElement){
		gift_handler.removeTravelComponentFromGiftList(search.findGiftElements_Helper(giftListElement));
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean payTravelComponent(UserDTO owner, UserDTO payer, GiftElements_HelperDTO giftListElement){
		return gift_handler.payTravelComponent(search.findUser(owner), search.findUser(payer), search.findGiftElements_Helper(giftListElement));
	}
}
