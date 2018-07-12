package agnieszka.wishlist.model;

import static agnieszka.wishlist.model.WishlistState.PRIVATE;
import static agnieszka.wishlist.model.WishlistState.PUBLIC;
import static agnieszka.wishlist.model.WishlistState.SHARED;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "wishlist")
public class Wishlist {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private int id;
	
	@Column(unique = true, nullable = false)
	private String name;

	@ManyToOne
	@JoinColumn(name = "wisher_id")
	private User wisher;

	@Column(nullable = false)
	private WishlistState state;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "wishlist_id")
	private Set<Wish> wishes = new HashSet<>();

	public Wishlist() {
		super();
	}

	public Wishlist(String name, WishlistState state) {
		super();
		this.name = name;
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public User getWisher() {
		return wisher;
	}

	public void setWisher(User wisher) {
		this.wisher = wisher;
	}

	public WishlistState getState() {
		return state;
	}
	
	@Enumerated(EnumType.STRING)
	public void setState(String state) {
		WishlistState wishlistState =  WishlistState.valueOf(state);
		this.state = wishlistState;
	}
	
	public Set<Wish> getWishes() {
		return wishes;
	}

	public void setWishes(Set<Wish> wishes) {
		this.wishes = wishes;
	}

	public void add(Offer offer) {
		wishes.add(new Wish(offer));
	}
	
	public boolean isEmpty() {
		return wishes.isEmpty();
	}
	
	public boolean hasWishes() {
		return !wishes.isEmpty();
	}
	
	public boolean contains(Offer offer) {
		return wishes.stream().anyMatch(w -> offer.equals(w.getOffer()));
	}
	
	public boolean contains(Wish wish) {
		return wishes.contains(wish);
	}
	
	public boolean isPublic() {
		return state == PUBLIC;
	}
	
	public boolean isShared() {
		return state == SHARED;
	}
	
	public boolean isPrivate() {
		return state == PRIVATE;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(name)
				.append(wisher)
				.append(wishes)
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
		Wishlist other = (Wishlist) obj;
		return new EqualsBuilder()
				.append(id, other.id)
				.append(name, other.name)
				.append(wisher, other.wisher)
				.append(wishes, other.wishes)
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(id)
				.append(name)
				.append(wisher)
				.append(wishes)
				.toString();
	}
	
}
