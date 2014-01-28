package beans.utils;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import beans.accountmanagement.UserDTO;
import beans.customerhandler.GiftElements_HelperDTO;
import beans.customerhandler.GiftListDTO;
import beans.travelcomponent.TravelComponentDTO;
import beans.travelcomponent.TravelElementDTO;
import beans.travelpackage.Components_HelperDTO;
import beans.travelpackage.PersonalizedTravelPackageDTO;
import beans.travelpackage.PredefinedTravelPackageDTO;
import entities.*;

/**
 * Session Bean implementation class SearchDTO
 */
@Stateless
public class SearchDTO implements SearchDTOInterface {
	@EJB
	private Search search;
	
	@Override
	public GiftListDTO findGiftList(UserDTO owner){
		return convertToDTO(search.findGiftList(search.findUser(owner)));
	}
	
	@Override
	public PersonalizedTravelPackageDTO findPersonalizedTravelPackage(long id){
		return convertToDTO(search.findPersonalizedTravelPackage(id));
	}
	
	@Override
	public List<PersonalizedTravelPackageDTO> findAllPersonalizedTravelPackages(UserDTO owner){
		List<PersonalizedTravelPackage> packages = search.findAllPersonalizedTravelPackages(search.findUser(owner));
		if(packages == null)
			return null;
		
		List<PersonalizedTravelPackageDTO> result = new ArrayList<PersonalizedTravelPackageDTO>();
		for(int i = 0; i < packages.size(); i++)
			result.add(convertToDTO(packages.get(i)));
		return result;
	}
	
	@Override
	public List<PersonalizedTravelPackageDTO> findAllPersonalizedTravelPackages(){
		List<PersonalizedTravelPackage> packages = search.findAllPersonalizedTravelPackages();
		List<PersonalizedTravelPackageDTO> result = new ArrayList<PersonalizedTravelPackageDTO>();
		for(int i = 0; i < packages.size(); i++)
			result.add(convertToDTO(packages.get(i)));
		return result;
	}
	
	@Override
	public List<PredefinedTravelPackageDTO> findPredefinedTravelPackage(String name, Date departureDate, Date returnDate){
		List<PredefinedTravelPackage> packages = search.findPredefinedTravelPackage(name,departureDate,returnDate);
		if(packages == null)
			return null;
		
		List<PredefinedTravelPackageDTO> result = new ArrayList<PredefinedTravelPackageDTO>();
		for(int i = 0; i < packages.size(); i++)
			result.add(convertToDTO(packages.get(i)));
		return result;
	}
	
	@Override
	public List<PredefinedTravelPackageDTO> findAllPredefinedTravelPackages(){
		List<PredefinedTravelPackage> packages = search.findAllPredefinedTravelPackages();
		if(packages == null)
			return null;
		
		List<PredefinedTravelPackageDTO> result = new ArrayList<PredefinedTravelPackageDTO>();
		for(int i = 0; i < packages.size(); i++)
			result.add(convertToDTO(packages.get(i)));
		return result;
	}
	
	@Override
	public List<TravelComponentDTO> findTravelComponent(TravelComponentDTO searchCriteria){
		List<TravelComponent> components = search.findTravelComponent(new TravelComponent(searchCriteria,search));
		if(components == null)
			return null;
		
		List<TravelComponentDTO> result = new ArrayList<TravelComponentDTO>();
		for(int i = 0; i < components.size(); i++)
			result.add(convertToDTO(components.get(i)));
		return result;
	}
	
	@Override
	public List<TravelComponentDTO> findAllTravelComponents(){
		List<TravelComponent> components = search.findAllTravelComponents();
		if(components == null)
			return null;
		
		List<TravelComponentDTO> result = new ArrayList<TravelComponentDTO>();
		for(int i = 0; i < components.size(); i++)
			result.add(convertToDTO(components.get(i)));
		return result;
	}
	
	@Override
	public UserDTO findUser(String email) {
		return convertToDTO(search.findUser(email));
    } 
	
	@Override
	public List<UserDTO> findUser(String firstName, String lastName){
		List<User> users = search.findUser(firstName, lastName);
		if(users == null)
			return null;
		
		List<UserDTO> result = new ArrayList<UserDTO>();
		for(int i = 0; i < users.size(); i++)
			result.add(convertToDTO(users.get(i)));
		return result;
	}
	
