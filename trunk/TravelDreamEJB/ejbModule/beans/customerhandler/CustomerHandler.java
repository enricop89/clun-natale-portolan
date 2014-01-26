package beans.customerhandler;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;

import entities.Components_Helper;
import entities.PersonalizedTravelPackage;
import beans.accountmanagement.UserDTO;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.Components_HelperDTO;
import beans.travelpackage.PersonalizedTravelPackageDTO;
import beans.travelpackage.PersonalizedTravelPackageHandler;
import beans.travelpackage.PredefinedTravelPackageDTO;
import beans.travelpackage.PredefinedTravelPackageHandler;
import beans.utils.Search;

/**
 * Session Bean implementation class CustomerHandler
 */
@Stateless
public class CustomerHandler implements CustomerHandlerInterface{
	@EJB
	private PersonalizedTravelPackageHandler handler;
	@EJB
	private PredefinedTravelPackageHandler predefined_handler;
	@EJB
	private GiftListHandler gift_handler;
	@EJB
	private Search search;

	@Override
	@RolesAllowed({"CUSTOMER"})
	public boolean addTravelComponentToPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage, TravelComponentDTO travelComponent){
		//control first if it is in the personalizedTravelPackage already
		for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
			if(personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent().getId() == travelComponent.getId())
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
	
	@Override
	@RolesAllowed({"CUSTOMER"})
	public boolean removeTravelComponentFromPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage, Components_HelperDTO travelComponent){
		if(travelComponent.getTravelElement() != null)
			return false;
		
		List<Components_HelperDTO> components = personalizedTravelPackage.getTravelComponents();
		boolean result = components.remove(travelComponent);
		if(result == true)
			personalizedTravelPackage.setTravelComponents(components);
		return result;
	}
	
	@Override
	@RolesAllowed({"CUSTOMER"})
	public String confirmPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage){
		PersonalizedTravelPackage toConfirm = search.findPersonalizedTravelPackage(personalizedTravelPackage);
		// check if the original one and the DTO are different, if so call an update first
		String result = "";
		if(!toConfirm.getName().equals(personalizedTravelPackage.getName()) || toConfirm.getDepartureDate().compareTo(personalizedTravelPackage.getDepartureDate()) != 0 || toConfirm.getReturnDate().compareTo(personalizedTravelPackage.getReturnDate()) != 0)
			result = updatePersonalizedTravelPackage(personalizedTravelPackage);
		else if(toConfirm.getTravelComponents().size() != personalizedTravelPackage.getTravelComponents().size())
			result = updatePersonalizedTravelPackage(personalizedTravelPackage);
		else{
			boolean different = false;
			for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
				if(search.findComponents_Helper(personalizedTravelPackage.getTravelComponents().get(i)) == null)
					different = true; // there exists at least one new component
			
			if(different == true)
				result = updatePersonalizedTravelPackage(personalizedTravelPackage);				
		}
		
		if(result.isEmpty())
			return handler.confirmPersonalizedTravelPackage(search.findPersonalizedTravelPackage(personalizedTravelPackage));
			
		else
			return result;
	}
	
	@Override
	@RolesAllowed({"CUSTOMER"})
	public boolean joinPersonalizedTravelPackage(UserDTO user, PersonalizedTravelPackageDTO personalizedTravelPackage){
		return handler.copyPersonalizedTravelPackage(search.findPersonalizedTravelPackage(personalizedTravelPackage), search.findUser(user));
	}
	
	@Override
	@RolesAllowed({"CUSTOMER"})
	public String updatePersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage){
		PersonalizedTravelPackage result = new PersonalizedTravelPackage(personalizedTravelPackage,search);
		return handler.updatePersonalizedTravelPackage(result);
	}

	@Override
	@RolesAllowed({"CUSTOMER"})
	public boolean deletePersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage){
		return handler.deletePersonalizedTravelPackage(search.findPersonalizedTravelPackage(personalizedTravelPackage));
	}
	
	@Override
	@RolesAllowed({"CUSTOMER"})
	public void addNewPersonalizedTravelPackage(UserDTO user, PredefinedTravelPackageDTO predefinedTravelPackage){
		predefined_handler.copyPredefinedTravelPackage(search.findPredefinedTravelPackage(predefinedTravelPackage), search.findUser(user));
	}
	
	@Override
	@RolesAllowed({"CUSTOMER"})
	public boolean addTravelComponentToGiftList(UserDTO user, Components_HelperDTO travelComponent, PersonalizedTravelPackageDTO personalizedTravelPackage){
		Components_Helper newComponent = search.findComponents_Helper(travelComponent);
		if(newComponent == null)
			return gift_handler.addTravelComponentToGiftList(search.findUser(user), search.findGiftList(search.findUser(user)), search.findComponents_Helper(travelComponent), search.findPersonalizedTravelPackage(personalizedTravelPackage));
		else
			return false;
	}
	
	@Override
	@RolesAllowed({"CUSTOMER"})
	public void removeTravelComponentFromGiftList(GiftElements_HelperDTO giftListElement){
		gift_handler.removeTravelComponentFromGiftList(search.findGiftElements_Helper(giftListElement));
	}
	
	@Override
	@RolesAllowed({"CUSTOMER"})
	public boolean payTravelComponent(UserDTO owner, UserDTO payer, GiftElements_HelperDTO giftListElement){
		return gift_handler.payTravelComponent(search.findUser(owner), search.findUser(payer), search.findGiftElements_Helper(giftListElement));
	}
}
