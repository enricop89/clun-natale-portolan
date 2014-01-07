package entities;

import java.io.Serializable;
import java.util.List;
import java.lang.String;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: PredefinedTravelPackage
 *
 */
@Entity

public class PredefinedTravelPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	   
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String name;
	private String description;

	@ManyToMany
	@JoinTable(
		name="PREDEFINED_COMPONENT"
		, joinColumns={
			@JoinColumn(name="PREDEFINED_ID", referencedColumnName="ID")
			}
		, inverseJoinColumns={
			@JoinColumn(name="COMPONENT_ID", referencedColumnName="ID")
			}
		)	
	private List<TravelComponent> travelComponents;

	public PredefinedTravelPackage() {
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
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
 
	public List<TravelComponent> getTravelComponents() {
		return this.travelComponents;
	}
	
	public void setTravelComponents(List<TravelComponent> travelComponents) {
		this.travelComponents = travelComponents;
	}
}
