package entities;

import java.io.Serializable;
import java.lang.String;

/**
 * ID class for entity: PersonalizedTravelPackage
 *
 */ 
public class PersonalizedTravelPackagePK  implements Serializable {   
   
	         
	private String name;         
	private String owner;
	private static final long serialVersionUID = 1L;

	public PersonalizedTravelPackagePK() {}

	

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
	
   
	/*
	 * @see java.lang.Object#equals(Object)
	 */	
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof PersonalizedTravelPackagePK)) {
			return false;
		}
		PersonalizedTravelPackagePK other = (PersonalizedTravelPackagePK) o;
		return true
			&& (getName() == null ? other.getName() == null : getName().equals(other.getName()))
			&& (getOwner() == null ? other.getOwner() == null : getOwner().equals(other.getOwner()));
	}
	
	/*	 
	 * @see java.lang.Object#hashCode()
	 */	
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (getName() == null ? 0 : getName().hashCode());
		result = prime * result + (getOwner() == null ? 0 : getOwner().hashCode());
		return result;
	}
   
   
}
