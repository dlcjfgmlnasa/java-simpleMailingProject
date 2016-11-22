package 메일링프로젝트;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;

interface MailProto{
	void open(MyAuthenticator ma) throws MessagingException;
	Message close() throws MessagingException;
	Message[] getMessages(int msgNum) throws MessagingException;
	Message[] getRecentMessages(int count) throws MessagingException;
	int getMessageCount() throws MessagingException;
	String getUID(Message msg) throws MessagingException;
	Store getStore() throws MessagingException;
}