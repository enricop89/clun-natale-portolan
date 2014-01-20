package beans.travelpackage.web;


import javax.annotation.PostConstruct;
import javax.inject.Inject;

import beans.travelpackage.PersonalizedTravelPackageDTO;
import beans.utils.web.Data_Exchange;

public class TravelPackagePage {

	@Inject
	private Data_Exchange data;
	public Data_Exchange getData(){
		return data;
	}
	public void setData(Data_Exchange data){
		this.data = data;
	}
	
	private PersonalizedTravelPackageDTO persTP;
	
	@PostConstruct
	public void visualizepersonalizeTP(){	
		persTP=data.getPersonalizedTravelPackagesList().get(0);
	}
	
	public PersonalizedTravelPackageDTO getPersTP() {
		return persTP;
	}
	public void setPersTP(PersonalizedTravelPackageDTO persTP) {
		this.persTP = persTP;
	}
	
	
	
}
