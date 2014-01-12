package beans.employeehandler;

import javax.ejb.Local;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.PredefinedTravelPackageDTO;

@Local
public interface EmployeeHandlerInterface {

	public boolean addNewPredefinedTravelPackage(PredefinedTravelPackageDTO predefinedTravelPackage);		

	public boolean updatePredefinedTravelPackage(PredefinedTravelPackageDTO predefinedTravelPackage);	

	public void deletePredefinedTravelPackage (PredefinedTravelPackageDTO prefedefinedTravelPackage);

	public boolean addNewTravelComponent(TravelComponentDTO travelComponent);

	public boolean updateTravelComponent(TravelComponentDTO travelComponent);

	public void deleteTravelComponent(TravelComponentDTO travelComponent);

	public boolean addTravelComponentToPersonalizedTravelPackage(PredefinedTravelPackageDTO predefinedTravelPackage, TravelComponentDTO travelComponent);

	public boolean removeTravelComponentFromPersonalizedTravelPackage(PredefinedTravelPackageDTO predefinedTravelPackage, TravelComponentDTO travelComponent);

}
