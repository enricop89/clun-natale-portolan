package beans.travelcomponent;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import beans.travelpackage.PredefinedTravelPackageDTO;

public class TravelComponentDTO {
	private long id;
	@NotEmpty
	private ComponentType type;	
	@NotEmpty
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
	private List<PredefinedTravelPackageDTO> predefinedTravelPackages;
	private List<TravelElementDTO> travelElements;
	
	public TravelComponentDTO() {
		super();
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
	
	public FlightType getFlightType() {
		return this.flightType;
	}

	public void setFlightType(FlightType flightType) {
		this.flightType = flightType;
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
 
	public List<PredefinedTravelPackageDTO> getPredefinedTravelPackages() {
		return this.predefinedTravelPackages;
	}
	
	public void setPredefinedTravelPackagess(List<PredefinedTravelPackageDTO> predefinedTravelPackages) {
		this.predefinedTravelPackages = predefinedTravelPackages;
	}

	public List<TravelElementDTO> getTravelElements() {
		return this.travelElements;
	}
	
	public void setTravelElements(List<TravelElementDTO> travelElements) {
		this.travelElements = travelElements;
	}
}
