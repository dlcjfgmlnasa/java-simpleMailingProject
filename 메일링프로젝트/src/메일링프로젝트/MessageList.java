package 메일링프로젝트;

import java.util.Arrays;
import java.util.List;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.FlagTerm;

class MessageList{
	@SuppressWarnings("static-access")
	private void folderOpen(Folder folder) {
		try{
			folder.open(folder.READ_ONLY);
		} catch(MessagingException e){
			e.printStackTrace();
		}
	}
	public List<Message> getMessageList(Folder folder) throws MessagingException{
		folderOpen(folder);
		return Arrays.asList(folder.getMessages());
	}
	
	public List<Message> getUnReadMessages(Folder folder) throws MessagingException{
		folderOpen(folder);
		return Arrays.asList(folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false)));
	}
	
	public List<Message> getRecentMessages(Folder folder, int count) throws MessagingException{
		folderOpen(folder);
		int folderSize = folder.getMessageCount();
		int setSize = folderSize > count ? folderSize-count+1 : folderSize-1;
		if(folderSize == 0) return Arrays.asList();
		return Arrays.asList(folder.getMessages(setSize,folderSize));
	}
	
	public void recentMessage(Folder folder) throws MessagingException{
		folder.getNewMessageCount();
	}
}
