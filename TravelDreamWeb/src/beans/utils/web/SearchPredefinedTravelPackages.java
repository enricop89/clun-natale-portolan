package beans.utils.web;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import beans.travelpackage.PredefinedTravelPackageDTO;

@ManagedBean(name="SearchPredefinedTravelPackages")
@RequestScoped
public class SearchPredefinedTravelPackages {
	@ManagedProperty(value = "#{Data_Exchange}")
	private Data_Exchange data;
	
	private List<PredefinedTravelPackageDTO> predefinedTravelPackagesList;
	
	@PostConstruct
	public void init(){
		predefinedTravelPackagesList = data.getPredefinedTravelPackagesList();
	}
	
	public Data_Exchange getData(){
		return data;
	}
	public void setData(Data_Exchange data){
		this.data = data;
	}
	
	public List<PredefinedTravelPackageDTO> getPredefinedTravelPackagesList(){
		return predefinedTravelPackagesList;
	}
	
	public void selectFromDialog(PredefinedTravelPackageDTO predefinedTravelPackage){
		// open up the predefinedTravelPackage page
		// TODO: waiting for TravelPackage page
	}

}
