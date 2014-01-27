package beans.customerhandler;

import javax.ejb.Local;
import beans.accountmanagement.UserDTO;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.Components_HelperDTO;
import beans.travelpackage.PersonalizedTravelPackageDTO;
import beans.travelpackage.PredefinedTravelPackageDTO;

@Local
public interface CustomerHandlerInterface {
	
	public boolean addTravelComponentToPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage, TravelComponentDTO travelComponent);

	public String removeTravelComponentFromPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage, Components_HelperDTO travelComponent);

	public String confirmPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage);

	public String joinPersonalizedTravelPackage(UserDTO user, PersonalizedTravelPackageDTO personalizedTravelPackage);

	public String updatePersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage);

	public boolean deletePersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage);

	public String addNewPersonalizedTravelPackage(UserDTO user, PredefinedTravelPackageDTO predefinedTravelPackage);

	public boolean addTravelComponentToGiftList(UserDTO user, Components_HelperDTO travelComponent, PersonalizedTravelPackageDTO personalizedTravelPackage);

	public void removeTravelComponentFromGiftList(GiftElements_HelperDTO giftListElement);
	
	public boolean payTravelComponent(UserDTO owner, UserDTO payer, GiftElements_HelperDTO giftListElement);
}
