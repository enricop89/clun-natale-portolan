package beans.travelpackage;

import java.sql.Date;
import java.sql.Timestamp;

import beans.travelcomponent.ComponentType;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelcomponent.TravelElementDTO;

public class Components_HelperDTO {
	private long id;
	private PersonalizedTravelPackageDTO personalizedTravelPackage;	
	private TravelComponentDTO travelComponent;   
	private TravelElementDTO travelElement;
	
	// those fields handle persistence of payed TravelComponents, even if their reference is deleted or modified
	// if the associated TravelComponent is confirmed, than those information will be used to refer to the TravelComponent instead
	private ComponentType type;
	private String supplyingCompany;	
	private Timestamp flightDepartureDateTime;
	private Timestamp flightArrivalDateTime;
	private String flightDepartureCity;
	private String flightArrivalCity;
	private String flightCode;
	private String hotelCity;
	private Date hotelDate;
	private String excursionDescription;
	private Timestamp excursionDateTime;
	private String excursionCity;
		
	public Components_HelperDTO() {
		super();
		this.type = null;
		this.supplyingCompany = null;
		this.flightDepartureDateTime = null;
		this.flightArrivalDateTime = null;
		this.flightDepartureCity = null;
		this.flightArrivalCity = null;
		this.flightCode = null;
		this.hotelCity = null;
		this.hotelDate = null;
		this.excursionDescription = null;
		this.excursionDateTime = null;
		this.excursionCity = null;
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
	
	public TravelComponentDTO getTravelComponent() {
		return this.travelComponent;
	}

	public void setTravelComponent(TravelComponentDTO travelComponent) {
		this.travelComponent = travelComponent;
	}   
  
	public TravelElementDTO getTravelElement() {
		return this.travelElement;
	}

	public void setTravelElement(TravelElementDTO travelElement) {
		this.travelElement = travelElement;
	}
   
	public void setPersistence(TravelComponentDTO travelComponent){
		this.type = travelComponent.getType();
		this.supplyingCompany = travelComponent.getSupplyingCompany();
		this.flightDepartureDateTime = travelComponent.getFlightDepartureDateTime();
		this.flightArrivalDateTime = travelComponent.getFlightArrivalDateTime();
		this.flightDepartureCity = travelComponent.getFlightDepartureCity();
		this.flightArrivalCity = travelComponent.getFlightArrivalCity();
		this.flightCode = travelComponent.getFlightCode();
		this.hotelCity = travelComponent.getHotelCity();
		this.hotelDate = travelComponent.getHotelDate();
		this.excursionDescription = travelComponent.getExcursionDescription();
		this.excursionDateTime = travelComponent.getExcursionDateTime();
		this.excursionCity = travelComponent.getExcursionCity();
	}
	
	public TravelComponentDTO getPersistence(){
		TravelComponentDTO travelComponent = new TravelComponentDTO();
		travelComponent.setType(this.type);
		travelComponent.setSupplyingCompany(this.supplyingCompany);
		travelComponent.setFlightDepartureDateTime(this.flightDepartureDateTime);
		travelComponent.setFlightArrivalDateTime(this.flightArrivalDateTime);
		travelComponent.setFlightDepartureCity(this.flightDepartureCity);
		travelComponent.setFlightArrivalCity(this.flightArrivalCity);
		travelComponent.setFlightCode(this.flightCode);
		travelComponent.setHotelCity(this.hotelCity);
		travelComponent.setHotelDate(this.hotelDate);
		travelComponent.setExcursionDescription(this.excursionDescription);
		travelComponent.setExcursionDateTime(this.excursionDateTime);
		travelComponent.setExcursionCity(this.excursionCity);
		return travelComponent;
	}
}
