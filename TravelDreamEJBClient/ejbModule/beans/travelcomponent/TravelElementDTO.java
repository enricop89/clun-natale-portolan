package beans.travelcomponent;

import java.sql.Timestamp;

import beans.accountmanagement.UserDTO;

public class TravelElementDTO {
	private long id;
	private TravelComponentDTO travelComponent;	
	private UserDTO owner;
	private Timestamp confirmationDateTime;

	public TravelElementDTO() {
		super();
	}  
	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}   
	public TravelComponentDTO getTravelComponent() {
		return this.travelComponent;
	}

	public void setTravelComponent(TravelComponentDTO travelComponent) {
		this.travelComponent = travelComponent;
	}   
	public UserDTO getOwner() {
		return this.owner;
	}

	public void setOwner(UserDTO owner) {
		this.owner = owner;
	}   
	public Timestamp getConfirmationDateTime() {
		return this.confirmationDateTime;
	}

	public void setConfirmationDateTime(Timestamp confirmationDateTime) {
		this.confirmationDateTime = confirmationDateTime;
	}
   
}
