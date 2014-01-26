package beans.utils.web;

import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import beans.travelcomponent.ComponentType;
import beans.travelcomponent.TravelComponentDTO;
import beans.utils.SearchDTOInterface;

@ManagedBean(name="SearchTravelComponents")
@ViewScoped
public class SearchTravelComponents {
	
	@EJB
	private SearchDTOInterface finder;
	
	private List<TravelComponentDTO> resultList;

	private TravelComponentDTO searchCriteria;
	private String flightSupplyingCompany;
	private String hotelSupplyingCompany;
	private String excursionSupplyingCompany;	
	private java.util.Date departureDate;
	private java.util.Date returnDate;
	private java.util.Date flightDepartureDateTime;
	private java.util.Date flightArrivalDateTime;
	private java.util.Date hotelStartingDate;
	private java.util.Date hotelEndingDate;
	private java.util.Date excursionDateTime;
	
	private boolean showFlightResult;
	private boolean showHotelResult;
	private boolean showExcursionResult;	

	public SearchTravelComponents()
	{
		searchCriteria = new TravelComponentDTO();
		flightSupplyingCompany = new String();
		hotelSupplyingCompany = new String();
		excursionSupplyingCompany = new String();
		showFlightResult = false;
	}
	
	public TravelComponentDTO getSearchCriteria() {
		return searchCriteria;
	}
	public void setSearchCriteria(TravelComponentDTO searchCriteria) {
		this.searchCriteria = searchCriteria;
	}
	public String getFlightSupplyingCompany() {
		return flightSupplyingCompany;
	}
	public void setFlightSupplyingCompany(String flightSupplyingCompany) {
		this.flightSupplyingCompany = flightSupplyingCompany;
	}
	public String getHotelSupplyingCompany() {
		return hotelSupplyingCompany;
	}
	public void setHotelSupplyingCompany(String hotelSupplyingCompany) {
		this.hotelSupplyingCompany = hotelSupplyingCompany;
	}
	public String getExcursionSupplyingCompany() {
		return excursionSupplyingCompany;
	}
	public void setExcursionSupplyingCompany(String excursionSupplyingCompany) {
		this.excursionSupplyingCompany = excursionSupplyingCompany;
	}
	public java.util.Date getDepartureDate() {
		return departureDate;
	}
	public void setDepartureDate(java.util.Date departureDate) {
		this.departureDate = departureDate;
	}
	public java.util.Date getReturnDate() {
		return returnDate;
	}
	public void setReturnDate(java.util.Date returnDate) {
		this.returnDate = returnDate;
	}
	public java.util.Date getFlightDepartureDateTime() {
		return flightDepartureDateTime;
	}
	public void setFlightDepartureDateTime(java.util.Date flightDepartureDateTime) {
		this.flightDepartureDateTime = flightDepartureDateTime;
	}
	public java.util.Date getFlightArrivalDateTime() {
		return flightArrivalDateTime;
	}
	public void setFlightArrivalDateTime(java.util.Date flightArrivalDateTime) {
		this.flightArrivalDateTime = flightArrivalDateTime;
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
	public java.util.Date getExcursionDateTime() {
		return excursionDateTime;
	}
	public void setExcursionDateTime(java.util.Date excursionDateTime) {
		this.excursionDateTime = excursionDateTime;
	}
	public List<TravelComponentDTO> getResultList() {
		return resultList;
	}
	public void setResultList(List<TravelComponentDTO> resultList) {
		this.resultList = resultList;
	}
	
	public boolean isShowFlightResult() {
		return showFlightResult;
	}

	public void setShowFlightResult(boolean showFlightResult) {
		this.showFlightResult = showFlightResult;
	}

	public boolean isShowHotelResult() {
		return showHotelResult;
	}

	public void setShowHotelResult(boolean showHotelResult) {
		this.showHotelResult = showHotelResult;
	}

	public boolean isShowExcursionResult() {
		return showExcursionResult;
	}

	public void setShowExcursionResult(boolean showExcursionResult) {
		this.showExcursionResult = showExcursionResult;
	}

	public void search(ComponentType type, boolean searchAll)
	{
		showFlightResult = false;
		showHotelResult = false;
		showExcursionResult = false;
		
		if (searchAll == true)
		{
			searchCriteria = new TravelComponentDTO();
			searchCriteria.setType(type);
			resultList = finder.findTravelComponent(searchCriteria);
		}
		else
		{
			searchCriteria.setType(type);
			switch(type){
			case FLIGHT:
				
				if(flightSupplyingCompany.isEmpty())
				{
					searchCriteria.setSupplyingCompany(null);
				}
				else
				{
					searchCriteria.setSupplyingCompany(flightSupplyingCompany);
				}
				if(flightDepartureDateTime != null)
					searchCriteria.setFlightDepartureDateTime(new Timestamp(flightDepartureDateTime.getTime()));		
				else
					searchCriteria.setFlightDepartureDateTime(null);
			
				if(flightArrivalDateTime != null)
					searchCriteria.setFlightArrivalDateTime(new Timestamp(flightArrivalDateTime.getTime()));	
				else
					searchCriteria.setFlightArrivalDateTime(null);
				
				if(searchCriteria.getFlightDepartureCity().isEmpty())
					searchCriteria.setFlightDepartureCity(null);
				
				if(searchCriteria.getFlightArrivalCity().isEmpty())
					searchCriteria.setFlightArrivalCity(null);
				
				if(searchCriteria.getFlightCode().isEmpty())
					searchCriteria.setFlightCode(null);
				
				resultList = finder.findTravelComponent(searchCriteria);

				break;
			case HOTEL:
				if(hotelSupplyingCompany.isEmpty())
				{
					searchCriteria.setSupplyingCompany(null);
				}
				else
				{
					searchCriteria.setSupplyingCompany(hotelSupplyingCompany);
				}
				if(searchCriteria.getHotelCity().isEmpty())
					searchCriteria.setHotelCity(null);
				if(hotelStartingDate != null && hotelEndingDate != null){
					Calendar start = Calendar.getInstance();
					start.setTime(hotelStartingDate);
					Calendar end = Calendar.getInstance();
					end.setTime(hotelEndingDate);
					resultList = new ArrayList<TravelComponentDTO>();
					for (java.util.Date date = start.getTime(); !start.after(end); start.add(Calendar.DATE, 1), date = start.getTime()) {
					    searchCriteria.setHotelDate(new Date(date.getTime()));
					    resultList.addAll(finder.findTravelComponent(searchCriteria));
					}
				}
				else{
					if(hotelStartingDate != null)
						searchCriteria.setHotelDate(new Date(hotelStartingDate.getTime()));
					else if(hotelEndingDate != null)
						searchCriteria.setHotelDate(new Date(hotelEndingDate.getTime()));
					
					resultList = finder.findTravelComponent(searchCriteria);
				}
			
				break;
			case EXCURSION:
				if(excursionSupplyingCompany.isEmpty())
				{
					searchCriteria.setSupplyingCompany(null);
				}
				else
				{
					searchCriteria.setSupplyingCompany(excursionSupplyingCompany);
				}
				if(searchCriteria.getExcursionCity().isEmpty())
					searchCriteria.setExcursionCity(null);
				
				if(searchCriteria.getExcursionDescription().isEmpty())
					searchCriteria.setExcursionDescription(null);
				
				if(excursionDateTime != null)
					searchCriteria.setExcursionDateTime(new Timestamp(excursionDateTime.getTime()));
				else
					searchCriteria.setExcursionDateTime(null);
				
				resultList = finder.findTravelComponent(searchCriteria);
				
				break;
			}
		}
		
		if(resultList == null || resultList.isEmpty())
		{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"No Results", "Your search has given no results"));
		}
		else
		{
			switch(type)
			{
				case FLIGHT:
					showFlightResult = true;
					break;
				case HOTEL:
					showHotelResult = true;
					break;
				case EXCURSION:
					showExcursionResult = true;
					break;
			}
		}


	
	}
	
	public void callSelectedFunction(TravelComponentDTO component, Object bean, String methodName)
	{
		java.lang.reflect.Method method = null;
		
		try {
			method = bean.getClass().getMethod(methodName, TravelComponentDTO.class);
		} catch (NoSuchMethodException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Server error", e.getMessage()));
			e.printStackTrace();
		} catch (SecurityException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Server error", e.getMessage()));
			e.printStackTrace();
		}
		
		try {
			method.invoke(bean, component);
		} catch (IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,"Server error", e.getMessage()));
			e.printStackTrace();
		}
	
	}
}