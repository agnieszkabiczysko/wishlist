package agnieszka.wishlist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name= "wishlistMail")
public class WishlistMail {
	
	public WishlistMail() {
		super();
	}

	public WishlistMail(String mailId, Wishlist wishlist) {
		super();
		this.mailId = mailId;
		this.wishlist = wishlist;
	}

	@Id
	@Column
	private String mailId;
	
	@ManyToOne
	@JoinColumn(name = "wishlist_id")
	private Wishlist wishlist;

	public String getMailId() {
		return mailId;
	}

	public void setMailId(String mailId) {
		this.mailId = mailId;
	}

	public Wishlist getWishlist() {
		return wishlist;
	}

	public void setWishlist(Wishlist wishlist) {
		this.wishlist = wishlist;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(mailId)
				.append(wishlist)
				.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WishlistMail other = (WishlistMail) obj;
		return new EqualsBuilder()
				.append(mailId, other.mailId)
				.append(wishlist, other.wishlist)
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(mailId)
				.append(wishlist)
				.toString();
	}
	
	
}
