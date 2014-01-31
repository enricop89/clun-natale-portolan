package beans.employeehandler;

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
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
	@EJB
	private TravelComponentHandler component_handler;
	@EJB
	private PredefinedTravelPackageHandler handler;
	@EJB
	private Search search;
	
	@Override
	@RolesAllowed("EMPLOYEE")
	public String addNewPredefinedTravelPackage(PredefinedTravelPackageDTO predefinedTravelPackage){
		return handler.addNewPredefinedTravelPackage(new PredefinedTravelPackage(predefinedTravelPackage,search));
	}

	@Override
	@RolesAllowed("EMPLOYEE")
	public String updatePredefinedTravelPackage(PredefinedTravelPackageDTO predefinedTravelPackage){	
		PredefinedTravelPackage result = new PredefinedTravelPackage();
		result.setId(search.findPredefinedTravelPackage(predefinedTravelPackage).getId());
		result.setAll(predefinedTravelPackage, search);
		return handler.updatePredefinedTravelPackage(result);
	}
	
	@Override
	@RolesAllowed("EMPLOYEE")
	public void deletePredefinedTravelPackage (PredefinedTravelPackageDTO prefedefinedTravelPackage){
		handler.deletePredefinedTravelPackage(search.findPredefinedTravelPackage(prefedefinedTravelPackage));
	}
	
	@Override
	@RolesAllowed({"EMPLOYEE"})
	public boolean addNewTravelComponent(TravelComponentDTO travelComponent){
		return component_handler.addNewTravelComponent(new TravelComponent(travelComponent,search), travelComponent.getAvailability());
	}
	
	@Override
	@RolesAllowed({"EMPLOYEE"})
	public boolean updateTravelComponent(TravelComponentDTO travelComponent){
		TravelComponent result = new TravelComponent();
		result.setTravelElements(search.findTravelComponent(travelComponent).getTravelElements());
		result.setId(search.findTravelComponent(travelComponent).getId());
		result.setPredefinedTravelPackagess(search.findTravelComponent(travelComponent).getPredefinedTravelPackages());	
		result.setAll(travelComponent);
		return component_handler.updateTravelComponent(result, travelComponent.getAvailability());
	}
	
	@Override
	@RolesAllowed({"EMPLOYEE"})
	public void deleteTravelComponent(TravelComponentDTO travelComponent){
		component_handler.deleteTravelComponent(search.findTravelComponent(travelComponent),null);
	}
	
	@Override
	@RolesAllowed({"EMPLOYEE","CUSTOMER"})
	public boolean addTravelComponentToPredefinedTravelPackage(PredefinedTravelPackageDTO predefinedTravelPackage, TravelComponentDTO travelComponent){
		//control first if it is in the personalizedTravelPackage already
		for(int i = 0; i < predefinedTravelPackage.getTravelComponents().size(); i++)
			if(predefinedTravelPackage.getTravelComponents().get(i).getId() == travelComponent.getId())
				return false;
		
		predefinedTravelPackage.getTravelComponents().add(travelComponent);
		return true;
	}
	
	@Override
	@RolesAllowed({"EMPLOYEE","CUSTOMER"})
	public boolean removeTravelComponentFromPredefinedTravelPackage(PredefinedTravelPackageDTO predefinedTravelPackage, TravelComponentDTO travelComponent){
		if(predefinedTravelPackage.getTravelComponents().size() == 1)
			return false;
		
		else			
			predefinedTravelPackage.getTravelComponents().remove(travelComponent);
		return true;
	}	
}
