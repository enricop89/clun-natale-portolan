package entities;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: GiftList
 *
 */
@Entity

public class GiftList implements Serializable {

	   
	@Id
	private String owner;
	private static final long serialVersionUID = 1L;

	public GiftList() {
		super();
	}   
	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
   
}
