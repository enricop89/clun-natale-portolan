package entities;

import entities.GiftList;
import entities.PersonalizedTravelPackage;
import entities.TravelComponent;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: GiftElements_Helper
 *
 */
@Entity

public class GiftElements_Helper implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private long id;	

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="GIFT_LIST_ID")	
	private GiftList giftList;	
	
	private PersonalizedTravelPackage personalizedTravelPackage;
	private TravelComponent travelComponent;  

	public GiftElements_Helper() {
		super();
	}   
	public PersonalizedTravelPackage getPersonalizedTravelPackage() {
		return this.personalizedTravelPackage;
	}

	public void setPersonalizedTravelPackage(PersonalizedTravelPackage personalizedTravelPackage) {
		this.personalizedTravelPackage = personalizedTravelPackage;
	}   
	public TravelComponent getTravelComponent() {
		return this.travelComponent;
	}

	public void setTravelComponent(TravelComponent travelComponent) {
		this.travelComponent = travelComponent;
	}   
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}   
	public GiftList getGiftList() {
		return this.giftList;
	}

	public void setGiftList(GiftList giftList) {
		this.giftList = giftList;
	}
   
}
