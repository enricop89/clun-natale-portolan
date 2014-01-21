package beans.utils.web;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;

import beans.travelcomponent.TravelComponentDTO;;

@ManagedBean(name="SearchTravelComponents")
@RequestScoped
public class SearchTravelComponents {
	@Inject
	private Data_Exchange data;
	public Data_Exchange getData(){
		return data;
	}
	public void setData(Data_Exchange data){
		this.data = data;
	}
	
	private List<TravelComponentDTO> travelComponentsList;
	
	@PostConstruct
	public void init(){
		travelComponentsList = data.getTravelComponentsList();
	}
	
	public List<TravelComponentDTO> getTravelComponentsList(){
		return travelComponentsList;
	}
	
	public void selectFromDialog(TravelComponentDTO travelComponent){
		// open up the travelComponent page
		// TODO: waiting for TravelComponent page
	}
}
