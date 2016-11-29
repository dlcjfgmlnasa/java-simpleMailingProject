package 메일링프로젝트;

import java.util.List;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.mail.Store;

interface MailProto{
	public List<String> getFolderNameList() throws MessagingException;
	public void colse(List<Folder> folderlist) throws MessagingException;
	public Folder getFolder(String folderName) throws MessagingException;
	public Store getStore() throws MessagingException;
}