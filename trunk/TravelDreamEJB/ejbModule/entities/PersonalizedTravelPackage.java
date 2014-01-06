package entities;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: PersonalizedTravelPackage
 *
 */
@Entity

public class PersonalizedTravelPackage implements Serializable {

	   
	@Id
	private long id; @GeneratedValue(strategy=GenerationType.IDENTITY)
	private String name;
	private String owner;
	private String description;
	private static final long serialVersionUID = 1L;

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
	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}   
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
   
}
