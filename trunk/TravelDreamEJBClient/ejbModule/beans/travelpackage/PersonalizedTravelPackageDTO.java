package beans.travelpackage;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import beans.accountmanagement.UserDTO;

public class PersonalizedTravelPackageDTO {
	private long id; 	
	@NotEmpty
	private String name;	
	private UserDTO owner;
	private Date departureDate;
	private Date returnDate;
	
	private List<Components_HelperDTO> travelComponents;
	
	public PersonalizedTravelPackageDTO() {
		super();
		travelComponents = new ArrayList<Components_HelperDTO>();
	}   
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}   
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}   
	
	public UserDTO getOwner() {
		return this.owner;
	}

	public void setOwner(UserDTO owner) {
		this.owner = owner;
	}   

	public List<Components_HelperDTO> getTravelComponents() {
		return this.travelComponents;
	}
	
	public void setTravelComponents(List<Components_HelperDTO> travelComponents) {
		this.travelComponents = travelComponents;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
	public Date getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}
}
