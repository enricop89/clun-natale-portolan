package beans.utils.web;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import beans.travelcomponent.TravelComponentDTO;
import beans.utils.SearchDTOInterface;

@ManagedBean(name="TravelComponentWeb")
@RequestScoped
public class TravelComponentWeb 
{
	@EJB
	private SearchDTOInterface finder;
	
	@Inject
	private Data_Exchange data;
	public Data_Exchange getData(){
		return data;
	}
	public void setData(Data_Exchange data){
		this.data = data;
	}
	
	private TravelComponentDTO component;
	
	public TravelComponentWeb()
	{
		
	}
	
	@PostConstruct
	public void init(){
		List<TravelComponentDTO> res = data.getTravelComponentsList();
		if(!res.isEmpty())
			this.component = res.get(0);
	}

	public TravelComponentDTO getComponent() {
		return component;
	}

	public void setComponent(TravelComponentDTO component) {
		this.component = component;
	}
}