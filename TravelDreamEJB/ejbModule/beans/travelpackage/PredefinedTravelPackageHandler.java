package beans.travelpackage;
import java.util.ArrayList;
import java.util.List;


import javax.persistence.PersistenceContext;

import beans.travelcomponent.ComponentType;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.annotation.security.RolesAllowed;

import entities.*;

import javax.persistence.EntityManager;


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



//-------------------------------------------------------

public boolean Controlconsistency(PredefinedTravelPackage predTP){
	List<TravelComponent> Arr_flight=new ArrayList<>();
	List<TravelComponent> Dep_flight=new ArrayList<>();
	List<TravelComponent> Hotel= new ArrayList<>();
	List<TravelComponent> Excursion= new ArrayList<>();
	int compareTime;
	for (int i=0;i<predTP.getTravelComponents().size();i++){ //divido component nei vari type
		ComponentType aux=predTP.getTravelComponents().get(i).getType();
		if(aux==ComponentType.ARRIVAL_FLIGHT)
			Arr_flight.add(predTP.getTravelComponents().get(i));
		if(aux==ComponentType.DEPARTURE_FLIGHT)
			Dep_flight.add(predTP.getTravelComponents().get(i));
		if(aux==ComponentType.HOTEL)
			Hotel.add(predTP.getTravelComponents().get(i));
		if(aux==ComponentType.EXCURSION)
			Excursion.add(predTP.getTravelComponents().get(i));}
	//caso more than a departure or arrival flyght
		if(Arr_flight.size()>1 || Dep_flight.size()>1) 
			return false;
		compareTime=Arr_flight.get(0).getFlightArrivalDateTime().compareTo(Dep_flight.get(0).getFlightDepartureDateTime());
		if(compareTime==1 ) //compareTo return 1 if data1>data2, 0 se uguali, -1 se data1<data2
			return false; 	//caso aereo andata dopo aereo ritorno
		
		for (int i=0;i<predTP.getTravelComponents().size();i++){//PENSARE A CICLO FOR
	
		if(Hotel.get(i).getHotelDate().compareTo(Arr_flight.get(0).getFlightArrivalDateTime())<0)
			return false; 	//Data hotel prima Data volo arrivo
		if(Hotel.get(i).getHotelDate().compareTo(Dep_flight.get(0).getFlightDepartureDateTime())>0)
			return false; 	//Data hotel dopo Data volo partenza
		if(Excursion.get(i).getExcursionDateTime().compareTo(Arr_flight.get(0).getFlightArrivalDateTime())<0)
			return false; 	//Data excursion prima Data volo arrivo
		if(Excursion.get(i).getExcursionDateTime().compareTo(Dep_flight.get(0).getFlightDepartureDateTime())>0)
			return false; 	//Data excursion dopo Data volo partenza
		
		if(Arr_flight.get(0)!=null && Dep_flight.get(0)!=null)//controllo se città arrivo e città partenza sono uguali
			if(Dep_flight.get(0).getFlightArrivalCity()==Arr_flight.get(0).getFlightDepartureCity())
				return false;
		if(Hotel.get(i).getHotelCity()!=Dep_flight.get(0).getFlightArrivalCity() && 
				Excursion.get(i).getExcursionCity()!=Dep_flight.get(0).getFlightArrivalCity())
			return false;
		}
		
		return true;// TUTTO ANDATO BENE
		
		
	
	}
	
}









