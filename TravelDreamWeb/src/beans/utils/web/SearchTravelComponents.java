package beans.utils.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;

import beans.travelcomponent.TravelComponentDTO;

@ManagedBean(name="SearchTravelComponents")
@ViewScoped
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
	private RequestContext instance;
	
	@PostConstruct
	public void init(){
		instance = data.getInstance();
		travelComponentsList = data.getTravelComponentsList();
	}
	
	public List<TravelComponentDTO> getTravelComponentsList(){
		return travelComponentsList;
	}
	
	public void selectFromDialog(TravelComponentDTO travelComponent){
		if(instance != null)
			RequestContext.setCurrentInstance(instance);
		
		RequestContext.getCurrentInstance().closeDialog(travelComponent); 
	}
	
	public void visualize(TravelComponentDTO travelComponent){
		List<TravelComponentDTO> toSend = new ArrayList<TravelComponentDTO>();
		toSend.add(travelComponent);
		data.setTravelComponentsList(toSend);
		RequestContext.getCurrentInstance().openDialog("/index.xhtml"); //TODO: Waiting for Travel Component page
	}
}