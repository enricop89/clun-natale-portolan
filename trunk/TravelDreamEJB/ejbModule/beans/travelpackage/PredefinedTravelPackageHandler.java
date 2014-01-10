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
	
		boolean result=Controlconsistency(predTP);
		if(result)
			entityManager.persist(predTP);
		
	return result;

	}
	@RolesAllowed("EMPLOYEE")
	public boolean updatePredefinedTravelPackage(PredefinedTravelPackage predTP){
	
		boolean result=Controlconsistency(predTP);
		if(result)
			entityManager.merge(predTP);	
		return result;
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

	public boolean Controlconsistency(PredefinedTravelPackage predTP){
		List<TravelComponent> Arr_flight=new ArrayList<TravelComponent>();
		List<TravelComponent> Dep_flight=new ArrayList<TravelComponent>();
		List<TravelComponent> Hotel= new ArrayList<TravelComponent>();
		List<TravelComponent> Excursion= new ArrayList<TravelComponent>();
		for (int i=0;i<predTP.getTravelComponents().size();i++){ //divide component in the four Component_type
			ComponentType aux=predTP.getTravelComponents().get(i).getType();
			if(aux==ComponentType.ARRIVAL_FLIGHT)
				Arr_flight.add(predTP.getTravelComponents().get(i));
			if(aux==ComponentType.DEPARTURE_FLIGHT)
				Dep_flight.add(predTP.getTravelComponents().get(i));
			if(aux==ComponentType.HOTEL)
				Hotel.add(predTP.getTravelComponents().get(i));
			if(aux==ComponentType.EXCURSION)
				Excursion.add(predTP.getTravelComponents().get(i));
		}
		//caso more than a departure or arrival flyght
		if(Arr_flight.size()>1 || Dep_flight.size()>1) 
			return false;
		
		if(Arr_flight.get(0).getFlightArrivalDateTime().compareTo(Dep_flight.get(0).getFlightDepartureDateTime())>0 ) 
			//compareTo return 1 if data1>data2, 0 if equal, -1 if data1<data2
			return false; 	//departure flight after arrival flight
		
		for (int i=0;i<Hotel.size();i++){
		if(Hotel.get(i).getHotelDate().compareTo(Arr_flight.get(0).getFlightArrivalDateTime())<0)
			return false; 	//Data hotel before Data arrival_flight
		if(Hotel.get(i).getHotelDate().compareTo(Dep_flight.get(0).getFlightDepartureDateTime())>0)
			return false; 	//Data hotel after Data departure_flight
		if(Hotel.get(i).getHotelCity()!=Dep_flight.get(0).getFlightArrivalCity() )
			return false;
		}
		for (int i=0;i<Excursion.size();i++){
		if(Excursion.get(i).getExcursionDateTime().compareTo(Arr_flight.get(0).getFlightArrivalDateTime())<0)
			return false; 	//Data excursion before Data arrival_flight
		if(Excursion.get(i).getExcursionDateTime().compareTo(Dep_flight.get(0).getFlightDepartureDateTime())>0)
			return false; 	//Data excursion after Data departure_flight
		if(Excursion.get(i).getExcursionCity()!=Dep_flight.get(0).getFlightArrivalCity())
			return false;
		}
		
		if(Arr_flight.get(0)!=null && Dep_flight.get(0)!=null)
			if(Dep_flight.get(0).getFlightArrivalCity()==Arr_flight.get(0).getFlightDepartureCity())//control if arrival_city is equal to departure_city
				return false;
		
		
		
		return true;// all the controls is ok	
	}
	
}









