package beans.travelcomponent;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
	
	private void addNewTravelElement(TravelElement travelElement){
		entityManager.persist(travelElement);
	}
	private void deleteTravelElement(TravelElement travelElement){
		entityManager.remove(travelElement);
	}
	
	@RolesAllowed({"CUSTOMER"})
	public TravelElement confirmTravelElement(TravelComponent travelComponent, User owner){
		if(travelComponent.getTravelElements().isEmpty())
			return null;
		TravelElement element = travelComponent.getTravelElements().get(0);
		element.setOwner(owner);
		element.setConfirmationDateTime(new Timestamp(new Date().getTime()));
		element.setTravelComponent(null);
		travelComponent.getTravelElements().remove(element);
		entityManager.merge(travelComponent);
		entityManager.merge(element);
		return element;
	}
	
	@RolesAllowed({"EMPLOYEE"})
	public boolean addNewTravelComponent(TravelComponent travelComponent, int availability){
		if(availability <= 0)
			return false;
		//generates automatically the associated TravelElements
		List<TravelElement> travelElements = new ArrayList<TravelElement>();
		for(int i = 0; i < availability; i++){
			TravelElement te = new TravelElement();
			te.setConfirmationDateTime(null);
			te.setOwner(null);
			te.setTravelComponent(travelComponent);
			travelElements.add(te);
			addNewTravelElement(te);
		}
		travelComponent.setTravelElements(travelElements);
		entityManager.persist(travelComponent);
		return true;
	}
	
	@RolesAllowed({"EMPLOYEE"})
	public boolean updateTravelComponent(TravelComponent travelComponent, int availability){
		if(availability <= 0)
			return false;
		List<TravelElement> travelElements = travelComponent.getTravelElements();
		if(travelComponent.getTravelElements().size() < availability)
			for(int i = 0; i < availability - travelComponent.getTravelElements().size(); i++){
				TravelElement te = new TravelElement();
				te.setConfirmationDateTime(null);
				te.setOwner(null);
				te.setTravelComponent(travelComponent);
				travelElements.add(te);
				addNewTravelElement(te);
			}
		else if(travelComponent.getTravelElements().size() > availability)
			for(int i = 0; i < travelComponent.getTravelElements().size() - availability; i++){
				TravelElement te = new TravelElement();
				te.setConfirmationDateTime(null);
				te.setOwner(null);
				te.setTravelComponent(travelComponent);
				travelElements.add(te);
				addNewTravelElement(te);
			}
		travelComponent.setTravelElements(travelElements);
		entityManager.merge(travelComponent);
		return true;
	}
	
	@RolesAllowed({"EMPLOYEE"})
	public void deleteTravelComponent(TravelComponent travelComponent){
		for(int i = 0; i < travelComponent.getTravelElements().size(); i++)
			deleteTravelElement(travelComponent.getTravelElements().get(i));
		entityManager.remove(travelComponent);
	}

}
