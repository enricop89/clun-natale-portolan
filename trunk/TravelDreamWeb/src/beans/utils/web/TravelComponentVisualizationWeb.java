package beans.utils.web;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import beans.travelcomponent.TravelComponentDTO;

@ManagedBean(name="TravelComponentVisualizationWeb")
@RequestScoped
public class TravelComponentVisualizationWeb 
{
	private TravelComponentDTO component;

	public TravelComponentDTO getComponent() {
		return component;
	}

	public void setComponent(TravelComponentDTO component) {
		this.component = component;
	}
}