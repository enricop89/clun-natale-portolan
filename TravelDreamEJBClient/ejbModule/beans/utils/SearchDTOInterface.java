package beans.utils;

import java.sql.Date;
import java.util.List;

import javax.ejb.Local;

import beans.accountmanagement.UserDTO;
import beans.customerhandler.GiftListDTO;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.PersonalizedTravelPackageDTO;
import beans.travelpackage.PredefinedTravelPackageDTO;

@Local
public interface SearchDTOInterface {
	public GiftListDTO findGiftList(UserDTO owner);
	
	public PersonalizedTravelPackageDTO findPersonalizedTravelPackage(long id);
	
	public List<PersonalizedTravelPackageDTO> findAllPersonalizedTravelPackages(UserDTO owner);
	
	public List<PersonalizedTravelPackageDTO> findAllPersonalizedTravelPackages();
	
	public List<PredefinedTravelPackageDTO> findPredefinedTravelPackage(String name, Date departureDate, Date returnDate);
	
	public List<PredefinedTravelPackageDTO> findAllPredefinedTravelPackages();
	
	public List<TravelComponentDTO> findTravelComponent(TravelComponentDTO searchCriteria);
	
	public List<TravelComponentDTO> findAllTravelComponents();
	
	public UserDTO findUser(String email);
	
	public List<UserDTO> findUser(String firstName, String lastName);
	
	public List<UserDTO> findAllUser();
	
}
