package beans.customerhandler;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import beans.accountmanagement.UserDTO;
import beans.travelpackage.PersonalizedTravelPackageHandler;
import beans.travelpackage.PredefinedTravelPackageHandler;
import beans.utils.Search;
import entities.*;

/**
 * Session Bean implementation class CustomerHandler
 */
@Stateless
@LocalBean
public class CustomerHandler {
	private PersonalizedTravelPackageHandler handler;
	private PredefinedTravelPackageHandler predefined_handler;
	private GiftListHandler gift_handler;
	private Search search;

	@RolesAllowed({"CUSTOMER"})
	public boolean addNewTravelComponentToPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage, TravelComponent travelComponent){
		//control first if it is in the personalizedTravelPackage already
		for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
			if(personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent() == travelComponent)
				return false;
		List<Components_Helper> components = personalizedTravelPackage.getTravelComponents();
		Components_Helper component = new Components_Helper();
		component.setPersonalizedTravelPackage(personalizedTravelPackage);
		component.setTravelElement(null);
		component.setTravelComponent(travelComponent);
		components.add(component);
		personalizedTravelPackage.setTravelComponents(components);
		return true;
	}
	
	@RolesAllowed({"CUSTOMER"})
	public void removeTravelComponentFromPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage, Components_Helper travelComponent){
		List<Components_Helper> components = personalizedTravelPackage.getTravelComponents();
		components.remove(travelComponent);
		personalizedTravelPackage.setTravelComponents(components);
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean confirmPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		return handler.confirmPersonalizedTravelPackage(personalizedTravelPackage);
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean joinPersonalizedTravelPackage(UserDTO user, PersonalizedTravelPackage personalizedTravelPackage){
		return handler.copyPersonalizedTravelPackage(personalizedTravelPackage, search.findUser(user));
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean addNewPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		return handler.addNewPersonalizedTravelPackage(personalizedTravelPackage);
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean updatePersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		return handler.updatePersonalizedTravelPackage(personalizedTravelPackage);
	}

	@RolesAllowed({"CUSTOMER"})
	public boolean deletePersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		return handler.deletePersonalizedTravelPackage(personalizedTravelPackage);
	}
	
	@RolesAllowed({"CUSTOMER"})
	public void addNewPersonalizedTravelPackage(UserDTO user, PredefinedTravelPackage predefinedTravelPackage){
		predefined_handler.copyPredefinedTravelPackage(predefinedTravelPackage, search.findUser(user));
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean addTravelComponentToGiftList(UserDTO user, Components_Helper travelComponent, PersonalizedTravelPackage personalizedTravelPackage){
		return gift_handler.addTravelComponentToGiftList(search.findUser(user), search.findGiftList(user), travelComponent, personalizedTravelPackage);		
	}
	
	@RolesAllowed({"CUSTOMER"})
	public void removeTravelComponentFromGiftList(GiftElements_Helper giftListElement){
		gift_handler.removeTravelComponentFromGiftList(giftListElement);
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean payTravelComponent(UserDTO owner, UserDTO payer, GiftElements_Helper giftListElement){
		return gift_handler.payTravelComponent(search.findUser(owner), search.findUser(payer), giftListElement);
	}
}
