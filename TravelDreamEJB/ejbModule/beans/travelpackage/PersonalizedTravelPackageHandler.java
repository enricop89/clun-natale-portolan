package beans.travelpackage;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import beans.travelcomponent.TravelComponentHandler;
import entities.*;

/**
 * Session Bean implementation class PersonalizedTravelPackageHandler
 */
@Stateless
@LocalBean
public class PersonalizedTravelPackageHandler {
	@PersistenceContext
    private EntityManager entityManager;
	
	public boolean addNewPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		
		//CONTROLLO CONSISTENZA
		
		entityManager.persist(personalizedTravelPackage);
		return true;
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean updatePersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		boolean result = false;
		for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
			if(personalizedTravelPackage.getTravelComponents().get(i).getTravelElement() != null)
				result = true;
		if(result){	// if it is a confirmed package it does not procede!	
			
			//CONTROLLO CONSISTENZA			
			
			entityManager.merge(personalizedTravelPackage);
		}
		return result;
		
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean deletePersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		boolean result = false;
		for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
			if(personalizedTravelPackage.getTravelComponents().get(i).getTravelElement() != null)
				result = true;
		if(result){ // if it is a confirmed package it does not procede!	
			for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
				entityManager.remove(personalizedTravelPackage.getTravelComponents().get(i));
			entityManager.remove(personalizedTravelPackage);
		}
		return result;
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean confirmPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage){
		boolean result = false;
		for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
			if(personalizedTravelPackage.getTravelComponents().get(i).getTravelElement() == null){
				TravelComponentHandler handler = new TravelComponentHandler();
				personalizedTravelPackage.getTravelComponents().get(i).setTravelElement(handler.payTravelComponent(personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent(), personalizedTravelPackage.getOwner()));
				personalizedTravelPackage.getTravelComponents().get(i).setPersistence(personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent());
				result = true; //the package is not confirmed yet, at least one component is not payed
			}
		return result;
	}
	
	@RolesAllowed({"CUSTOMER"})
	public boolean copyPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage, User owner){
		PersonalizedTravelPackage newPersonalizedTravelPackage = new PersonalizedTravelPackage();
		newPersonalizedTravelPackage.setName(personalizedTravelPackage.getName());
		newPersonalizedTravelPackage.setOwner(owner);
		List<Components_Helper> components = new ArrayList<Components_Helper>();
		for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
			if(personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent() != null)
			{
				Components_Helper component = new Components_Helper();
				component.setPersonalizedTravelPackage(newPersonalizedTravelPackage);
				component.setTravelElement(null);
				component.setTravelComponent( personalizedTravelPackage.getTravelComponents().get(i).getTravelComponent());
				components.add(component);
			}
			else
				return false; //the travel component associated has been deleted by an employee, the user cannot copy the package!
		newPersonalizedTravelPackage.setTravelComponents(components);
		for(int i = 0; i < components.size(); i++)
			entityManager.persist(components.get(i));
		entityManager.persist(newPersonalizedTravelPackage);
		return true;
		
	}
	
}
