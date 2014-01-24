package entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;
import java.lang.String;

import javax.persistence.*;

import beans.travelpackage.PredefinedTravelPackageDTO;
import beans.utils.Search;

/**
 * Entity implementation class for Entity: PredefinedTravelPackage
 *
 */
@Entity
@NamedQueries({
	@NamedQuery(name=PredefinedTravelPackage.FIND_ALL,
				query="SELECT p FROM PredefinedTravelPackage p")
})
public class PredefinedTravelPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	   
	public static final String FIND_ALL = "PredefinedTravelPackage.findAll";
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String name;
	private String description;
	private Date departureDate;
	private Date returnDate;
	
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
	public PredefinedTravelPackage(PredefinedTravelPackageDTO predefinedTravelPackage, Search search){		
		this.id = predefinedTravelPackage.getId();
		this.name = predefinedTravelPackage.getName();
		this.description = predefinedTravelPackage.getDescription();
		this.returnDate = predefinedTravelPackage.getReturnDate();
		this.departureDate = predefinedTravelPackage.getDepartureDate();
		for(int i = 0; i < predefinedTravelPackage.getTravelComponents().size(); i++)	
			travelComponents.add(search.findTravelComponent(predefinedTravelPackage.getTravelComponents().get(i)));
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
	public Date getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(Date departureDate) {
		this.departureDate = departureDate;
	}
	public Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}
}
