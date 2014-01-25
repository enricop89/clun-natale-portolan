package entities;

import java.io.Serializable;
import java.sql.Date;
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
@NamedQueries({
	@NamedQuery(name=PersonalizedTravelPackage.FIND_ALL,
				query="SELECT p FROM PersonalizedTravelPackage p")
})
public class PersonalizedTravelPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final String FIND_ALL = "PersonalizedTravelPackage.findAll";
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id; 
	
	private String name;
	private Date departureDate;
	private Date returnDate;
	
	@JoinColumn(nullable = false)
	private User owner;

	@OneToMany(mappedBy="personalizedTravelPackage")
	private List<Components_Helper> travelComponents;
	
	public PersonalizedTravelPackage() {
		super();
	}   
	public PersonalizedTravelPackage(PersonalizedTravelPackageDTO personalizedTravelPackage, Search search) {		
		this.id = personalizedTravelPackage.getId();
		this.name = personalizedTravelPackage.getName();
		this.owner = search.findUser(personalizedTravelPackage.getOwner());
		this.returnDate = personalizedTravelPackage.getReturnDate();
		this.departureDate = personalizedTravelPackage.getDepartureDate();
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
