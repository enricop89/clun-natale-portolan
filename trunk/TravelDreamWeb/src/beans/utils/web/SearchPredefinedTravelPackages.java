package beans.utils.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import beans.travelpackage.PredefinedTravelPackageDTO;

@ManagedBean(name="SearchPredefinedTravelPackages")
@RequestScoped
public class SearchPredefinedTravelPackages {
	@ManagedProperty(value = "#{SearchWeb}")
	private SearchWeb searchWeb;
	
	private List<PredefinedTravelPackageDTO> predefinedTravelPackagesList;
	
	@PostConstruct
	public void init(){
		predefinedTravelPackagesList = new ArrayList<PredefinedTravelPackageDTO>(searchWeb.getPredefinedTravelPackagesList());
	}
	
	public SearchWeb getSearchWeb(){
		return searchWeb;
	}
	public void setSearchWeb(SearchWeb searchWeb){
		this.searchWeb = searchWeb;
	}
	
	public List<PredefinedTravelPackageDTO> getPredefinedTravelPackagesList(){
		return predefinedTravelPackagesList;
	}
	
	public void selectFromDialog(PredefinedTravelPackageDTO predefinedTravelPackage){
		// open up the predefinedTravelPackage page
	}

}
