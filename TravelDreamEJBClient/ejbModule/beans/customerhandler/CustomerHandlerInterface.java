package beans.customerhandler;

import javax.ejb.Local;
import beans.accountmanagement.UserDTO;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.Components_HelperDTO;
import beans.travelpackage.PersonalizedTravelPackageDTO;
import beans.travelpackage.PredefinedTravelPackageDTO;

@Local
public interface CustomerHandlerInterface {
	public boolean addNewTravelComponentToPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage, TravelComponentDTO travelComponent);

	public void removeTravelComponentFromPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage, Components_HelperDTO travelComponent);

	public boolean confirmPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage);

	public boolean joinPersonalizedTravelPackage(UserDTO user, PersonalizedTravelPackageDTO personalizedTravelPackage);

	public boolean addNewPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage);

	public boolean updatePersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage);

	public boolean deletePersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage);

	public void addNewPersonalizedTravelPackage(UserDTO user, PredefinedTravelPackageDTO predefinedTravelPackage);

	public boolean addTravelComponentToGiftList(UserDTO user, Components_HelperDTO travelComponent, PersonalizedTravelPackageDTO personalizedTravelPackage);

	public void removeTravelComponentFromGiftList(GiftElements_HelperDTO giftListElement);
	
	public boolean payTravelComponent(UserDTO owner, UserDTO payer, GiftElements_HelperDTO giftListElement);
}
