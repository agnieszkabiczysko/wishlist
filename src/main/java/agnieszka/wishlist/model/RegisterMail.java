package agnieszka.wishlist.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "registerMail")
public class RegisterMail {

	@Id
	@Column
	private String mailingId;
	
	@Column
	private String confirmationId;
	
	@Column
	private long mailingTime;
	
	@Column
	private RegisterMailState state;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User user;

	
	public RegisterMail() {
		super();
	}

	public RegisterMail(User user, String mailingId, String confirmationId, long mailingTime, RegisterMailState state) {
		super();
		this.user = user;
		this.mailingId = mailingId;
		this.confirmationId = confirmationId;
		this.mailingTime = mailingTime;
		this.state = state;
	}

	
	public String getMailingId() {
		return mailingId;
	}

	public void setMailingId(String mailingId) {
		this.mailingId = mailingId;
	}

	public String getConfirmationId() {
		return confirmationId;
	}

	public void setConfirmationId(String confirmationId) {
		this.confirmationId = confirmationId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public long getMailingTime() {
		return mailingTime;
	}

	public void setMailingTime(long mailingTime) {
		this.mailingTime = mailingTime;
	}

	@Enumerated(EnumType.STRING)
	public RegisterMailState getState() {
		return state;
	}

	public void setState(RegisterMailState state) {
		this.state = state;
	}
	
	public static boolean isActive(RegisterMail mail) {
		return mail.getState() == RegisterMailState.ACTIVE;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder()
				.append(mailingId)
				.append(confirmationId)
				.append(mailingTime)
				.append(state)
				.append(user)
				.hashCode();
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
		RegisterMail otherMailing = (RegisterMail) other;
		return new EqualsBuilder()
				.append(mailingId, otherMailing.mailingId)
				.append(confirmationId, otherMailing.confirmationId)
				.append(mailingTime, otherMailing.mailingTime)
				.append(state, otherMailing.state)
				.append(user, otherMailing.user)
				.isEquals();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this)
				.append(mailingId)
				.append(confirmationId)
				.append(mailingTime)
				.append(state)
				.append(user)
				.toString();
	}
	
	
	
}
