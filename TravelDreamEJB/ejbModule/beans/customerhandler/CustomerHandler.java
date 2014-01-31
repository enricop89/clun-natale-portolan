package beans.customerhandler;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import entities.Components_Helper;
import entities.GiftList;
import entities.PersonalizedTravelPackage;
import entities.PredefinedTravelPackage;
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
	@PersistenceContext
    private EntityManager entityManager;

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
	public String removeTravelComponentFromPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage, Components_HelperDTO travelComponent){
		if(personalizedTravelPackage.getTravelComponents().size() == 1)
			return "cannot delete the last component, would make the package empty";
		
		if(travelComponent.getTravelElement() != null)
			return "cannot delete a payed component";
		
		List<Components_HelperDTO> components = personalizedTravelPackage.getTravelComponents();
		boolean result = components.remove(travelComponent);
		if(result == true)
			personalizedTravelPackage.setTravelComponents(components);		
		return "";
	}
	
	@Override
	@RolesAllowed({"CUSTOMER"})
	public String confirmPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage){
		PersonalizedTravelPackage toConfirm = search.findPersonalizedTravelPackage(personalizedTravelPackage);
		// check if the original one and the DTO are different, if so call an update first
		String result = "";
		if(!toConfirm.getName().equals(personalizedTravelPackage.getName()) || !toConfirm.getDepartureDate().equals(personalizedTravelPackage.getDepartureDate()) || !toConfirm.getReturnDate().equals(personalizedTravelPackage.getReturnDate()))
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
		
		if(result.isEmpty()){
			result = handler.confirmPersonalizedTravelPackage(search.findPersonalizedTravelPackage(personalizedTravelPackage));
			if(result.isEmpty()){ // Removes all the eventual corresponding entry in the gift list	
				GiftList giftList = search.findGiftList(search.findPersonalizedTravelPackage(personalizedTravelPackage).getOwner());
				for(int j = 0; j < giftList.getGiftElements().size(); j++){
					if(giftList.getGiftElements().get(j).getPersonalizedTravelPackage().getId() == personalizedTravelPackage.getId()){
						gift_handler.removeTravelComponentFromGiftList(giftList.getGiftElements().get(j));
					}
				}
				return "";
			}
			else
				return result;
		}
			
		else
			return result;
	}
	
	@Override
	@RolesAllowed({"CUSTOMER"})
	public String joinPersonalizedTravelPackage(UserDTO user, PersonalizedTravelPackageDTO personalizedTravelPackage){
		PersonalizedTravelPackage newPackage = handler.copyPersonalizedTravelPackage(search.findPersonalizedTravelPackage(personalizedTravelPackage), search.findUser(user));
		if(newPackage != null){
			return handler.confirmPersonalizedTravelPackage(newPackage);
				
		}
		else
			return "you already have joined this package or it contains components which are not available anymore.";
	}
	
	@Override
	@RolesAllowed({"CUSTOMER"})
	public String updatePersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage){
		PersonalizedTravelPackage result = new PersonalizedTravelPackage();
		result.setId(search.findPersonalizedTravelPackage(personalizedTravelPackage).getId());
		result.setAll(personalizedTravelPackage, search);
		return handler.updatePersonalizedTravelPackage(result);
	}

	@Override
	@RolesAllowed({"CUSTOMER"})
	public boolean deletePersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage){
		return handler.deletePersonalizedTravelPackage(search.findPersonalizedTravelPackage(personalizedTravelPackage));
	}
	
	@Override
	@RolesAllowed({"CUSTOMER"})
	public String addNewPersonalizedTravelPackage(UserDTO user, PredefinedTravelPackageDTO predefinedTravelPackage){
		PredefinedTravelPackage newPackage = new PredefinedTravelPackage();		
		newPackage.setAll(predefinedTravelPackage, search);
		return predefined_handler.copyPredefinedTravelPackage(newPackage, search.findUser(user));
	}
	
	@Override
	@RolesAllowed({"CUSTOMER"})
	public boolean addTravelComponentToGiftList(UserDTO user, Components_HelperDTO travelComponent, PersonalizedTravelPackageDTO personalizedTravelPackage){
		Components_Helper newComponent = search.findComponents_Helper(travelComponent);
		if(newComponent != null)
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
