package entities;

import java.io.Serializable;
import java.lang.String;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import beans.travelcomponent.ComponentType;
//import beans.travelcomponent.FlightType;
import beans.travelcomponent.TravelComponentDTO;
import beans.utils.Search;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: TravelComponent
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name=TravelComponent.FIND_ALL,
				query="SELECT t FROM TravelComponent t")
})
public class TravelComponent implements Serializable {
	private static final long serialVersionUID = 1L;
	 
	public static final String FIND_ALL = "TravelComponent.findAll";
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Enumerated(EnumType.STRING)	
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

	@ManyToMany(mappedBy="travelComponents")
	private List<PredefinedTravelPackage> predefinedTravelPackages;
	
	@OneToMany(mappedBy="travelComponent")
	private List<TravelElement> travelElements;
	
	public TravelComponent() {
		super();
	}   
	public TravelComponent(TravelComponentDTO travelComponent, Search search){
		this.id = travelComponent.getId();
		this.type = travelComponent.getType();
		this.supplyingCompany = travelComponent.getSupplyingCompany();
		switch(travelComponent.getType()){
		case FLIGHT:
			flightDepartureDateTime = travelComponent.getFlightDepartureDateTime();
			flightArrivalDateTime = travelComponent.getFlightArrivalDateTime();
			flightDepartureCity = travelComponent.getFlightDepartureCity();
			flightArrivalCity = travelComponent.getFlightArrivalCity();
			flightCode = travelComponent.getFlightCode();
			
			hotelCity = null;
			hotelDate = null;
			
			excursionDescription = null;
			excursionDateTime = null;
			excursionCity = null;
			break;
		case HOTEL:
			flightDepartureDateTime = null;
			flightArrivalDateTime = null;
			flightDepartureCity = null;
			flightArrivalCity = null;
			flightCode = null;
			
			hotelCity = travelComponent.getHotelCity();
			hotelDate = travelComponent.getHotelDate();
			
			excursionDescription = null;
			excursionDateTime = null;
			excursionCity = null;
			break;
		case EXCURSION:
			flightDepartureDateTime = null;
			flightArrivalDateTime = null;
			flightDepartureCity = null;
			flightArrivalCity = null;
			flightCode = null;
			
			hotelCity = null;
			hotelDate = null;
			
			excursionDescription = travelComponent.getExcursionDescription();
			excursionDateTime = travelComponent.getExcursionDateTime();
			excursionCity = travelComponent.getExcursionCity();
			break;
		}
		if(search.findTravelComponent(travelComponent) == null){ // this is a new travel component
			this.predefinedTravelPackages = new ArrayList<PredefinedTravelPackage>(); //is empty when created
			this.travelElements = null; //will be created afterwards
		}
		else{
			this.predefinedTravelPackages = search.findTravelComponent(travelComponent).getPredefinedTravelPackages();
			this.travelElements = search.findTravelComponent(travelComponent).getTravelElements();
		}
	}
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}   
	public ComponentType getType() {
		return this.type;
	}

	public void setType(ComponentType type) {
		this.type = type;
	}   
	
	public String getSupplyingCompany() {
		return this.supplyingCompany;
	}

	public void setSupplyingCompany(String supplyingCompany) {
		this.supplyingCompany = supplyingCompany;
	}   
	
	public Timestamp getFlightDepartureDateTime() {
		return this.flightDepartureDateTime;
	}

	public void setFlightDepartureDateTime(Timestamp flightDepartureDateTime) {
		this.flightDepartureDateTime = flightDepartureDateTime;
	}   
	public Timestamp getFlightArrivalDateTime() {
		return this.flightArrivalDateTime;
	}

	public void setFlightArrivalDateTime(Timestamp flightArrivalDateTime) {
		this.flightArrivalDateTime = flightArrivalDateTime;
	}   
	
	public String getFlightDepartureCity() {
		return this.flightDepartureCity;
	}

	public void setFlightDepartureCity(String flightDepartureCity) {
		this.flightDepartureCity = flightDepartureCity;
	}   
	
	public String getFlightArrivalCity() {
		return this.flightArrivalCity;
	}

	public void setFlightArrivalCity(String flightArrivalCity) {
		this.flightArrivalCity = flightArrivalCity;
	}   
	
	public String getFlightCode() {
		return this.flightCode;
	}

	public void setFlightCode(String flightCode) {
		this.flightCode = flightCode;
	} 
	
	public String getHotelCity() {
		return this.hotelCity;
	}

	public void setHotelCity(String hotelCity) {
		this.hotelCity = hotelCity;
	}   
	
	public Date getHotelDate() {
		return this.hotelDate;
	}

	public void setHotelDate(Date hotelDate) {
		this.hotelDate = hotelDate;
	}   
	
	public String getExcursionDescription() {
		return this.excursionDescription;
	}

	public void setExcursionDescription(String excursionDescription) {
		this.excursionDescription = excursionDescription;
	}   
	
	public Timestamp getExcursionDateTime() {
		return this.excursionDateTime;
	}

	public void setExcursionDateTime(Timestamp excursionDateTime) {
		this.excursionDateTime = excursionDateTime;
	}   
	
	public String getExcursionCity() {
		return this.excursionCity;
	}

	public void setExcursionCity(String excursionCity) {
		this.excursionCity = excursionCity;
	}
 
	public List<PredefinedTravelPackage> getPredefinedTravelPackages() {
		return this.predefinedTravelPackages;
	}
	
	public void setPredefinedTravelPackagess(List<PredefinedTravelPackage> predefinedTravelPackages) {
		this.predefinedTravelPackages = predefinedTravelPackages;
	}

	public List<TravelElement> getTravelElements() {
		return this.travelElements;
	}
	
	public void setTravelElements(List<TravelElement> travelElements) {
		this.travelElements = travelElements;
	}
	
	public void setAll(TravelComponentDTO travelComponent){
//		this.id = travelComponent.getId();
		this.type = travelComponent.getType();
		this.supplyingCompany = travelComponent.getSupplyingCompany();
		switch(travelComponent.getType()){
		case FLIGHT:
			flightDepartureDateTime = travelComponent.getFlightDepartureDateTime();
			flightArrivalDateTime = travelComponent.getFlightArrivalDateTime();
			flightDepartureCity = travelComponent.getFlightDepartureCity();
			flightArrivalCity = travelComponent.getFlightArrivalCity();
			flightCode = travelComponent.getFlightCode();
			
			hotelCity = null;
			hotelDate = null;
			
			excursionDescription = null;
			excursionDateTime = null;
			excursionCity = null;
			break;
		case HOTEL:
			flightDepartureDateTime = null;
			flightArrivalDateTime = null;
			flightDepartureCity = null;
			flightArrivalCity = null;
			flightCode = null;
			
			hotelCity = travelComponent.getHotelCity();
			hotelDate = travelComponent.getHotelDate();
			
			excursionDescription = null;
			excursionDateTime = null;
			excursionCity = null;
			break;
		case EXCURSION:
			flightDepartureDateTime = null;
			flightArrivalDateTime = null;
			flightDepartureCity = null;
			flightArrivalCity = null;
			flightCode = null;
			
			hotelCity = null;
			hotelDate = null;
			
			excursionDescription = travelComponent.getExcursionDescription();
			excursionDateTime = travelComponent.getExcursionDateTime();
			excursionCity = travelComponent.getExcursionCity();
			break;
		}
	}
}
