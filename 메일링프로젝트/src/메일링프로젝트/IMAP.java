package 메일링프로젝트;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;

public class IMAP implements MailProto{
	protected Session session;
	protected Store store;
	protected Folder folder;

	@Override
	public void open(MyAuthenticator myAuthenticator) throws MessagingException {
		Properties props = new Properties();
		return ;
		
	}

	@Override
	public Message close() throws MessagingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message[] getMessages(int msgNum) throws MessagingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Message[] getRecentMessages(int count) throws MessagingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getMessageCount() throws MessagingException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getUID(Message msg) throws MessagingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Store getStore() throws MessagingException {
		// TODO Auto-generated method stub
		return null;
	}

}
