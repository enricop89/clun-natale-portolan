package entities;

import entities.TravelComponent;
import entities.TravelElement;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.*;

import beans.travelcomponent.ComponentType;
import beans.travelcomponent.FlightType;

/**
 * Entity implementation class for Entity: Components_Helper
 *
 */
@Entity

public class Components_Helper implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PERSONALIZED_TRAVEL_PACKAGE_ID")
	private PersonalizedTravelPackage personalizedTravelPackage;
	
	private TravelComponent travelComponent;   

	private TravelElement travelElement;

	// those fields handle persistence of payed TravelComponents, even if their reference is deleted or modified
	// if the associated TravelComponent is confirmed, than those information will be used to refer to the TravelComponent instead
	@Enumerated(EnumType.STRING)	
	private ComponentType type;
	private String supplyingCompany;	
	private FlightType flightType;
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
	
	
	public Components_Helper() {
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
	
	public PersonalizedTravelPackage getPersonalizedTravelPackage() {
		return this.personalizedTravelPackage;
	}

	public void setPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage) {
		this.personalizedTravelPackage = personalizedTravelPackage;
	}
	
	public TravelComponent getTravelComponent() {
		return this.travelComponent;
	}

	public void setTravelComponent(TravelComponent travelComponent) {
		this.travelComponent = travelComponent;
	}   
  
	public TravelElement getTravelElement() {
		return this.travelElement;
	}

	public void setTravelElement(TravelElement travelElement) {
		this.travelElement = travelElement;
	}
   
	public void setPersistence(TravelComponent travelComponent){
		this.type = travelComponent.getType();
		this.supplyingCompany = travelComponent.getSupplyingCompany();
		this.flightType = travelComponent.getFlightType();
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
	
	public TravelComponent getPersistence(){
		TravelComponent travelComponent = new TravelComponent();
		travelComponent.setType(this.type);
		travelComponent.setSupplyingCompany(this.supplyingCompany);
		travelComponent.setFlightType(this.flightType);
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
