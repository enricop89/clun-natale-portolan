package beans.travelcomponent;
import java.sql.Date;
import java.sql.Timestamp;

import org.hibernate.validator.constraints.NotEmpty;

public class TravelComponentDTO {
	
@NotEmpty
private ComponentType type;
@NotEmpty
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

public void setComponentType(ComponentType type) {
    this.type = type;
}
public ComponentType getComponentType(ComponentType type) {
    return type;
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

public Date getHotelStartingDate() {
	return this.hotelDate;
}

public void setHotelStartingDate(Date hotelDate) {
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

}
