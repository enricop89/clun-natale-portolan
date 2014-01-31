package beans.utils.web;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;

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
	
	@PostConstruct
	public void init(){
		List<TravelComponentDTO> res = data.getTravelComponentsList();
		if(!res.isEmpty())
		{
			this.component = res.get(0);
			if (component.getHotelDate() != null)
				this.hotelDate = new java.util.Date(component.getHotelDate().getTime());
			if (component.getFlightDepartureDateTime() != null)
				this.flightDepartureDate = new java.util.Date(component.getFlightDepartureDateTime().getTime());
			if (component.getFlightArrivalDateTime() != null)
				this.flightArrivalDate = new java.util.Date(component.getFlightArrivalDateTime().getTime());
			if (component.getExcursionDateTime() != null)
				this.excursionDate = new java.util.Date(component.getExcursionDateTime().getTime());
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
	public void saveChanges()
	{		
		if (hotelDate != null)
			component.setHotelDate(new java.sql.Date(hotelDate.getTime()));	
		if (flightDepartureDate != null)
			component.setFlightDepartureDateTime(new java.sql.Timestamp(flightDepartureDate.getTime()));
		if (flightArrivalDate != null)
			component.setFlightArrivalDateTime(new java.sql.Timestamp(flightArrivalDate.getTime()));
		if (excursionDate != null)
			component.setExcursionDateTime(new java.sql.Timestamp(excursionDate.getTime()));
		
		boolean result = employeeHandler.updateTravelComponent(component);		
		data.setResult(result);
		RequestContext.getCurrentInstance().execute("window.top.location.reload();");
	}
}