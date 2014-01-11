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
	private Search search;

	@RolesAllowed({"CUSTOMER"})
	public void addNewTravelComponentToPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage, TravelComponent travelComponent){
		List<Components_Helper> components = personalizedTravelPackage.getTravelComponents();
		Components_Helper component = new Components_Helper();
		component.setPersonalizedTravelPackage(personalizedTravelPackage);
		component.setTravelElement(null);
		component.setTravelComponent(travelComponent);
		components.add(component);
		personalizedTravelPackage.setTravelComponents(components);
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

}
