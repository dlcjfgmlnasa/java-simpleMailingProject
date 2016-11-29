package 메일링프로젝트;

import java.util.List;

import javax.mail.Folder;
import javax.mail.Message;

public class Main {
	public static void main(String[] args) throws Exception{
		/*
		IMAPAgent imapAgent = new IMAPAgent("imap.naver.com", "dlcjfgmlnasa", "zxc~2051801");
		MessageList mList = new MessageList();
		for(String folderName : imapAgent.getFolderNameList()){
			Folder folder = imapAgent.getFolder(folderName);
			List<Message> messages = mList.getUnReadMessages(folder);
			for(Message m : messages){
				System.out.println(m.getSubject());
			}
		}
		*/
		new LoginPage("login").setVisible(true);
	}
}
