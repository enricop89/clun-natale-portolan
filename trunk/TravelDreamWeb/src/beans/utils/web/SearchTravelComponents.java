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
		travelComponentsList = data.getTravelComponentsList();
		instance = data.getInstance();
	}
	
	public List<TravelComponentDTO> getTravelComponentsList(){
		return travelComponentsList;
	}
	
	public void selectFromDialog(TravelComponentDTO travelComponent){
		if(instance != null)
			instance.closeDialog(travelComponent);
			
		else
			RequestContext.getCurrentInstance().closeDialog(travelComponent); 
	}
	
	public void visualize(TravelComponentDTO travelComponent){
		List<TravelComponentDTO> toSend = new ArrayList<TravelComponentDTO>();
		toSend.add(travelComponent);
		data.setTravelComponentsList(toSend);
		RequestContext.getCurrentInstance().openDialog("/index.xhtml"); //TODO: Waiting for Travel Component page. NB: MUST CHECK IF THIS WORKS, PRIMEFACES DOES NOT SUPPORT NESTED DIALOGS!
	}
}