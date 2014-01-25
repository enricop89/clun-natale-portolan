package beans.travelpackage.web;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.primefaces.event.CloseEvent;

import beans.accountmanagement.UserDTO;
import beans.customerhandler.CustomerHandlerInterface;
import beans.employeehandler.EmployeeHandlerInterface;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelpackage.PersonalizedTravelPackageDTO;
import beans.travelpackage.PredefinedTravelPackageDTO;
import beans.utils.SearchDTOInterface;
import beans.utils.web.Data_Exchange;

@ManagedBean(name="PredefinedTravelPackageWeb")
@ViewScoped
public class PredefinedTravelPackageWeb {
	@EJB
	private SearchDTOInterface search;
	private PredefinedTravelPackageDTO predTP;
	private EmployeeHandlerInterface employee;
	private CustomerHandlerInterface customerhandler;
	private UserDTO user;
	private java.util.Date departureDate;
	private java.util.Date returnDate;
	private PersonalizedTravelPackageDTO personalizedPackage;
	private PredefinedTravelPackageDTO packageDTO;
	@Inject
	private Data_Exchange data;
	public Data_Exchange getData(){
		return data;
	}
	public void setData(Data_Exchange data){
		this.data = data;
	}
	
	@PostConstruct
	public void init(){	
		predTP=data.getPredefinedTravelPackagesList().get(0);
		departureDate=new java.util.Date (predTP.getDepartureDate().getTime());
		returnDate=new java.util.Date (predTP.getReturnDate().getTime());
	}
	
	public void showComponent(TravelComponentDTO helper) throws IOException{
		//select package and go to personal_package_home
		List<TravelComponentDTO> toSend = new ArrayList<TravelComponentDTO>();
		toSend.add(helper);
		data.setTravelComponentsList(toSend);
        Map<String,Object> options = new HashMap<String, Object>();  
        options.put("resizable", false);
		RequestContext.getCurrentInstance().openDialog("/misc/dialog_travelcomponent.xhtml",options,null);	
	}
	
	public void updateComponent(TravelComponentDTO helper) throws IOException{
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		boolean result=employee.updateTravelComponent(helper);
		if(result==true){
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have succesfully modify your component from the travel package!"));
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "paginapackage.xhtml");
		}
		else{
			
			facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error", "Something went wrong")); 
		}
		
	}
	
	public void copyInPersonalizedTravelPackage(PredefinedTravelPackageDTO helper) throws IOException{
		user = search.findUser(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
		customerhandler.addNewPersonalizedTravelPackage(user, predTP);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "You have succesfully add the travel package in your list of package!"));
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personal_travel_package.xhtml?faces-redirect=true");
	}
	
public void save() throws IOException{
		
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		
		if(departureDate != null)
			predTP.setDepartureDate(new Date(departureDate.getTime()));
		if(returnDate != null)
			predTP.setReturnDate(new Date(returnDate.getTime()));
		
		user = search.findUser(FacesContext.getCurrentInstance().getExternalContext().getRemoteUser());
		if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("CUSTOMER"))
			copyInPersonalizedTravelPackage(predTP); //if customer copy in personalized
		if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("EMPLOYEE"))
			employee.updatePredefinedTravelPackage(predTP); //if employee update
		
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Successful", "The package has been succesfully added to your package list!")); 
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/personal_travel_package.xhtml?faces-redirect=true");
		
	}

	public void modify(TravelComponentDTO helper) throws IOException{
	
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		List<TravelComponentDTO> toSend = new ArrayList<TravelComponentDTO>();
		toSend.add(helper);
		data.setTravelComponentsList(toSend);
		FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/misc/modify_predefinedtravelpackage.xhtml?faces-redirect=true");
	
	}
	
	public void onTravelComponentChosen(CloseEvent event) throws IOException
	{
		try{ // must use try catch, getters in Data_Exchange flushes lists by design! calling it twice is a logical error
			TravelComponentDTO travelComponent = data.getTravelComponentsList().get(0);
			employee.addTravelComponentToPredefinedTravelPackage(packageDTO, travelComponent);	//TODO:se salva chiamo copy, dipende se customer o employee
			List<PredefinedTravelPackageDTO> toSend = new ArrayList<PredefinedTravelPackageDTO>();
			toSend.add(packageDTO);
			data.setPredefinedTravelPackagesList(toSend);
			FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/customer/control_panel.xhtml");
		}
		catch (java.lang.IndexOutOfBoundsException e){/* does nothing */}
	}
	
	
	
	public boolean checkIfCustomer() throws IOException{
		
		if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("CUSTOMER"))
				return true; //the remote user is a customer
		
			return false; 
	}
	public boolean checkIfEmployee() throws IOException{
		if(FacesContext.getCurrentInstance().getExternalContext().isUserInRole("EMPLOYEE"))
			return true;
	
		return false;
	
	}
	
	public PredefinedTravelPackageDTO getPredTP() {
		return predTP;
	}
	public void setPredTP(PredefinedTravelPackageDTO predTP) {
		this.predTP = predTP;
	}
	public EmployeeHandlerInterface getEmployee() {
		return employee;
	}
	public void setEmployee(EmployeeHandlerInterface employee) {
		this.employee = employee;
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
	public PersonalizedTravelPackageDTO getPersonalizedPackage() {
		return personalizedPackage;
	}
	public void setPersonalizedPackage(PersonalizedTravelPackageDTO personalizedPackage) {
		this.personalizedPackage = personalizedPackage;
	}
	
	
}
