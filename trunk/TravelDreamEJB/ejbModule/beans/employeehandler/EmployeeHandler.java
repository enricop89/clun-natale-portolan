package beans.employeehandler;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;

import entities.*;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelcomponent.TravelComponentHandler;
import beans.travelpackage.PredefinedTravelPackageDTO;
import beans.travelpackage.PredefinedTravelPackageHandler;
import beans.utils.Search;

/**
 * Session Bean implementation class EmployeeHandler
 */
@Stateless
public class EmployeeHandler implements EmployeeHandlerInterface{

	@Override
	@RolesAllowed("EMPLOYEE")
	public boolean addNewPredefinedTravelPackage(PredefinedTravelPackageDTO predefinedTravelPackage){
		PredefinedTravelPackageHandler handler = new PredefinedTravelPackageHandler();
		return handler.addNewPredefinedTravelPackage(new PredefinedTravelPackage(predefinedTravelPackage));
	}

	@Override
	@RolesAllowed("EMPLOYEE")
	public boolean updatePredefinedTravelPackage(PredefinedTravelPackageDTO predefinedTravelPackage){	
		Search search = new Search();
		PredefinedTravelPackageHandler handler = new PredefinedTravelPackageHandler();
		return handler.updatePredefinedTravelPackage(search.findPredefinedTravelPackage(predefinedTravelPackage));
	}
	
	@Override
	@RolesAllowed("EMPLOYEE")
	public void deletePredefinedTravelPackage (PredefinedTravelPackageDTO prefedefinedTravelPackage){
		Search search = new Search();
		PredefinedTravelPackageHandler handler = new PredefinedTravelPackageHandler();
		handler.deletePredefinedTravelPackage(search.findPredefinedTravelPackage(prefedefinedTravelPackage));
	}
	
	@Override
	@RolesAllowed({"EMPLOYEE"})
	public boolean addNewTravelComponent(TravelComponentDTO travelComponent){
		TravelComponentHandler component_handler = new TravelComponentHandler();
		return component_handler.addNewTravelComponent(new TravelComponent(travelComponent), travelComponent.getAvailability());
	}
	
	@Override
	@RolesAllowed({"EMPLOYEE"})
	public boolean updateTravelComponent(TravelComponentDTO travelComponent){
		TravelComponentHandler component_handler = new TravelComponentHandler();
		Search search = new Search();
		return component_handler.updateTravelComponent(search.findTravelComponent(travelComponent), travelComponent.getAvailability());
	}
	
	@Override
	@RolesAllowed({"EMPLOYEE"})
	public void deleteTravelComponent(TravelComponentDTO travelComponent){
		TravelComponentHandler component_handler = new TravelComponentHandler();
		Search search = new Search();
		component_handler.deleteTravelComponent(search.findTravelComponent(travelComponent));
	}
	
	@Override
	@RolesAllowed({"EMPLOYEE"})
	public boolean addTravelComponentToPersonalizedTravelPackage(PredefinedTravelPackageDTO predefinedTravelPackage, TravelComponentDTO travelComponent){
		//control first if it is in the personalizedTravelPackage already
		for(int i = 0; i < predefinedTravelPackage.getTravelComponents().size(); i++)
			if(predefinedTravelPackage.getTravelComponents().get(i) == travelComponent)
				return false;
		predefinedTravelPackage.getTravelComponents().add(travelComponent);
		return true;
	}
	
	@Override
	@RolesAllowed({"EMPLOYEE"})
	public boolean removeTravelComponentFromPersonalizedTravelPackage(PredefinedTravelPackageDTO predefinedTravelPackage, TravelComponentDTO travelComponent){
		return predefinedTravelPackage.getTravelComponents().remove(travelComponent);
	}	
}
