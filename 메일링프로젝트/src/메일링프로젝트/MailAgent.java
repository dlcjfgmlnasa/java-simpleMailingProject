package 메일링프로젝트;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

public class MailAgent {
	protected MailProto mailProto;
	protected String host;
	protected String id;
	protected String passwd;
	
	
	public MailAgent(MailProto mailProto, String host, String id, String passwd){
		if(mailProto == null || host == null || id == null || passwd == null){
			throw new IllegalArgumentException();
		}
		this.mailProto = mailProto;
		this.host = host;
		this.id = id;
		this.passwd = passwd;
	}
	
	public void open() throws MessagingException{
		mailProto.open(host, id, passwd);
	}
	
	public void close() throws MessagingException{
		mailProto.close();
	}
	
	public Message getMessage(int msgNum) throws MessagingException{
		return mailProto.getMessages(msgNum);
	}
	
	public Message[] getMessage() throws MessagingException{
		return mailProto.getMessages();
	}
	
	public Message[] getRecentMessage(int count) throws MessagingException{
		return mailProto.getRecentMessages(count);
	}
	
	public int getMessageCount() throws MessagingException{
		return mailProto.getMessageCount();
	}
	
	public Folder getDefaultFolder() throws MessagingException{
		return mailProto.getFolder();
	}
	
	public Store getStore() throws MessagingException{
		return mailProto.getStore();
	}
	
	public void setMailProto(MailProto mailProto){
		this.mailProto = mailProto;
	}
}
