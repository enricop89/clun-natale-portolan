package entities;

import java.io.Serializable;
import java.lang.String;
import java.sql.Date;
import java.util.Enumeration;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: TravelComponent
 *
 */
@Entity

public class TravelComponent implements Serializable {

	   
	@Id
	private long id;
	private Enumeration type;
	private String supplyingCompany;
	private Date flightDepartureDateTime;
	private Date flightArrivalDateTime;
	private String flightDepartureCity;
	private String flightArrivalCity;
	private String flightCode;
	private String hotelCity;
	private Date hotelStartingDate;
	private Date hotelEndingDate;
	private String excursionDescription;
	private Date excursionDateTime;
	private String excursionCity;
	private static final long serialVersionUID = 1L;

	public TravelComponent() {
		super();
	}   
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}   
	public Enumeration getType() {
		return this.type;
	}

	public void setType(Enumeration type) {
		this.type = type;
	}   
	public String getSupplyingCompany() {
		return this.supplyingCompany;
	}

	public void setSupplyingCompany(String supplyingCompany) {
		this.supplyingCompany = supplyingCompany;
	}   
	public Date getFlightDepartureDateTime() {
		return this.flightDepartureDateTime;
	}

	public void setFlightDepartureDateTime(Date flightDepartureDateTime) {
		this.flightDepartureDateTime = flightDepartureDateTime;
	}   
	public Date getFlightArrivalDateTime() {
		return this.flightArrivalDateTime;
	}

	public void setFlightArrivalDateTime(Date flightArrivalDateTime) {
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
		return this.hotelStartingDate;
	}

	public void setHotelStartingDate(Date hotelStartingDate) {
		this.hotelStartingDate = hotelStartingDate;
	}   
	public Date getHotelEndingDate() {
		return this.hotelEndingDate;
	}

	public void setHotelEndingDate(Date hotelEndingDate) {
		this.hotelEndingDate = hotelEndingDate;
	}   
	public String getExcursionDescription() {
		return this.excursionDescription;
	}

	public void setExcursionDescription(String excursionDescription) {
		this.excursionDescription = excursionDescription;
	}   
	public Date getExcursionDateTime() {
		return this.excursionDateTime;
	}

	public void setExcursionDateTime(Date excursionDateTime) {
		this.excursionDateTime = excursionDateTime;
	}   
	public String getExcursionCity() {
		return this.excursionCity;
	}

	public void setExcursionCity(String excursionCity) {
		this.excursionCity = excursionCity;
	}
   
}
