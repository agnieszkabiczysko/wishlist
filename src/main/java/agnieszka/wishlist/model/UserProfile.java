package agnieszka.wishlist.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "user_profile")
public class UserProfile {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private int id;
	
	@Column(length = 15, nullable = false)
	private String type = UserProfileType.USER.name();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(id)
				.append(type)
				.build();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserProfile other = (UserProfile) obj;
		return new EqualsBuilder()
				.append(id, other.id)
				.append(type, other.type)
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(id)
				.append(type)
				.toString();
	}
	
}
