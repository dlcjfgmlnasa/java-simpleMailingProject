package 메일링프로젝트;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.*;

public class MyAuthenticator extends Authenticator{
		private String id;
		private String password;
		
		public MyAuthenticator(String id, String pw){
			this.id = id;
			this.password = pw;
		}
		
		public PasswordAuthentication getPasswordAuthentication(){
			  return new PasswordAuthentication(id, password);
		}
}
