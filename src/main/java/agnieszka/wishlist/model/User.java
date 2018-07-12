package agnieszka.wishlist.model;

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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "user")
public class User {
	
	public User() {
		super();
	}

	public User(String userId, String firstname, String lastname, EmailAddress email, UserState state) {
		super();
		this.userId = userId;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.state = state;
	}

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	@Column
	private int id;
	
	@Column(unique = true, nullable = false)
	private String userId;
		
	@Column(nullable = false)
	private String firstname;
	
	@Column(nullable = false)
	private String lastname;
	
	@Column(nullable = true)
	private String password;
	
	@OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "email", unique = true)
	private EmailAddress email;
	
	@Column(nullable=false)
    private UserState state=UserState.INACTIVE;
	
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "user_user_profile",
			joinColumns = {@JoinColumn(name = "user_id")},
			inverseJoinColumns = {@JoinColumn(name = "user_profile_id")})
	private Set<UserProfile> userProfiles = new HashSet<>();
		
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "offerSeller", orphanRemoval = true, fetch = FetchType.EAGER)
	private Set<Offer> offers = new HashSet<>();
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "wisher", orphanRemoval = true)
	private Set<Wishlist> wishlists = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "fulfiller", orphanRemoval = true)
	private Set<Wish> fulfills = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="friends",
			joinColumns = {@JoinColumn(name = "personId")},
			inverseJoinColumns = {@JoinColumn(name = "friendId")})
	private Set<User> friends = new HashSet<>();


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String ssoId) {
		this.userId = ssoId;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public EmailAddress getEmail() {
		return email;
	}

	public void setEmail(EmailAddress email) {
		this.email = email;
	}

	public UserState getState() {
		return state;
	}

	@Enumerated(EnumType.STRING)
	public void setState(UserState state) {
		this.state = state;
	}

	public Set<UserProfile> getUserProfiles() {
		return userProfiles;
	}

	public void setUserProfiles(Set<UserProfile> userProfiles) {
		this.userProfiles = userProfiles;
	}

	public Set<Offer> getOffers() {
		return offers;
	}

	public void setOffers(Set<Offer> offers) {
		this.offers = offers;
	}

	public Set<Wishlist> getWishlists() {
		return wishlists;
	}

	public void setWishlists(Set<Wishlist> wishlists) {
		this.wishlists = wishlists;
	}

	public Set<Wish> getFulfills() {
		return fulfills;
	}

	public void setFulfills(Set<Wish> fulfills) {
		this.fulfills = fulfills;
	}
	
	public Set<User> getFriends() {
		return friends;
	}

	public void setFriends(Set<User> friends) {
		this.friends = friends;
	}
	
	public void addFriend(User friend) {
		friends.add(friend);
	}
	
	public boolean isFriendOf(User anotherUser) {
		return anotherUser.friends.contains(this);
	}
	
	public void addUserProfile(UserProfile userProfile) {
		userProfiles.add(userProfile);
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(userId)
				.toHashCode();
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
		User otherUser = (User) other;
		return new EqualsBuilder()
				.append(userId, otherUser.userId)
				.isEquals();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(id)
				.append(userId)
				.append(firstname)
				.append(lastname)
				.append(offers)
				.append(wishlists)
				.append(fulfills)
				.toString();
	}

}
