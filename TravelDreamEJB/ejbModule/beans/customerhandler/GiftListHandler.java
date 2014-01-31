package beans.customerhandler;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.annotation.security.RolesAllowed;

import beans.travelcomponent.TravelComponentHandler;
import beans.utils.SendEmail;
import entities.*;

@Stateless
@LocalBean
public class GiftListHandler {
	@PersistenceContext
    private EntityManager entityManager;
	@EJB
	private TravelComponentHandler handler;
	
	@RolesAllowed({"CUSTOMER"})
	public boolean addTravelComponentToGiftList(User owner, GiftList giftList, Components_Helper travelComponent, PersonalizedTravelPackage personalizedTravelPackage){
		if(travelComponent.getTravelElement() != null)
			return false;//if it is already confirmed
		
		if(giftList.getOwner() != owner)
			return false;//for some reason the giftList referred is not the one of the owner
		
		for(int i=0;i<giftList.getGiftElements().size();i++) //control if it is already in the giftList
			if(travelComponent == giftList.getGiftElements().get(i).getTravelComponent())
				return false;
		
		for(int i=0;i<personalizedTravelPackage.getTravelComponents().size();i++) //control if travelComponent is not in personalizedTravelPackage, then add
			if(travelComponent == personalizedTravelPackage.getTravelComponents().get(i)){	
				GiftElements_Helper giftElement = new GiftElements_Helper();
				giftElement.setTravelComponent(travelComponent);
				giftElement.setPersonalizedTravelPackage(personalizedTravelPackage);
				giftElement.setGiftList(giftList);
				giftList.getGiftElements().add(giftElement);
				entityManager.persist(giftElement);
				entityManager.merge(giftList);
				return true;
			}
		return false;
	}
	
	@RolesAllowed({"CUSTOMER"})
	public void removeTravelComponentFromGiftList(GiftElements_Helper giftListElement){
		giftListElement.getGiftList().getGiftElements().remove(giftListElement);
		entityManager.merge(giftListElement.getGiftList());
		entityManager.remove(giftListElement);
	} 
	
	public boolean payTravelComponent(User owner, User payer, GiftElements_Helper giftListElement){
		GiftList giftList = giftListElement.getGiftList();
		TravelComponent persistence = new TravelComponent();
		persistence.setAll(giftListElement.getTravelComponent().getTravelComponent());
		
		if(payer==owner) //in this case this cannot be done!
			return false; 
		
		if(giftListElement.getTravelComponent().getTravelElement() != null) //control if it is already payed
				return false; 
		
		TravelElement element = handler.payTravelComponent(giftListElement.getTravelComponent().getTravelComponent(), owner, giftListElement.getPersonalizedTravelPackage());
		if(element == null) //errors or no travelElements available
			return false;
		
		for(int i = 0; i < giftListElement.getPersonalizedTravelPackage().getTravelComponents().size(); i++)
			if(giftListElement.getPersonalizedTravelPackage().getTravelComponents().get(i) == giftListElement.getTravelComponent()){
				giftListElement.getPersonalizedTravelPackage().getTravelComponents().get(i).setPersistence(persistence);
				giftListElement.getPersonalizedTravelPackage().getTravelComponents().get(i).setTravelElement(element);
				giftListElement.setPersonalizedTravelPackage(null);
				giftListElement.setTravelComponent(null);
				giftList.getGiftElements().remove(giftListElement);
				entityManager.remove(giftListElement); // the element is removed from the gift list!
				entityManager.merge(giftList);
				SendEmail.send(owner.getEmail(), "One of your friend payed your component", 
						"Hi " + owner.getFirstName() + " " + owner.getLastName() + "!\nThe staff wants to inform you that "
					+	payer.getFirstName() + " " + payer.getLastName() + " has payed a component of your Gift List!.\n"
					+ 	"The package: " + giftListElement.getPersonalizedTravelPackage().getName() + " has been updated!");				
				return true;
			}
		return false; // some errors incurred
	}		
}  

