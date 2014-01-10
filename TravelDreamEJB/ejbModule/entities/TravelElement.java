package entities;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: TravelElement
 *
 */
@Entity

public class TravelElement implements Serializable {
	private static final long serialVersionUID = 1L;
	   
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="TRAVEL_COMPONENT_ID")
	private TravelComponent travelComponent;
	
	private User owner;
	private Timestamp confirmationDateTime;

	public TravelElement() {
		super();
	}  
	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}   
	public TravelComponent getTravelComponent() {
		return this.travelComponent;
	}

	public void setTravelComponent(TravelComponent travelComponent) {
		this.travelComponent = travelComponent;
	}   
	public User getOwner() {
		return this.owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}   
	public Timestamp getConfirmationDateTime() {
		return this.confirmationDateTime;
	}

	public void setConfirmationDateTime(Timestamp confirmationDateTime) {
		this.confirmationDateTime = confirmationDateTime;
	}
   
}
