package beans.utils.web;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import beans.travelpackage.PredefinedTravelPackageDTO;

@ManagedBean(name="SearchPredefinedTravelPackages")
@ViewScoped
public class SearchPredefinedTravelPackages {
	@Inject
	private Data_Exchange data;
	public Data_Exchange getData(){
		return data;
	}
	public void setData(Data_Exchange data){
		this.data = data;
	}
	
	private List<PredefinedTravelPackageDTO> predefinedTravelPackagesList;
	
	@PostConstruct
	public void init(){
		predefinedTravelPackagesList = data.getPredefinedTravelPackagesList();
	}
	
	public List<PredefinedTravelPackageDTO> getPredefinedTravelPackagesList(){
		return predefinedTravelPackagesList;
	}
	
	public void selectFromDialog(PredefinedTravelPackageDTO predefinedTravelPackage){
		// open up the predefinedTravelPackage page
		// TODO: waiting for TravelPackage page
	}

}
