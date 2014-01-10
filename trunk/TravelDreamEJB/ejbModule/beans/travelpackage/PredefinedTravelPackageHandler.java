package beans.travelpackage;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.annotation.security.RolesAllowed;

import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Session Bean implementation class PredefinedTravelPackageHandler
 */
@Stateless
@LocalBean
public class PredefinedTravelPackageHandler {
	@PersistenceContext
    private EntityManager entityManager;
	

	@RolesAllowed("EMPLOYEE")
	public boolean savenewPredefinedTravelPackage(PredefinedTravelPackage predTP){
	//FUNZIONE CONTROLLO PERSISTENZA
	entityManager.persist(predTP);
	return true;

	}
	@RolesAllowed("EMPLOYEE")
	public boolean updatePredefinedTravelPackage(PredefinedTravelPackage predTP){
	//FUNZIONE CONTROLLO PERSISTENZA
	entityManager.merge(predTP);	
		return true;
	}
	@RolesAllowed("EMPLOYEE")
	public void deletePredefinedTravelPackage (PredefinedTravelPackage predTP){
		entityManager.remove(predTP);	
	}
	
	public void copyPredefinedTravelPackage(PredefinedTravelPackage predTP,User owner ){
		
		PersonalizedTravelPackage persTP= new PersonalizedTravelPackage();
		persTP.setOwner(owner);
		List<Components_Helper> constructor= new ArrayList<Components_Helper>();
		for(int i=0;i<predTP.getTravelComponents().size();i++){
			Components_Helper ch= new Components_Helper();
			ch.setTravelElement(null);
			ch.setPersonalizedTravelPackage(persTP);   
			ch.setTravelComponent(predTP.getTravelComponents().get(i));
			constructor.add(ch);	
		}
			persTP.setTravelComponents(constructor);
			persTP.setName(predTP.getName());
			PersonalizedTravelPackageHandler ptphandler= new PersonalizedTravelPackageHandler();
			ptphandler.addNewPersonalizedTravelPackage(persTP);
	}
}
