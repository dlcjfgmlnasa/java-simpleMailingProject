package 메일링프로젝트;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;


class MainFrame extends JFrame{
	JTabbedPane jTabbedPane;
	HashMap<String, String> imapMap = new HashMap<>();
	public void settingMailProtocol(){
		imapMap.put("naver.com", "imap.naver.com");
		imapMap.put("gmail.com", "imap.gmail.com");
		imapMap.put("hanmail.net", "imap.hanmail.net");
	}
	
	public MainFrame(String programName) throws MessagingException{
		setTitle(programName);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		settingMailProtocol();
		String host = imapMap.get(User.mail);
		String mailAddress = User.id+"@"+User.mail;
		String passwd = User.passwd;
		IMAPAgent imapAgent = null;
		
		try{
			imapAgent = new IMAPAgent(host, mailAddress, passwd);
		} catch(MessagingException e){
			JOptionPane.showMessageDialog(null, "로그인 실패!!","Error!!",JOptionPane.ERROR_MESSAGE);
			new LoginPage("login").setVisible(true);
		}
		
		jTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		List<String> folderListName = imapAgent.getFolderNameList();
		List<Folder> folderList = new ArrayList<Folder>();
		for(String folderName : folderListName){
			folderList.add(imapAgent.getFolder(folderName));
		}
		
		for(Folder folder : folderList){
			jTabbedPane.addTab(folder.getFullName(),new PanelOnTable(folder));
		}
		
		add(jTabbedPane);
		setSize(1000,700);
		imapAgent.colse(folderList);
	}
}
