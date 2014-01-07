package entities;

import entities.TravelComponent;
import entities.TravelElement;

import java.io.Serializable;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Components_Helper
 *
 */
@Entity

public class Components_Helper implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="PERSONALIZED_TRAVEL_PACKAGE_ID")
	private PersonalizedTravelPackage personalizedTravelPackage;
	
	private TravelComponent travelComponent;   

	private TravelElement travelElement;

	public Components_Helper() {
		super();
	}
	
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
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
  
	public TravelElement getTravelElement() {
		return this.travelElement;
	}

	public void setTravelElement(TravelElement travelElement) {
		this.travelElement = travelElement;
	}
   
}
