package beans.utils.web;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
//import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import beans.employeehandler.EmployeeHandlerInterface;
import beans.travelcomponent.TravelComponentDTO;
import beans.utils.SearchDTOInterface;

@ManagedBean(name="TravelComponentWeb")
@ViewScoped
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
	
	private java.util.Date flightDepartureDate;
	private java.util.Date flightArrivalDate;	
	private java.util.Date excursionDate;
	private java.util.Date hotelDate;
	
	public TravelComponentWeb()
	{
		
	}
	
	@PostConstruct
	public void init(){
		List<TravelComponentDTO> res = data.getTravelComponentsList();
		if(!res.isEmpty())
		{
			this.component = res.get(0);
			if (component.getHotelDate() != null)
				this.hotelDate = new java.util.Date(component.getHotelDate().getTime());
		}
	}

	public TravelComponentDTO getComponent() {
		return component;
	}

	public void setComponent(TravelComponentDTO component) {
		this.component = component;
	}
	
	public java.util.Date getFlightDepartureDate() {
		return flightDepartureDate;
	}
	public void setFlightDepartureDate(java.util.Date flightDepartureDate) {
		this.flightDepartureDate = flightDepartureDate;
	}
	public java.util.Date getFlightArrivalDate() {
		return flightArrivalDate;
	}
	public void setFlightArrivalDate(java.util.Date flightArrivalDate) {
		this.flightArrivalDate = flightArrivalDate;
	}
	public java.util.Date getExcursionDate() {
		return excursionDate;
	}
	public void setExcursionDate(java.util.Date excursionDate) {
		this.excursionDate = excursionDate;
	}
	public java.util.Date getHotelDate() {
		return hotelDate;
	}
	public void setHotelDate(java.util.Date hotelDate) {
		this.hotelDate = hotelDate;
	}
	public String saveChanges()
	{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		
		if (hotelDate != null)
			component.setHotelDate(new java.sql.Date(hotelDate.getTime()));		
		//component.setExcursionDateTime(new java.sql.Timestamp());
		//component.setHotelDate(new java.sql.Date(hotelDate.getTime()));
		
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