package entities;

import java.io.Serializable;
import java.lang.String;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: PredefinedTravelPackage
 *
 */
@Entity

public class PredefinedTravelPackage implements Serializable {

	   
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	private String description;
	private static final long serialVersionUID = 1L;

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
   
}
