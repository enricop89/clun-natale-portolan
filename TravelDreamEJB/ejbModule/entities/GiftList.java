package entities;

import java.io.Serializable;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: GiftList
 *
 */
@Entity

public class GiftList implements Serializable {
	private static final long serialVersionUID = 1L;
	   
	@Id
	private User owner;

	//GiftElements (list of couples of references to a PersonalizedTravelPackage and a TravelComponent; both cannot be null)
	//STESSO PROBLEMA DI PERSONALIZED TRAVEL PACKAGE: COME LO IMPLEMENTIAMO?
	
	public GiftList() {
		super();
	}   
	public User getOwner() {
		return this.owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
   
}
