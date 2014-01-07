package entities;

import java.io.Serializable;
import java.util.List;
import java.lang.String;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: PersonalizedTravelPackage
 *
 */
@Entity

public class PersonalizedTravelPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	   
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id; 
	
	private String name;
	
	@Column(nullable = false)
	private User owner;

	private String description;

	//Components (list of couple of references to TravelComponent and TravelElement; the reference to TravelElement may be null)
	// COME LO IMPLEMENTIAMO?
	
	public PersonalizedTravelPackage() {
		super();
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
	
	public User getOwner() {
		return this.owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}   
	
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
   
}
