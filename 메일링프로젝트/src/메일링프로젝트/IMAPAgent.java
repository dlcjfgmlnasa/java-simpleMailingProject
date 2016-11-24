package 메일링프로젝트;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.FlagTerm;

public class IMAPAgent extends MailAgent{
	public IMAPAgent(String host, String id, String passwd){
		super(new IMAP(),host,id,passwd);
	}
	
	public Folder getFolder(String name) throws MessagingException{
		return getStore().getFolder(name);
	}
	
	public Message[] getUnReadMessages() throws MessagingException{
		return getDefaultFolder().search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
	}
	
	public void createFolder(String name) throws MessagingException{
		Folder folder = getStore().getDefaultFolder();
		if(!folder.isOpen()){
			folder.create(Folder.HOLDS_MESSAGES);
		}
	}
	
	public void moveMessage(Message[] messages,Folder src,Folder dest) throws MessagingException{
		src.copyMessages(messages, dest);
		src.setFlags(messages, new Flags(Flags.Flag.DELETED), true);
	}
	
	public void setSeenFlag(Message message) throws MessagingException{
		setFlag(message, Flags.Flag.SEEN, true);
	}
	
	public void setUnSeenFlag(Message message) throws MessagingException{
		setFlag(message, Flags.Flag.SEEN, false);
	}
	
	public void setFlag(Message message, Flag flag, boolean set) throws MessagingException{
		message.setFlag(flag,set);
	}
	
	public boolean hasNewMessage(Folder folder) throws MessagingException{
		return folder.hasNewMessages();
	}
}
