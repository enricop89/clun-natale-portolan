package beans.customerhandler;

import java.util.List;
import beans.accountmanagement.UserDTO;

public class GiftListDTO {
	private UserDTO owner;
	private List<GiftElements_HelperDTO> giftElements;
	
	public GiftListDTO() {
		super();
	}   
	public UserDTO getOwner() {
		return this.owner;
	}

	public void setOwner(UserDTO owner) {
		this.owner = owner;
	}

	public List<GiftElements_HelperDTO> getGiftElements() {
		return this.giftElements;
	}
	
	public void setGiftElements(List<GiftElements_HelperDTO> giftElements) {
		this.giftElements = giftElements;
	}
}
