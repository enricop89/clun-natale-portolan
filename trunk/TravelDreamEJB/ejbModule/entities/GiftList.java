package entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: GiftList
 *
 */
@Entity

public class GiftList implements Serializable {
	private static final long serialVersionUID = 1L;
	   
	@Id
	private User owner;

	@OneToMany(mappedBy="giftList")
	private List<GiftElements_Helper> giftElements;
	
	public GiftList() {
		super();
	}   
	public User getOwner() {
		return this.owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public List<GiftElements_Helper> getGiftElements() {
		return this.giftElements;
	}
	
	public void setGiftElements(List<GiftElements_Helper> giftElements) {
		this.giftElements = giftElements;
	}
}
