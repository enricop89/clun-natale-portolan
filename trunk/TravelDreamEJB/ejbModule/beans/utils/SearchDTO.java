package beans.utils;

import java.util.ArrayList;
import java.util.List;

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
	public List<PredefinedTravelPackageDTO> findPredefinedTravelPackage(String name){
		List<PredefinedTravelPackage> packages = search.findPredefinedTravelPackage(name);
		List<PredefinedTravelPackageDTO> result = new ArrayList<PredefinedTravelPackageDTO>();
		for(int i = 0; i < packages.size(); i++)
			result.add(convertToDTO(packages.get(i)));
		return result;
	}
	
	@Override
	public List<PredefinedTravelPackageDTO> findAllPredefinedTravelPackages(){
		List<PredefinedTravelPackage> packages = search.findAllPredefinedTravelPackages();
		List<PredefinedTravelPackageDTO> result = new ArrayList<PredefinedTravelPackageDTO>();
		for(int i = 0; i < packages.size(); i++)
			result.add(convertToDTO(packages.get(i)));
		return result;
	}
	
	@Override
	public List<TravelComponentDTO> findTravelComponent(TravelComponentDTO searchCriteria){
		List<TravelComponent> components = search.findTravelComponent(new TravelComponent(searchCriteria));
		List<TravelComponentDTO> result = new ArrayList<TravelComponentDTO>();
		for(int i = 0; i < components.size(); i++)
			result.add(convertToDTO(components.get(i)));
		return result;
	}
	
	@Override
	public List<TravelComponentDTO> findAllTravelComponents(){
		List<TravelComponent> components = search.findAllTravelComponents();
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
		List<UserDTO> result = new ArrayList<UserDTO>();
		for(int i = 0; i < users.size(); i++)
			result.add(convertToDTO(users.get(i)));
		return result;
	}
	
	@Override
	public List<UserDTO> findAllUser(){
		List<User> users = search.findAllUser();
		List<UserDTO> result = new ArrayList<UserDTO>();
		for(int i = 0; i < users.size(); i++)
			result.add(convertToDTO(users.get(i)));
		return result;		
	}
	
	// helper functions
    private UserDTO convertToDTO(User user) {
		UserDTO userDTO = new UserDTO();
		userDTO.setEmail(user.getEmail());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		userDTO.setPassword(null);
		return userDTO;
	}
    private TravelComponentDTO convertToDTO(TravelComponent travelComponent){
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
    	List<PredefinedTravelPackageDTO> predefinedTravelPackages = new ArrayList<PredefinedTravelPackageDTO>();
    	for(int i = 0; i < travelComponent.getPredefinedTravelPackages().size(); i++)
    		predefinedTravelPackages.add(convertToDTO(travelComponent.getPredefinedTravelPackages().get(i)));
    	travelComponentDTO.setPredefinedTravelPackagess(predefinedTravelPackages);
    	List<TravelElementDTO> travelElements = new ArrayList<TravelElementDTO>();
    	for(int i = 0; i < travelComponent.getTravelElements().size(); i++)
    		travelElements.add(convertToDTO(travelComponent.getTravelElements().get(i)));
    	travelComponentDTO.setTravelElements(travelElements);
    	return travelComponentDTO;
    }
    private TravelElementDTO convertToDTO(TravelElement travelElement){
    	TravelElementDTO travelElementDTO = new TravelElementDTO();
    	travelElementDTO.setId(travelElement.getId());
    	travelElementDTO.setTravelComponent(convertToDTO(travelElement.getTravelComponent()));
    	travelElementDTO.setOwner(convertToDTO(travelElement.getOwner()));
    	travelElementDTO.setConfirmationDateTime(travelElement.getConfirmationDateTime());
    	return travelElementDTO;
    }
    private PredefinedTravelPackageDTO convertToDTO(PredefinedTravelPackage predefinedTravelPackage){
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
    	PersonalizedTravelPackageDTO personalizedTravelPackageDTO = new PersonalizedTravelPackageDTO();
    	personalizedTravelPackageDTO.setId(personalizedTravelPackage.getId());
    	personalizedTravelPackageDTO.setName(personalizedTravelPackage.getName());
    	personalizedTravelPackageDTO.setOwner(convertToDTO(personalizedTravelPackage.getOwner()));
    	personalizedTravelPackageDTO.setDepartureDate(personalizedTravelPackage.getDepartureDate());
    	personalizedTravelPackageDTO.setReturnDate(personalizedTravelPackage.getReturnDate());
    	List<Components_HelperDTO> travelComponents = new ArrayList<Components_HelperDTO>();
    	for(int i = 0; i < personalizedTravelPackage.getTravelComponents().size(); i++)
    		travelComponents.add(convertToDTO(personalizedTravelPackage.getTravelComponents().get(i)));
    	personalizedTravelPackageDTO.setTravelComponents(travelComponents);
    	return personalizedTravelPackageDTO;
    }
    private Components_HelperDTO convertToDTO(Components_Helper component){
    	Components_HelperDTO componentDTO = new Components_HelperDTO();
    	componentDTO.setId(component.getId());
    	componentDTO.setPersonalizedTravelPackage(convertToDTO(component.getPersonalizedTravelPackage()));
    	componentDTO.setTravelComponent(convertToDTO(component.getTravelComponent()));
    	componentDTO.setTravelElement(convertToDTO(component.getTravelElement()));
    	componentDTO.setPersistence(convertToDTO(component.getPersistence()));
    	return componentDTO;
 
    }
    private GiftListDTO convertToDTO(GiftList giftList){
    	GiftListDTO giftListDTO = new GiftListDTO();
    	giftListDTO.setOwner(convertToDTO(giftList.getOwner()));
    	List<GiftElements_HelperDTO> giftElements = new ArrayList<GiftElements_HelperDTO>();
    	for(int i = 0; i < giftList.getGiftElements().size(); i++)
    		giftElements.add(convertToDTO(giftList.getGiftElements().get(i)));
    	giftListDTO.setGiftElements(giftElements);
    	return giftListDTO;
    }
    private GiftElements_HelperDTO convertToDTO(GiftElements_Helper elements){
    	GiftElements_HelperDTO elementsDTO = new GiftElements_HelperDTO();
    	elementsDTO.setId(elements.getId());
    	elementsDTO.setGiftList(convertToDTO(elements.getGiftList()));
    	elementsDTO.setPersonalizedTravelPackage(convertToDTO(elements.getPersonalizedTravelPackage()));
    	elementsDTO.setTravelComponent(convertToDTO(elements.getTravelComponent()));
    	return elementsDTO;
    }
}
