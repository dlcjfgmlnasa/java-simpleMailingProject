package 메일링프로젝트;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;

import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

public class IMAP implements MailProto{
	protected Session session;
	protected Store store;
	protected Folder folder;
	private static String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	@Override
	public void open(String host, String id, String passwd) throws MessagingException {
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		session = Session.getInstance(props, null);
		store = session.getStore();
		store.connect(host,id,passwd);
		folder = store.getDefaultFolder().getFolder("Sent Messages");
		folder.open(Folder.READ_WRITE);
	}

	@Override
	public void close() throws MessagingException {
		if(folder != null && folder.isOpen()){
			folder.close(true);
		} 
		if(store != null && store.isConnected()){
			store.close();
		}
	}

	@Override
	public Message getMessages(int msgNum) throws MessagingException {
		if(!folder.isOpen()){
			throw new MessagingException("Already colsed folder");
		}
		return folder.getMessage(msgNum);
	}
	
	@Override
	public Message[] getMessages() throws MessagingException {
		if(!folder.isOpen())
			throw new MessagingException("Already colsed folder");
		return folder.getMessages();
	}


	@Override
	public Message[] getRecentMessages(int count) throws MessagingException {
		if(!folder.isOpen())
			throw new MessagingException("Already colsed folder");
		int folderSize = folder.getMessageCount();
		return folder.getMessages(folderSize-count+1,folderSize);
	}

	@Override
	public int getMessageCount() throws MessagingException {
		if(!folder.isOpen())
			throw new MessagingException("Already colsed folder");
		return folder.getMessageCount();
	}

	@Override
	public Folder getFolder() throws MessagingException {
		if(!folder.isOpen())
			throw new MessagingException("Already colsed folder");
		return folder;
	}

	@Override
	public Store getStore() throws MessagingException {
		if(!folder.isOpen())
			throw new MessagingException("Already colsed folder");
		return store;
	}
}
