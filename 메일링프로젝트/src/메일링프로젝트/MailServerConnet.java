
package ���ϸ�������Ʈ;

import javax.mail.MessagingException;

interface MailServerConnect{
	public void connet(String host, String id, String passwd) throws MessagingException;
}