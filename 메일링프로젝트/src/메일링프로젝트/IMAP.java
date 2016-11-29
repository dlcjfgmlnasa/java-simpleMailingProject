package 메일링프로젝트;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;


class IMAP implements MailServerConnect, MailProto{
	static Properties props;
	protected Session session;
	protected Store store;
	protected Folder folder;
	static{
		props = new Properties();
		props.setProperty("mail.store.protocol", "imaps");
		props.put("mail.imap.fetchsize", "1024000");
	}
	public void connet(String host, String id, String passwd) throws MessagingException{
		session = Session.getInstance(props,null);
		store = session.getStore();
		store.connect(host,id,passwd);
	}
	
	public List<String> getFolderNameList() throws MessagingException{
		Folder[] folder = store.getDefaultFolder().list();
		List<String> filelist = new ArrayList<String>();
		for(Folder file : folder){
			filelist.add(file.getFullName());
		}
		return filelist;
	}
	
	public Folder getFolder(String folderName) throws MessagingException{
		return store.getFolder(folderName);
	}
	
	public Store getStore() throws MessagingException{
		if(!store.isConnected()){
			throw new MessagingException("not connet Store");
		}
		return store;
	}
	

	@Override
	public void colse(List<Folder> folderlist) throws MessagingException {
		for(Folder folder : folderlist){
			if(folder != null && folder.isOpen()){
				folder.close(true);
			} 
		}
		if(store != null && store.isConnected()){
			store.close();
		}
	}
}