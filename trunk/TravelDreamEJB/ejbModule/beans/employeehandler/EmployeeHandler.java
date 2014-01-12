package beans.employeehandler;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateful;

import entities.*;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelcomponent.TravelComponentHandler;
import beans.travelpackage.PredefinedTravelPackageDTO;
import beans.travelpackage.PredefinedTravelPackageHandler;
import beans.utils.Search;

/**
 * Session Bean implementation class EmployeeHandler
 */
@Stateful
public class EmployeeHandler implements EmployeeHandlerInterface{
	private PredefinedTravelPackageHandler handler;
	private TravelComponentHandler component_handler;
	private Search search;
	
	@Override
	@RolesAllowed("EMPLOYEE")
	public boolean addNewPredefinedTravelPackage(PredefinedTravelPackageDTO predefinedTravelPackage){			
		return handler.addNewPredefinedTravelPackage(new PredefinedTravelPackage(predefinedTravelPackage));
	}

	@Override
	@RolesAllowed("EMPLOYEE")
	public boolean updatePredefinedTravelPackage(PredefinedTravelPackageDTO predefinedTravelPackage){	
		return handler.updatePredefinedTravelPackage(search.findPredefinedTravelPackage(predefinedTravelPackage));
	}
	
	@Override
	@RolesAllowed("EMPLOYEE")
	public void deletePredefinedTravelPackage (PredefinedTravelPackageDTO prefedefinedTravelPackage){
		handler.deletePredefinedTravelPackage(search.findPredefinedTravelPackage(prefedefinedTravelPackage));
	}
	
	@Override
	@RolesAllowed({"EMPLOYEE"})
	public boolean addNewTravelComponent(TravelComponentDTO travelComponent){
		return component_handler.addNewTravelComponent(new TravelComponent(travelComponent), travelComponent.getAvailability());
	}
	
	@Override
	@RolesAllowed({"EMPLOYEE"})
	public boolean updateTravelComponent(TravelComponentDTO travelComponent){
		return component_handler.updateTravelComponent(search.findTravelComponent(travelComponent), travelComponent.getAvailability());
	}
	
	@Override
	@RolesAllowed({"EMPLOYEE"})
	public void deleteTravelComponent(TravelComponentDTO travelComponent){
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
