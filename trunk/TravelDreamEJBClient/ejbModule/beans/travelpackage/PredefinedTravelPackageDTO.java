package beans.travelpackage;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import beans.travelcomponent.TravelComponentDTO;

public class PredefinedTravelPackageDTO {
	private long id;	
	@NotEmpty
	private String name;
	@NotEmpty
	private String description;
	private List<TravelComponentDTO> travelComponents;

	public PredefinedTravelPackageDTO() {
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
 
	public List<TravelComponentDTO> getTravelComponents() {
		return this.travelComponents;
	}
	
	public void setTravelComponents(List<TravelComponentDTO> travelComponents) {
		this.travelComponents = travelComponents;
	}
}
