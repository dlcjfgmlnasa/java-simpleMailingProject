package 메일링프로젝트;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;


class MainFrame extends JFrame{
	private static final long serialVersionUID = 1L;
	private JTabbedPane jTabbedPane;
	private TableDBConnet table = new TableDBConnet();
	
	public static HashMap<String, String> imapMap = new HashMap<>();
	public void settingMailProtocol(){
		imapMap.put("naver.com", "imap.naver.com");
		imapMap.put("gmail.com", "imap.gmail.com");
		imapMap.put("hanmail.net", "imap.hanmail.net");
	}
	
	public MainFrame(String programName) throws MessagingException{
		setTitle(programName);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width/2)-(getWidth()/2), (dim.height/2)-(getHeight()/2));
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		settingMailProtocol();
		String host = imapMap.get(User.mail);
		String mailAddress = User.id+"@"+User.mail;
		String passwd = User.passwd;
		IMAPAgent imapAgent = new IMAPAgent(host, mailAddress, passwd);
		jTabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		List<String> folderListName = imapAgent.getFolderNameList();
		List<Folder> folderList = new ArrayList<Folder>();
		table.DBconnect();
		
		
		for(String folderName : folderListName){
			if("Deleted Messages".equals(folderName)) continue;
			new inputWorker(folderName,table).run();
		}
		
		for(String folderName : folderListName){
			folderList.add(imapAgent.getFolder(folderName));
		}
		
		for(Folder folder : folderList){
			jTabbedPane.addTab(folder.getFullName(),new PanelOnTable(folder));
		}
		
		add(jTabbedPane);
		setSize(1050,700);
		imapAgent.colse(folderList);
	}
}

