package beans.travelpackage;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import beans.accountmanagement.UserDTO;

public class PersonalizedTravelPackageDTO {
	private long id; 	
	@NotEmpty
	private String name;
	
	private UserDTO owner;
	private List<Components_HelperDTO> travelComponents;
	
	public PersonalizedTravelPackageDTO() {
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
	
	public UserDTO getOwner() {
		return this.owner;
	}

	public void setOwner(UserDTO owner) {
		this.owner = owner;
	}   

	public List<Components_HelperDTO> getTravelComponents() {
		return this.travelComponents;
	}
	
	public void setTravelComponents(List<Components_HelperDTO> travelComponents) {
		this.travelComponents = travelComponents;
	}
}
