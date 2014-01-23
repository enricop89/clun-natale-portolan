package beans.employeehandler.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import beans.employeehandler.EmployeeHandlerInterface;
import beans.travelcomponent.*;
import beans.travelpackage.*;
import beans.utils.web.Data_Exchange;

@ManagedBean(name="EmployeeHandlerWeb")
@ViewScoped
public class EmployeeHandlerWeb  {
	
	@EJB
	private EmployeeHandlerInterface employeeHandler;

	//Travel package creation data
	private PredefinedTravelPackageDTO packageDTO;
	
	private java.util.Date packageDepDate;
	private java.util.Date packageArrDate;
	
	//Travel component creation data
	private TravelComponentDTO componentDTO;
	
	private java.util.Date flightDepartureDate;
	private String flightDepartureTime;
	private java.util.Date flightArrivalDate;
	private String flightArrivalTime;
	
	private java.util.Date excursionDate;
	private String excursionTime;
	
	private java.util.Date hotelDate;
	
	private boolean redirected;
	private int activePanel;
	
	@Inject
	private Data_Exchange data;
	public Data_Exchange getData(){
		return data;
	}
	public void setData(Data_Exchange data){
		this.data = data;
	}
		
	public EmployeeHandlerWeb()
	{
		componentDTO = new TravelComponentDTO();
		setPackageDTO(new PredefinedTravelPackageDTO());
		redirected = false;
	}
	
	@PostConstruct
	public void init(){
		if (data.getPredefinedTravelPackagesList() != null && !data.getPredefinedTravelPackagesList().isEmpty())
		{
			packageDTO = data.getPredefinedTravelPackagesList().get(0);
			redirected = true;
		}
	}
	
	public TravelComponentDTO getComponentDTO()
	{
		return componentDTO;
	}
	
	public void setComponentDTO(TravelComponentDTO componentDTO)
	{
		this.componentDTO = componentDTO;
	}
	
	public java.util.Date getFlightDepartureDate()
	{
		return flightDepartureDate;
	}
	
	public void setFlightDepartureDate(java.util.Date flightDepartureDate)
	{
		this.flightDepartureDate = flightDepartureDate;
	}
	
	public java.util.Date getFlightArrivalDate()
	{
		return flightArrivalDate;
	}
	
	public void setFlightArrivalDate(java.util.Date flightArrivalDate)
	{
		this.flightArrivalDate = flightArrivalDate;
	}
	
	public String getFlightDepartureTime()
	{
		return this.flightDepartureTime;
	}
	
	public void setFlightDepartureTime(String flightDepartureTime)
	{
		this.flightDepartureTime = flightDepartureTime;
	}
	
	public String getFlightArrivalTime()
	{
		return this.flightArrivalTime;
	}
	
	public void setFlightArrivalTime(String flightArrivalTime)
	{
		this.flightArrivalTime = flightArrivalTime;
	}

	public java.util.Date getExcursionDate() {
		return excursionDate;
	}

	public void setExcursionDate(java.util.Date excursionDate) {
		this.excursionDate = excursionDate;
	}

	public String getExcursionTime() {
		return excursionTime;
	}

	public void setExcursionTime(String excursionTime) {
		this.excursionTime = excursionTime;
	}
	
