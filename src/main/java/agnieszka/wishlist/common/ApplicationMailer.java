package agnieszka.wishlist.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import agnieszka.wishlist.model.EmailAddress;

@Service("mailService")
public class ApplicationMailer {
	
	@Autowired
	private JavaMailSender mailSender;
	
	private static class Builder {
		
		private EmailAddress email;
		private String subject;
		private String body;
		
		public Builder email(EmailAddress email) {
			this.email = email;
			return this;
		}
		
		public Builder subject(String subject) {
			this.subject = subject;
			return this;
		}
		
		public Builder body(String body) {
			this.body = body;
			return this;
		}
		
		public SimpleMailMessage build() {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(email.getEmail());
			message.setSubject(subject);
			message.setText(body);
			return message;
		}
	}
	
	
	public void sendMessage(EmailAddress email, String subject, String body) {
		SimpleMailMessage message = new Builder()
				.email(email)
				.subject(subject)
				.body(body)
				.build();
		mailSender.send(message);
	}

}
