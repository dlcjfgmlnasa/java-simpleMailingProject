package 메일링프로젝트;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

interface MailProto{
	void open(String host, String id, String passwd) throws MessagingException;
	void close() throws MessagingException;
	Message getMessages(int msgNum) throws MessagingException;
	Message[] getMessages() throws MessagingException;
	Message[] getRecentMessages(int count) throws MessagingException;
	int getMessageCount() throws MessagingException;
	Folder getFolder() throws MessagingException;
	Store getStore() throws MessagingException;
}