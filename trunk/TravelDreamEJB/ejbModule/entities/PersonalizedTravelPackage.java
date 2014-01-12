package entities;

import java.io.Serializable;
import java.util.List;
import java.lang.String;

import javax.persistence.*;

import beans.travelpackage.PersonalizedTravelPackageDTO;
import beans.utils.Search;

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
	
	@JoinColumn(nullable = false)
	private User owner;

	@OneToMany(mappedBy="personalizedTravelPackage")
	private List<Components_Helper> travelComponents;
	
	public PersonalizedTravelPackage() {
		super();
	}   
	public PersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage) {
		Search search = new Search();
//		this.id = personalizedTravelPackage.getId();
		this.name = personalizedTravelPackage.getName();
		this.owner = search.findUser(personalizedTravelPackage.getOwner());
		for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
			travelComponents.add(search.findComponents_Helper(personalizedTravelPackage.getTravelComponents().get(i)));
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
