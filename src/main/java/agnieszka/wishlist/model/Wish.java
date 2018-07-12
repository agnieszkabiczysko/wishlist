package agnieszka.wishlist.model;

import static agnieszka.wishlist.model.WishState.PURCHASED;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "wish")
public class Wish {

	public Wish() {
		super();
		this.state = WishState.ACTIVE;
	}

	public Wish(Offer offer) {
		super();
		this.offer = offer;
		this.state = WishState.ACTIVE;
	}

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(nullable = false)
	private WishState state;
	
	@ManyToOne
	@JoinColumn(name = "offer_id")
	private Offer offer;
	
	@ManyToOne
	@JoinColumn(name = "fulfiller_id")
	private User fulfiller;
	
	@ManyToOne
	@JoinColumn(name = "wishlist_id")
	private Wishlist wishlist;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getFulfiller() {
		return fulfiller;
	}

	public void setFulfiller(User fulfiller) {
		this.fulfiller = fulfiller;
	}

	public Offer getOffer() {
		return offer;
	}

	public void setOffer(Offer offer) {
		this.offer = offer;
	}
	
	public WishState getState() {
		return state;
	}

	public void setState(WishState state) {
		this.state = state;
	}

	public Wishlist getWishlist() {
		return wishlist;
	}

	public void setWishlist(Wishlist wishlist) {
		this.wishlist = wishlist;
	}
	
	public boolean isPurchased() {
		return state == PURCHASED;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (getClass() != other.getClass()) {
			return false;
		}
		Wish otherWish = (Wish) other;
		return new EqualsBuilder()
				.append(id, otherWish.id)
				.append(offer, otherWish.offer)
				.append(fulfiller, otherWish.fulfiller)
				.append(state, otherWish.state)
				.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(offer)
				.append(fulfiller)
				.append(state)
				.toHashCode();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(offer)
				.append(state)
				.append(fulfiller)
				.toString();
	}
	
}
