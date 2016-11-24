package 메일링프로젝트;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;



public class main {
	public static void main(String[] args) throws Exception{
		MyAuthenticator ma = new MyAuthenticator("dlcjfgmlnasa@naver.com", "zxc~2051801");
		IMAPAgent mailAgent = new IMAPAgent("imap.naver.com", "dlcjfgmlnasa@naver.com", "zxc~2051801");
		mailAgent.open();
		Message[] message = mailAgent.getMessage();
		Address[] address = message[0].getFrom();
		InternetAddress addr =(InternetAddress)address[0];
		System.out.println(addr.getPersonal());
		System.out.println(addr.getAddress());
		System.out.println();
		
		Store s = mailAgent.getStore();
		Folder[] folders = s.getDefaultFolder().list();
		for(Folder folder : folders){
			Folder tempFolder = mailAgent.getFolder(folder.getFullName());
			System.out.println(folder.getFullName());
			System.out.println(tempFolder.getMessageCount());
		}
		
		mailAgent.close();
	}
}


