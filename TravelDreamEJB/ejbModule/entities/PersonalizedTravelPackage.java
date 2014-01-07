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

	@OneToMany(mappedBy="personalizedTravelPackage")
	private List<Components_Helper> travelComponents;
	
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

	public List<Components_Helper> getTravelComponents() {
		return this.travelComponents;
	}
	
	public void setTravelComponents(List<Components_Helper> travelComponents) {
		this.travelComponents = travelComponents;
	}
}
