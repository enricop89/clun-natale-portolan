package beans.customerhandler;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.annotation.security.RolesAllowed;

import beans.travelcomponent.TravelComponentHandler;
import entities.*;

@Stateless
@LocalBean
public class GiftListHandler {
	@PersistenceContext
    private EntityManager entityManager;
	@EJB
	private TravelComponentHandler handler;
	
	@RolesAllowed("CUSTOMER")
	public boolean addTravelComponentToGiftList(User owner, GiftList giftList, Components_Helper travelComponent, PersonalizedTravelPackage personalizedTravelPackage){
		if(travelComponent.getTravelElement().getOwner() != null)
			return false;//if it is already confirmed
		
		if(giftList.getOwner() != owner)
			return false;//for some reason the giftList referred is not the one of the owner
		
		for(int i=0;i<giftList.getGiftElements().size();i++) //control if it is already in the giftList
			if(travelComponent == giftList.getGiftElements().get(i).getTravelComponent())
				return false;
		
		for(int i=0;i<personalizedTravelPackage.getTravelComponents().size();i++) //control if travelComponent is not in personalizedTravelPackage, then add
			if(travelComponent == personalizedTravelPackage.getTravelComponents().get(i)){	
				GiftElements_Helper aux=new GiftElements_Helper();
				aux.setTravelComponent(travelComponent);
				aux.setPersonalizedTravelPackage(personalizedTravelPackage);
				giftList.getGiftElements().add(aux);
				entityManager.merge(giftList);
				return true;
			}
		return false;
	}
	
	@RolesAllowed("CUSTOMER")
	public void removeTravelComponentFromGiftList(GiftElements_Helper giftListElement){
		giftListElement.getGiftList().getGiftElements().remove(giftListElement);
		entityManager.merge(giftListElement.getGiftList());
		entityManager.remove(giftListElement);
	} 
	
	public boolean payTravelComponent(User owner, User payer, GiftElements_Helper giftListElement){
		if(payer==owner) //in this case this cannot be done!
			return false; 
		
		if(giftListElement.getTravelComponent().getTravelElement() != null) //control if it is already payed
				return false; 
		
		TravelElement element = handler.payTravelComponent(giftListElement.getTravelComponent().getTravelComponent(), owner);
		if(element == null) //errors or no travelElements available
			return false;
		
		for(int i = 0; i < giftListElement.getPersonalizedTravelPackage().getTravelComponents().size(); i++)
			if(giftListElement.getPersonalizedTravelPackage().getTravelComponents().get(i) == giftListElement.getTravelComponent()){
				giftListElement.getPersonalizedTravelPackage().getTravelComponents().get(i).setPersistence(giftListElement.getTravelComponent().getTravelComponent());
				giftListElement.getPersonalizedTravelPackage().getTravelComponents().get(i).setTravelElement(element);
				return true;
			}
		return false; // some errors incurred
	}		
}  

