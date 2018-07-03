package agnieszka.wishlist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@Table(name = "userPreferences")
public class UserPreferences {

	@Id
	@GeneratedValue(generator = "generator")
	@GenericGenerator(name = "generator", strategy = "foreign",
	parameters = @Parameter(name = "property", value = "user"))
	@Column(name = "user_id", unique = true, nullable = false)
	private int userId;
	
	@OneToOne(fetch = FetchType.EAGER)
	@PrimaryKeyJoinColumn
	private User user;
	
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "wishlist_id")
	private Wishlist currentWishlist;


	public UserPreferences(User user, Wishlist wishlist) {
		this.user = user;
		this.currentWishlist = wishlist;
	}

	
	public UserPreferences() {
		super();
	}


	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Wishlist getCurrentWishlist() {
		return currentWishlist;
	}

	public void setCurrentWishlist(Wishlist currentWishlist) {
		this.currentWishlist = currentWishlist;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(userId)
				.append(currentWishlist)
				.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserPreferences other = (UserPreferences) obj;
		return new EqualsBuilder()
				.append(userId, other.userId)
				.append(currentWishlist, other.currentWishlist)
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(userId)
				.append(currentWishlist)
				.toString();
	}

	
}