	@Override
	public List<UserDTO> findAllUser(){
		List<User> users = search.findAllUser();
		if(users == null)
			return null;
		
		List<UserDTO> result = new ArrayList<UserDTO>();
		for(int i = 0; i < users.size(); i++)
			result.add(convertToDTO(users.get(i)));
		return result;		
	}
	
	// helper functions
    private UserDTO convertToDTO(User user) {
    	if(user == null)
    		return null;
		UserDTO userDTO = new UserDTO();
		userDTO.setEmail(user.getEmail());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		userDTO.setPassword(null);
		return userDTO;
	}
    private TravelComponentDTO convertToDTO(TravelComponent travelComponent){
    	if(travelComponent == null)
    		return null;
    	TravelComponentDTO travelComponentDTO = new TravelComponentDTO();
    	travelComponentDTO.setId(travelComponent.getId());
    	travelComponentDTO.setType(travelComponent.getType());
    	travelComponentDTO.setSupplyingCompany(travelComponent.getSupplyingCompany());
    	travelComponentDTO.setFlightDepartureDateTime(travelComponent.getFlightDepartureDateTime());
    	travelComponentDTO.setFlightArrivalDateTime(travelComponent.getFlightArrivalDateTime());
    	travelComponentDTO.setFlightDepartureCity(travelComponent.getFlightDepartureCity());
    	travelComponentDTO.setFlightArrivalCity(travelComponent.getFlightArrivalCity());
    	travelComponentDTO.setFlightCode(travelComponent.getFlightCode());
    	travelComponentDTO.setHotelCity(travelComponent.getHotelCity());
    	travelComponentDTO.setHotelDate(travelComponent.getHotelDate());
    	travelComponentDTO.setExcursionDescription(travelComponent.getExcursionDescription());
    	travelComponentDTO.setExcursionDateTime(travelComponent.getExcursionDateTime());
    	travelComponentDTO.setExcursionCity(travelComponent.getExcursionCity());
    	travelComponentDTO.setAvailability(travelComponent.getTravelElements().size());
    	return travelComponentDTO;
    }
    private TravelComponentDTO convertToDTO(TravelComponent travelComponent, boolean isPersistence){
    	if(travelComponent == null)
    		return null;
    	TravelComponentDTO travelComponentDTO = new TravelComponentDTO();
    	travelComponentDTO.setId(travelComponent.getId());
    	travelComponentDTO.setType(travelComponent.getType());
    	travelComponentDTO.setSupplyingCompany(travelComponent.getSupplyingCompany());
    	travelComponentDTO.setFlightDepartureDateTime(travelComponent.getFlightDepartureDateTime());
    	travelComponentDTO.setFlightArrivalDateTime(travelComponent.getFlightArrivalDateTime());
    	travelComponentDTO.setFlightDepartureCity(travelComponent.getFlightDepartureCity());
    	travelComponentDTO.setFlightArrivalCity(travelComponent.getFlightArrivalCity());
    	travelComponentDTO.setFlightCode(travelComponent.getFlightCode());
    	travelComponentDTO.setHotelCity(travelComponent.getHotelCity());
    	travelComponentDTO.setHotelDate(travelComponent.getHotelDate());
    	travelComponentDTO.setExcursionDescription(travelComponent.getExcursionDescription());
    	travelComponentDTO.setExcursionDateTime(travelComponent.getExcursionDateTime());
    	travelComponentDTO.setExcursionCity(travelComponent.getExcursionCity());
    	if(isPersistence == false)
    		travelComponentDTO.setAvailability(travelComponent.getTravelElements().size());
    	
    	return travelComponentDTO;
    }
    private TravelElementDTO convertToDTO(TravelElement travelElement){
    	if(travelElement == null)
    		return null;    	
    	TravelElementDTO travelElementDTO = new TravelElementDTO();
    	travelElementDTO.setId(travelElement.getId());
    	travelElementDTO.setTravelComponent(convertToDTO(travelElement.getTravelComponent()));
    	travelElementDTO.setOwner(convertToDTO(travelElement.getOwner()));
    	travelElementDTO.setConfirmationDateTime(travelElement.getConfirmationDateTime());
    	return travelElementDTO;
    }
    private PredefinedTravelPackageDTO convertToDTO(PredefinedTravelPackage predefinedTravelPackage){
    	if(predefinedTravelPackage == null)
    		return null;
    	PredefinedTravelPackageDTO predefinedTravelPackageDTO = new PredefinedTravelPackageDTO();
    	predefinedTravelPackageDTO.setId(predefinedTravelPackage.getId());
    	predefinedTravelPackageDTO.setName(predefinedTravelPackage.getName());
    	predefinedTravelPackageDTO.setDescription(predefinedTravelPackage.getDescription());
    	predefinedTravelPackageDTO.setReturnDate(predefinedTravelPackage.getReturnDate());
    	predefinedTravelPackageDTO.setDepartureDate(predefinedTravelPackage.getDepartureDate());
    	List<TravelComponentDTO> travelComponents = new ArrayList<TravelComponentDTO>();
    	for(int i = 0; i < predefinedTravelPackage.getTravelComponents().size(); i++)
    		travelComponents.add(convertToDTO(predefinedTravelPackage.getTravelComponents().get(i)));
    	predefinedTravelPackageDTO.setTravelComponents(travelComponents);
    	return predefinedTravelPackageDTO;
    }
    private PersonalizedTravelPackageDTO convertToDTO(PersonalizedTravelPackage personalizedTravelPackage){
    	if(personalizedTravelPackage == null)
    		return null;
    	PersonalizedTravelPackageDTO personalizedTravelPackageDTO = new PersonalizedTravelPackageDTO();
    	personalizedTravelPackageDTO.setId(personalizedTravelPackage.getId());
    	personalizedTravelPackageDTO.setName(personalizedTravelPackage.getName());
    	personalizedTravelPackageDTO.setOwner(convertToDTO(personalizedTravelPackage.getOwner()));
    	personalizedTravelPackageDTO.setDepartureDate(personalizedTravelPackage.getDepartureDate());
    	personalizedTravelPackageDTO.setReturnDate(personalizedTravelPackage.getReturnDate());
    	List<Components_HelperDTO> travelComponents = new ArrayList<Components_HelperDTO>();
    	for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++){
    		travelComponents.add(convertToDTO(personalizedTravelPackage.getTravelComponents().get(i)));
    		travelComponents.get(i).setPersonalizedTravelPackage(personalizedTravelPackageDTO);
    	}
    	personalizedTravelPackageDTO.setTravelComponents(travelComponents);
    	return personalizedTravelPackageDTO;
    }
    private Components_HelperDTO convertToDTO(Components_Helper component){
    	if(component == null)
    		return null;
    	Components_HelperDTO componentDTO = new Components_HelperDTO();
    	componentDTO.setId(component.getId());
    	componentDTO.setTravelComponent(convertToDTO(component.getTravelComponent()));
    	componentDTO.setTravelElement(convertToDTO(component.getTravelElement()));
    	componentDTO.setPersistence(convertToDTO(component.getPersistence(),true));
    	return componentDTO;
 
    }
    private GiftListDTO convertToDTO(GiftList giftList){
    	if(giftList == null)
    		return null;
    	GiftListDTO giftListDTO = new GiftListDTO();
    	giftListDTO.setOwner(convertToDTO(giftList.getOwner()));
    	List<GiftElements_HelperDTO> giftElements = new ArrayList<GiftElements_HelperDTO>();
    	for(int i = 0; i < giftList.getGiftElements().size(); i++){
    		giftElements.add(convertToDTO(giftList.getGiftElements().get(i),giftListDTO));
    	}
    	giftListDTO.setGiftElements(giftElements);
    	return giftListDTO;
    }
    
    private GiftElements_HelperDTO convertToDTO(GiftElements_Helper element, GiftListDTO giftList){
    	if(element == null)
    		return null;
    	GiftElements_HelperDTO elementDTO = new GiftElements_HelperDTO();
    	elementDTO.setId(element.getId());
    	elementDTO.setGiftList(giftList);
    	elementDTO.setPersonalizedTravelPackage(convertToDTO(element.getPersonalizedTravelPackage()));
    	elementDTO.setTravelComponent(convertToDTO(element.getTravelComponent()));
    	return elementDTO;
    }
}
