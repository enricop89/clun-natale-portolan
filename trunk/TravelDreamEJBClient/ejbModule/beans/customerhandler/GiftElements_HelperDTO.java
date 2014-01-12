package beans.customerhandler;

import beans.travelpackage.Components_HelperDTO;
import beans.travelpackage.PersonalizedTravelPackageDTO;

public class GiftElements_HelperDTO {
	private long id;	
	private GiftListDTO giftList;	
	private PersonalizedTravelPackageDTO personalizedTravelPackage;
	private Components_HelperDTO travelComponent; 

	public GiftElements_HelperDTO(){
		super();
	} 
	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}   
	
	public PersonalizedTravelPackageDTO getPersonalizedTravelPackage() {
		return this.personalizedTravelPackage;
	}

	public void setPersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage) {
		this.personalizedTravelPackage = personalizedTravelPackage;
	}   
	public Components_HelperDTO getTravelComponent() {
		return this.travelComponent;
	}

	public void setTravelComponent(Components_HelperDTO travelComponent) {
		this.travelComponent = travelComponent;
	}   
	
	public GiftListDTO getGiftList() {
		return this.giftList;
	}

	public void setGiftList(GiftListDTO giftList) {
		this.giftList = giftList;
	}   
}
