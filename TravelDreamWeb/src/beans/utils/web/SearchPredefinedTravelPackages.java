package beans.utils.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;

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
	
	public void visualize(PredefinedTravelPackageDTO predefinedTravelPackage){
		Map<Boolean, PredefinedTravelPackageDTO> toSend = new HashMap<>();
		toSend.put(false, predefinedTravelPackage);
		RequestContext.getCurrentInstance().closeDialog(toSend); 
	}
	public void delete(PredefinedTravelPackageDTO predefinedTravelPackage){
		Map<Boolean, PredefinedTravelPackageDTO> toSend = new HashMap<>();
		toSend.put(true, predefinedTravelPackage);
		RequestContext.getCurrentInstance().closeDialog(toSend); 
	}
}