	public String createExcursion()
	{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		
		componentDTO.setType(ComponentType.EXCURSION);
		
		componentDTO.setFlightArrivalDateTime(null);
		componentDTO.setFlightArrivalCity(null);
		componentDTO.setFlightCode(null);
		componentDTO.setFlightDepartureCity(null);
		componentDTO.setFlightDepartureDateTime(null);
		componentDTO.setHotelCity(null);
		componentDTO.setHotelDate(null);	

		String[] timeSplit = excursionTime.split(":");
		int HH = Integer.parseInt(timeSplit[0]);
		int MM = Integer.parseInt(timeSplit[1]);
		
		componentDTO.setExcursionDateTime(new java.sql.Timestamp(excursionDate.getTime() +  ((HH * 60) + MM) * 60 * 1000  ));

		boolean result = employeeHandler.addNewTravelComponent(componentDTO);
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
	
	public String createFlight()
	{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		
		componentDTO.setType(ComponentType.FLIGHT);
		
		componentDTO.setHotelCity(null);
		componentDTO.setHotelDate(null);
		componentDTO.setExcursionCity(null);
		componentDTO.setExcursionDateTime(null);
		componentDTO.setExcursionDescription(null);
		
		String[] arrivalSplit = flightArrivalTime.split(":");
		int arrivalHH = Integer.parseInt(arrivalSplit[0]);
		int arrivalMM = Integer.parseInt(arrivalSplit[1]);
		
		componentDTO.setFlightArrivalDateTime(new java.sql.Timestamp(flightArrivalDate.getTime() + ((arrivalHH * 60) + arrivalMM) * 60 * 1000 ));

		String[] departureSplit = flightDepartureTime.split(":");
		int departureHH = Integer.parseInt(departureSplit[0]);
		int departureMM = Integer.parseInt(departureSplit[1]);

		componentDTO.setFlightDepartureDateTime(new java.sql.Timestamp(flightDepartureDate.getTime() + ((departureHH * 60) + departureMM) * 60 * 1000));		

		boolean result = employeeHandler.addNewTravelComponent(componentDTO);
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
	
	public String createHotel()
	{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		
		componentDTO.setType(ComponentType.HOTEL);
		
		componentDTO.setFlightArrivalDateTime(null);
		componentDTO.setFlightArrivalCity(null);
		componentDTO.setFlightCode(null);
		componentDTO.setFlightDepartureCity(null);
		componentDTO.setFlightDepartureDateTime(null);
		
		componentDTO.setExcursionCity(null);
		componentDTO.setExcursionDateTime(null);
		componentDTO.setExcursionDescription(null);
		
		componentDTO.setHotelDate(new java.sql.Date(hotelDate.getTime()));
		boolean result = employeeHandler.addNewTravelComponent(componentDTO);
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

	public java.util.Date getHotelDate() {
		return hotelDate;
	}

	public void setHotelDate(java.util.Date hotelDate) {
		this.hotelDate = hotelDate;
	}

	public java.util.Date getPackageDepDate() {
		return packageDepDate;
	}

	public void setPackageDepDate(java.util.Date packageDepDate) {
		this.packageDepDate = packageDepDate;
	}

	public java.util.Date getPackageArrDate() {
		return packageArrDate;
	}

	public void setPackageArrDate(java.util.Date packageArrDate) {
		this.packageArrDate = packageArrDate;
	}

	public PredefinedTravelPackageDTO getPackageDTO() {
		return packageDTO;
	}

	public void setPackageDTO(PredefinedTravelPackageDTO packageDTO) {
		this.packageDTO = packageDTO;
	}
	
	public void deleteComponent(TravelComponentDTO component)
	{
		employeeHandler.removeTravelComponentFromPredefinedTravelPackage(packageDTO, component);
		
	}

	public void addComponent()
	{
		RequestContext.getCurrentInstance().openDialog("/misc/search/search_travelcomponent_to_add.xhtml");
	}
	
	public void onTravelComponentChosen(SelectEvent event) throws IOException
	{
		TravelComponentDTO travelComponent = (TravelComponentDTO) event.getObject(); 
		employeeHandler.addTravelComponentToPredefinedTravelPackage(packageDTO, travelComponent);
		
		List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
		toSend.add(packageDTO);
		data.setPredefinedTravelPackagesList(toSend);
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/employee/control_panel.xhtml");
	}
	
	public int getActivePanel() {
		if (redirected == true)
		{
			return 1;
		}
		else
		{
			return activePanel;
		}
	}
	
	public void setActivePanel(int activePanel) {
		this.activePanel = activePanel;
	}
}