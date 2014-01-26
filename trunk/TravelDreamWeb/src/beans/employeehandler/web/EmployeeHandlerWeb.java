package beans.employeehandler.web;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.primefaces.event.TabChangeEvent;

import beans.employeehandler.EmployeeHandlerInterface;
import beans.travelcomponent.*;
import beans.travelpackage.*;
import beans.utils.web.Data_Exchange;

@ManagedBean(name="EmployeeHandlerWeb")
@ViewScoped
public class EmployeeHandlerWeb  {	
	@Inject
	private Data_Exchange data;
	public Data_Exchange getData(){
		return data;
	}
	public void setData(Data_Exchange data){
		this.data = data;
	}
		
	@EJB
	private EmployeeHandlerInterface employeeHandler;

	//Travel package creation data
	private PredefinedTravelPackageDTO packageDTO;
	
	private java.util.Date packageStartDate;
	private java.util.Date packageEndDate;
	
	//Travel component creation data
	private TravelComponentDTO componentDTO;
	
	private java.util.Date flightDepartureDate;
	private String flightDepartureTime;
	private java.util.Date flightArrivalDate;
	private String flightArrivalTime;
	
	private java.util.Date excursionDate;
	private String excursionTime;
	
	private java.util.Date hotelStartingDate;
	private java.util.Date hotelEndingDate;
	
	private int activePanel;
			
	@PostConstruct
	public void init()
	{
		componentDTO = new TravelComponentDTO();
		List<PredefinedTravelPackageDTO> res = data.getPredefinedTravelPackagesList();
		if(res.isEmpty())
			packageDTO = new PredefinedTravelPackageDTO();
		else{
			packageDTO = res.get(0);
			activePanel = 1;
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
	
	public java.util.Date getHotelStartingDate() {
		return hotelStartingDate;
	}
	public void setHotelStartingDate(java.util.Date hotelStartingDate) {
		this.hotelStartingDate = hotelStartingDate;
	}
	public java.util.Date getHotelEndingDate() {
		return hotelEndingDate;
	}
	public void setHotelEndingDate(java.util.Date hotelEndingDate) {
		this.hotelEndingDate = hotelEndingDate;
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
		
		boolean result = false;
		
		if(hotelStartingDate != null && hotelEndingDate != null){
			Calendar start = Calendar.getInstance();
			start.setTime(hotelStartingDate);
			Calendar end = Calendar.getInstance();
			end.setTime(hotelEndingDate);
			for (java.util.Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
				componentDTO.setHotelDate(new Date(date.getTime()));
				result = employeeHandler.addNewTravelComponent(componentDTO);
				if(result == false)
				{
					facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Fatal Error", "An unexpected error has occurred. The travel components may have not been correctly created."));
					return "/employee/control_panel.html?faces-redirect=true";
				}
			}
		}
		else{
			if(hotelStartingDate != null){
				componentDTO.setHotelDate(new java.sql.Date(hotelStartingDate.getTime()));
			}
			else if(hotelEndingDate != null){
				componentDTO.setHotelDate(new java.sql.Date(hotelStartingDate.getTime()));
			}

			else{
				facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Unable to add the travel component."));
				return "/employee/control_panel.html?faces-redirect=true";
			}
			
			result = employeeHandler.addNewTravelComponent(componentDTO);
		}
		
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
	
	public java.util.Date getPackageStartDate() {
		return packageStartDate;
	}

	public void setPackageStartDate(java.util.Date packageStartDate) {
		this.packageStartDate = packageStartDate;
	}

	public java.util.Date getPackageEndDate() {
		return packageEndDate;
	}

	public void setPackageEndDate(java.util.Date packageEndDate) {
		this.packageEndDate = packageEndDate;
	}

	public PredefinedTravelPackageDTO getPackageDTO() {
		return packageDTO;
	}

	public void setPackageDTO(PredefinedTravelPackageDTO packageDTO) {
		this.packageDTO = packageDTO;
	}
	
	public void deleteComponentFromPackage(TravelComponentDTO component) throws IOException
	{	
		FacesMessage message = null;
		boolean result = employeeHandler.removeTravelComponentFromPredefinedTravelPackage(packageDTO, component);
		if(result == true){
			message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "Travel component deleted"); 
		}
		else{
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Some error occured");
		}
		
		activePanel = 1;
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
		toSend.add(packageDTO);
		data.setPredefinedTravelPackagesList(toSend);
				
		facesContext.addMessage(null,message);
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/employee/control_panel.xhtml");
	}
	
	public void addComponentToPackage(TravelComponentDTO component) throws IOException
	{
		FacesMessage message = null;
		boolean result = employeeHandler.addTravelComponentToPredefinedTravelPackage(packageDTO, component);
		if(result == true){
			message = new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have added the component to the package"); 
		}
		else{
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "The component is already in the travel package!");
		}	
		
		activePanel = 1;
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
		toSend.add(packageDTO);
		data.setPredefinedTravelPackagesList(toSend);
		
		facesContext.addMessage(null,message);	
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/employee/control_panel.xhtml");
	}
	
	public int getActivePanel() {
			return activePanel;
	}
	
	public void setActivePanel(int activePanel) {
		this.activePanel = activePanel;
	}
	
	public String getDescription(TravelComponentDTO component)
	{
		String result = new String();
		switch (component.getType())
		{
			case FLIGHT:
				result = component.getFlightCode() + " - FROM: " + component.getFlightDepartureCity() + " ("+ component.getFlightDepartureDateTime() + ") - TO: " + component.getFlightArrivalCity() + " (" + component.getFlightArrivalDateTime() + ")";
				break;
			case HOTEL:
				result = "CITY: " + component.getHotelCity() + " - DATE: " + DateFormatUtils.format(component.getHotelDate(), "yyyy-MM-dd");
				break;
			case EXCURSION:
				result = "CITY: " + component.getExcursionCity() + " - DATE: " + component.getExcursionDateTime();
				break;
		}
		return result;
	}
	
	public void createPackage()
	{
		packageDTO.setDepartureDate(new java.sql.Date(packageStartDate.getTime()));
		packageDTO.setReturnDate(new java.sql.Date(packageEndDate.getTime()));
		String result = employeeHandler.addNewPredefinedTravelPackage(packageDTO);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (result.isEmpty())
		{
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "Package correctly added."));
			packageDTO = new PredefinedTravelPackageDTO();
			packageStartDate = null;
			packageEndDate = null;
		}
		else
		{
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "The package is empty or did not pass the consistency check. Check if the dates and the cities are consistent and retry."));
		}
	}
	
	public void onTabChange(TabChangeEvent event){
		FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove("SearchTravelComponents");
	}
	public void onClose(){
		FacesContext.getCurrentInstance().getViewRoot().getViewMap().remove("SearchTravelComponents");
	}
}