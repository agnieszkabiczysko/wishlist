package test.agnieszka.wishlist.email;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GmailInbox {

	private static final Logger LOGGER = LoggerFactory.getLogger(GmailInbox.class);

	private Store connectWith(String email, String password) throws MessagingException {
		@SuppressWarnings("serial")
		Properties properties = new Properties() {
			{
				put("mail.store.protocol", "imap");
				put("mail.imaps.host", "imap.gmail.com");
				put("mail.debug", "true");
				put("mail.debug.auth", "true");
			}
		};
		Session session = Session.getDefaultInstance(properties, null);
		Store store = session.getStore("imaps");
		store.connect(email, password);
		return store;
	}

	private Message[] fetchMessages(Folder inbox) throws MessagingException {
		Message[] messages = inbox.getMessages();
		for (Message msg : messages) {
			LOGGER.info(msg.toString());
			msg.setFlag(Flags.Flag.DELETED, true);
		} return messages;
	}

	public String getLatestMessageContent(String email, String password) {
		Message[] messages;
		try {
			Store store = connectWith(email, password);
			Folder inbox = store.getFolder("inbox");
			inbox.open(Folder.READ_WRITE);
			messages = fetchMessages(inbox);
			LOGGER.info("Liczba maili: " + messages.length);
			Message newestMessage = messages[0];
			LOGGER.info(newestMessage.toString());
			for (Message msg : messages) {
				if (msg.getReceivedDate().after(newestMessage.getReceivedDate())) {
					newestMessage = msg;
				}
			}
			LOGGER.info(newestMessage.toString());
			String content = newestMessage.getContent().toString();
			inbox.close(true);
			store.close();
			return content;
		} catch (MessagingException  | IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	public String extractLink(String messageContent) {
		String link;
		link = messageContent.substring(messageContent.indexOf("http"));
		link = link.substring(0, link.indexOf("\n"));
		LOGGER.info(link);
		return link;
	}
}
