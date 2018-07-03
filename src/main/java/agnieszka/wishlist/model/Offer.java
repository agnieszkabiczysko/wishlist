package agnieszka.wishlist.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "offer")
public class Offer{

	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@NotNull
	@Column(unique = true, nullable = false)
	private String name;
	
	@NotNull
	@Column
	private String description;
	
	@NotNull
	@Column
	private String vendor;

	@Transient
	private MultipartFile image;
	
	@ManyToOne
	@JoinColumn(name = "seller_id")
	private User offerSeller;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "offer", orphanRemoval=true)
	private List<Wish> wishesList = new ArrayList<>();

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public User getOfferSeller() {
		return offerSeller;
	}

	public void setOfferSeller(User offerSeller) {
		this.offerSeller = offerSeller;
	}
	
	public List<Wish> getWishesList() {
		return wishesList;
	}

	public void setWishesList(List<Wish> wishesList) {
		this.wishesList = wishesList;
	}
	
	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
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
		Offer otherOffer = (Offer) other;
		return new EqualsBuilder()
				.append(id, otherOffer.id)
				.append(name, otherOffer.name)
				.append(description, otherOffer.description)
				.append(vendor, otherOffer.vendor)
				.append(offerSeller, otherOffer.offerSeller)
				.isEquals();
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(name)
				.append(description)
				.append(vendor)
				.append(offerSeller)
				.toHashCode();
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(id)
				.append(name)
				.append(description)
				.append(vendor)
				.append(offerSeller)
				.toString();
	}

}
