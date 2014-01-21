package beans.travelcomponent;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import beans.travelpackage.PersonalizedTravelPackageHandler;
import beans.travelpackage.PredefinedTravelPackageHandler;
import beans.utils.Search;
import beans.utils.SendEmail;
import entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.sql.Timestamp;

/**
 * Session Bean implementation class TravelComponentHandler
 */
@Stateless
@LocalBean
public class TravelComponentHandler{
	@PersistenceContext
    private EntityManager entityManager;
	@EJB
	private Search search;
	@EJB
	private PredefinedTravelPackageHandler predefined_handler;
	@EJB
	private PersonalizedTravelPackageHandler personalized_handler;
	
	private void addNewTravelElement(TravelElement travelElement){
		entityManager.persist(travelElement);
	}
	private void deleteTravelElement(TravelElement travelElement){
		entityManager.remove(travelElement);
	}
	
	@RolesAllowed({"CUSTOMER"})
	public TravelElement payTravelComponent(TravelComponent travelComponent, User owner){
		if(travelComponent.getTravelElements().isEmpty())
			return null;
		
		TravelElement travelElement = travelComponent.getTravelElements().get(0);
		travelElement.setOwner(owner);
		travelElement.setConfirmationDateTime(new Timestamp(new Date().getTime()));
		travelElement.setTravelComponent(null);
		travelComponent.getTravelElements().remove(travelElement);
		entityManager.merge(travelElement);
		//checks if the travelComponent becomes empty, if so, it removes it from all references and deletes it from the system
		if(travelComponent.getTravelElements().isEmpty())
			deleteTravelComponent(travelComponent);
		else
			entityManager.merge(travelComponent);
		return travelElement;
	}
	
	@RolesAllowed({"EMPLOYEE"})
	public boolean addNewTravelComponent(TravelComponent travelComponent, int availability){
		if(availability <= 0)
			return false;
		if(travelComponent.getType() == ComponentType.FLIGHT)
			if(travelComponent.getFlightDepartureDateTime().compareTo(travelComponent.getFlightDepartureDateTime()) > 0)
				return false; //inconsistent travelComponent!
		//generates automatically the associated TravelElements
		List<TravelElement> travelElementsList = new ArrayList<TravelElement>();
		for(int i = 0; i < availability; i++){
			TravelElement travelElement = new TravelElement();
			travelElement.setConfirmationDateTime(null);
			travelElement.setOwner(null);
			travelElement.setTravelComponent(travelComponent);
			travelElementsList.add(travelElement);
			addNewTravelElement(travelElement);
		}
		travelComponent.setTravelElements(travelElementsList);
		entityManager.persist(travelComponent);
		return true;
	}
	
	@RolesAllowed({"EMPLOYEE"})
	public boolean updateTravelComponent(TravelComponent travelComponent, int availability){
		if(availability <= 0)
			return false;
		List<TravelElement> travelElements = travelComponent.getTravelElements();
		int delta = travelComponent.getTravelElements().size() - availability;
		if(delta < 0)
			for(int i = 0; i < -(delta); i++){
				TravelElement te = new TravelElement();
				te.setConfirmationDateTime(null);
				te.setOwner(null);
				te.setTravelComponent(travelComponent);
				travelElements.add(te);
				addNewTravelElement(te);
			}
		else if(delta > 0)
			for(int i = 0; i < delta; i++){
				entityManager.remove(travelElements.get(i));
				travelElements.remove(i);
			}
		travelComponent.setTravelElements(travelElements);
		// check if the update affects some predefined travel package
		List<PredefinedTravelPackage> predTPs = search.findAllPredefinedTravelPackages();
		for(int i = 0; i < predTPs.size(); i++){
			for(int j = 0; j < predTPs.get(i).getTravelComponents().size(); j++){
				if(predTPs.get(i).getTravelComponents().get(j).getId() == travelComponent.getId()){
					predTPs.get(i).getTravelComponents().remove(j);
					predTPs.get(i).getTravelComponents().add(j, travelComponent);
					if(!predefined_handler.updatePredefinedTravelPackage(predTPs.get(i))){
						predTPs.get(i).getTravelComponents().remove(travelComponent); // the TravelComponent update violates consistency, so is deleted from the TravelPackage
						if(predTPs.get(i).getTravelComponents().isEmpty()){ // the deletion causes the package to be empty
							predefined_handler.deletePredefinedTravelPackage(predTPs.get(i)); // the package is then deleted
						}
						else
							predefined_handler.updatePredefinedTravelPackage(predTPs.get(i));
					}
				}					
			}
		}
		// check if the update affects some personalized travel package
		List<PersonalizedTravelPackage> persTPs = search.findAllPersonalizedTravelPackages();
		for(int i = 0; i < persTPs.size(); i++){
			for(int j = 0; j < persTPs.get(i).getTravelComponents().size(); j++){
				if(persTPs.get(i).getTravelComponents().get(j).getTravelComponent().getId() == travelComponent.getId()){
					if(persTPs.get(i).getTravelComponents().get(j).getTravelElement() != null){
						persTPs.get(i).getTravelComponents().get(j).setTravelComponent(null);
						personalized_handler.updatePersonalizedTravelPackage(persTPs.get(i));
					}
					else{
						String message = new String();						
						persTPs.get(i).getTravelComponents().get(j).setTravelComponent(travelComponent);
						if(!personalized_handler.updatePersonalizedTravelPackage(persTPs.get(i))){
							persTPs.get(i).getTravelComponents().remove(j); // the TravelComponent update violates consistency, so is deleted from the TravelPackage
							if(persTPs.get(i).getTravelComponents().isEmpty()){ // the deletion causes the package to be empty
								message = "We are sorry to inform you that our staff has been forced to update a Travel Component, and this affect one of your Travel Package: "
										+ persTPs.get(i).getName() + ".\n"
										+ "Since the Travel Package had only this Travel Component, it has been removed, sorry for the inconvenience.";
								personalized_handler.deletePersonalizedTravelPackage(persTPs.get(i)); // the package is then deleted
							}
							else{
								message = "We are sorry to inform you that our staff has been forced to update a Travel Component, and this affect one of your Travel Package: "
										+ persTPs.get(i).getName() + ".\n"
										+ "The Travel Component has been removed, please login and select another one to eventually substitute it!";
								personalized_handler.updatePersonalizedTravelPackage(persTPs.get(i)); // the package is updated with the deletion
							}
						}
						else{
							message = "We are sorry to inform you that our staff has been forced to update a Travel Component, and this affect one of your Travel Package: "
									+ persTPs.get(i).getName() + ".\n"
									+ "The Travel Component has been updated, please login and check if the modification still satisfy you, otherwise you are invited to substitute it!";
						}
						// an email is sent to the customer notifying the event
						SendEmail.send(persTPs.get(i).getOwner().getEmail(), "TravelDream notification", message);
					}
				}	
			}
		}				
		entityManager.merge(travelComponent);		
		return true;
	}
	
