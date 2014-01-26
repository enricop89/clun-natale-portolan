package beans.utils.web;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import beans.employeehandler.EmployeeHandlerInterface;
import beans.travelcomponent.TravelComponentDTO;
import beans.utils.SearchDTOInterface;

@ManagedBean(name="TravelComponentWeb")
@RequestScoped
public class TravelComponentWeb 
{
	@EJB
	private SearchDTOInterface finder;
	
	@EJB
	private EmployeeHandlerInterface employeeHandler;
	
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
	
	public String saveChanges()
	{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		boolean result = employeeHandler.updateTravelComponent(component);
		if (result == true)
		{
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "Travel component correctly added."));
			return "/employee/control_panel.html?faces-redirect=true";
		}
		else
		{
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Unable to add the travel component."));
			return "/employee/control_panel.html?faces-redirect=true";
		}
	}
}