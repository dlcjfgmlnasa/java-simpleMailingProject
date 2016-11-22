package 메일링프로젝트;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;



public class main {
	public static void main(String[] args){
		MyAuthenticator ma = new MyAuthenticator("dlcjfgmlnasa@naver.com", "zxc~2051801");
		IMAP imap = new IMAP();
		try {
			imap.open(ma);
		} catch(Exception e){
			
		} 
	}
}