	public void deleteTravelComponent(TravelComponent travelComponent){
		for(int i = 0; i < travelComponent.getTravelElements().size(); i++)
			deleteTravelElement(travelComponent.getTravelElements().get(i));
		// check if the deletion affects some predefined travel package
		List<PredefinedTravelPackage> predTPs = search.findAllPredefinedTravelPackages();
		for(int i = 0; i < predTPs.size(); i++){
			if(predTPs.get(i).getTravelComponents().contains(travelComponent)){
				predTPs.get(i).getTravelComponents().remove(travelComponent);
				if(predTPs.get(i).getTravelComponents().isEmpty()){ // the deletion causes the package to be empty
					predefined_handler.deletePredefinedTravelPackage(predTPs.get(i)); // the package is then deleted
				}
				else
					predefined_handler.updatePredefinedTravelPackage(predTPs.get(i));
			}
		}
		// check if the deletion affects some personalized travel package
		List<PersonalizedTravelPackage> persTPs = search.findAllPersonalizedTravelPackages();
		for(int i = 0; i < persTPs.size(); i++){
			for(int j = 0; j < persTPs.get(i).getTravelComponents().size(); j++){
				if(persTPs.get(i).getTravelComponents().get(j).getTravelComponent().getId() == travelComponent.getId()){
					if(persTPs.get(i).getTravelComponents().get(j).getTravelElement() != null){
						persTPs.get(i).getTravelComponents().get(j).setTravelComponent(null);
						personalized_handler.updatePersonalizedTravelPackage(persTPs.get(i));
					}	
					else{
						String message = new String();
						persTPs.get(i).getTravelComponents().remove(j);
						if(persTPs.get(i).getTravelComponents().isEmpty()){ // the deletion causes the package to be empty
							personalized_handler.deletePersonalizedTravelPackage(persTPs.get(i)); // the package is then deleted
							message = "We are sorry to inform you that our staff has been forced to delete a Travel Component, and this affect one of your Travel Package: "
									+ persTPs.get(i).getName() + ".\n"
									+ "Since the Travel Package had only this Travel Component, it has been removed, sorry for the inconvenience.";
						}
						else{
							personalized_handler.updatePersonalizedTravelPackage(persTPs.get(i)); // the package is updated accordingly
							message = "We are sorry to inform you that our staff has been forced to delete a Travel Component, and this affect one of your Travel Package: "
									+ persTPs.get(i).getName() + ".\n"
									+ "The Travel Component has been removed, please login and select another one to eventually substitute it!";
						}
						// an email is sent to the customer notifying the event
						SendEmail.send(persTPs.get(i).getOwner().getEmail(), "TravelDream notification", message);
					}
				}
			}
		}
		entityManager.remove(travelComponent);
	}
}
