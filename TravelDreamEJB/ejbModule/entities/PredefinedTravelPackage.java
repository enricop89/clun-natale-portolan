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

	   
	@Id
	private String name;
	private String descritpion;
	private static final long serialVersionUID = 1L;

	public PredefinedTravelPackage() {
		super();
	}   
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}   
	public String getDescritpion() {
		return this.descritpion;
	}

	public void setDescritpion(String descritpion) {
		this.descritpion = descritpion;
	}
   
}
