package beans.travelpackage.web;

import java.io.IOException;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;

import beans.employeehandler.EmployeeHandlerInterface;
import beans.travelcomponent.TravelComponentDTO;
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
	}
	
	public void showComponent(TravelComponentDTO helper) throws IOException{
		//select package and go to personal_package_home
		ArrayList<TravelComponentDTO> toSend = new ArrayList<TravelComponentDTO>();
		toSend.add(helper);
		data.setTravelComponentsList(toSend);
		RequestContext.getCurrentInstance().openDialog("/index.xhtml"); // TODO: waiting for TravelComponent page	
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
	
	
	//TODO:fare i casi per il customer e quella cosa getpersistence 
	
	
	
	
	
	
}
