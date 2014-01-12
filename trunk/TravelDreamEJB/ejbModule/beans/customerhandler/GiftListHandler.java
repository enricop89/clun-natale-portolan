package beans.customerhandler;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.annotation.security.RolesAllowed;
import entities.*;


@Stateless
@LocalBean
public class GiftListHandler {
	@PersistenceContext
    private EntityManager entityManager;
	
	@RolesAllowed("CUSTOMER")
	public boolean addTCtoGiftList(User owner,GiftList gf,Components_Helper tc,PersonalizedTravelPackage persTP){
		if(gf.getOwner()!=persTP.getOwner()) //same owner of gf and persTP otherwise false
			return false;
		if(gf.getOwner()!=tc.getTravelElement().getOwner()) //same owner of gf and travel component otherwise false
			return false;
		if(tc.getTravelElement().getOwner()==owner)
			return false;//if it is confirm
		
		for(int i=0;i<gf.getGiftElements().size();i++)//control if it is already in
			if(tc==gf.getGiftElements().get(i).getTravelComponent())
				return false;
		for(int i=0;i<persTP.getTravelComponents().size();i++)//control if tc is in persTP, then add
			if(tc==persTP.getTravelComponents().get(i)){	
			GiftElements_Helper aux=new GiftElements_Helper();
			aux.getTravelComponent().setTravelComponent(tc.getTravelComponent());
			gf.getGiftElements().add(aux);
			entityManager.persist(gf);
			return true;}
		return false;
	}
	@RolesAllowed("CUSTOMER")
	public boolean removeTCfromGiftList(User owner,GiftList gf,Components_Helper tc,PersonalizedTravelPackage persTP){
		if(gf.getOwner()!=persTP.getOwner()) //same owner otherwise false
			return false;
		if(gf.getOwner()!=tc.getTravelElement().getOwner()) //same owner of gf and travel component otherwise false
			return false;
		if(tc.getTravelElement().getOwner()==owner)
			return false;//if it is confirm
		gf.getGiftElements().remove(tc);
		entityManager.remove(gf);
		return true;
	} 
	
	public boolean payTravelComponent(User owner,User payer,GiftList gf,Components_Helper tc,PersonalizedTravelPackage persTP){ //persTP non serve?
		if(payer==owner)
			return false;
				//for(int i=0;i<tc.getTravelComponent().getTravelElements().size();i++)//control if it is already payed
					if(tc.getTravelElement().getOwner()==owner)
						return false; 
				for(int i=0;i<gf.getGiftElements().size();i++)
					if(gf.getGiftElements().get(i).getTravelComponent()==tc) {//se il component è nella gift list
						gf.getGiftElements().get(i).getTravelComponent().getTravelElement().setOwner(owner);
						entityManager.merge(gf); //metto l'owner uguale all'owner
						return true;	
							}
		return false;
	}
	
	
}  

