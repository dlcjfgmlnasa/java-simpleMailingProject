
package 메일링프로젝트;

import javax.mail.MessagingException;

interface MailServerConnect{
	public void connet(String host, String id, String passwd) throws MessagingException;
}