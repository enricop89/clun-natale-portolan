package entities;

import java.io.Serializable;
import java.lang.String;
import java.sql.Date;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: TravelElement
 *
 */
@Entity

public class TravelElement implements Serializable {

	   
	@Id
	private long id;
	private long componentId;
	private String owner;
	private Date confirmationDateTime;
	private static final long serialVersionUID = 1L;

	public TravelElement() {
		super();
	}   
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}   
	public long getComponentId() {
		return this.componentId;
	}

	public void setComponentId(long componentId) {
		this.componentId = componentId;
	}   
	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}   
	public Date getConfirmationDateTime() {
		return this.confirmationDateTime;
	}

	public void setConfirmationDateTime(Date confirmationDateTime) {
		this.confirmationDateTime = confirmationDateTime;
	}
   
}
