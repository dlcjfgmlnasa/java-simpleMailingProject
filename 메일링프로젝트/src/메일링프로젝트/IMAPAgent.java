package 메일링프로젝트;

import javax.mail.MessagingException;

class IMAPAgent extends IMAP{
	public IMAPAgent(String host, String id, String passwd) throws MessagingException{
		connet(host,id,passwd);
	}
	
	
}

