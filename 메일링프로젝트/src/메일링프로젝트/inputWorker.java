package 메일링프로젝트;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;

class inputWorker implements Runnable{
	final int defaultCount = 100;
	private String folderName;
	private String host;
	private MessageList mList = new MessageList();
	private int preContentsCount=0;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private TableDBConnet TableDBConnet;
	private static StringBuffer bodytext;
	
	public inputWorker(String folderName, TableDBConnet table){
		this.TableDBConnet = table;
		this.folderName = folderName;
		this.host = MainFrame.imapMap.get(User.mail);

		preContentsCount = TableDBConnet.count("select count from folder where e_mail = '"+User.id+"@"+User.mail+"'"
													+" and folderName = '"+folderName+"';");
	}
	
	public void run(){
		try{
			TableDBConnet.DBconnect();
			inputMessageContents();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	private void inputMessageContents() throws MessagingException, IOException{
		Folder folder = new IMAPAgent(host, User.id, User.passwd).getFolder(folderName);
		int messageCount = folder.getMessageCount();
		int mCount=0;
		
		if(messageCount == 0) return; //message내용이 아무것도 없으면 돌려보낸다.
		int contentsBoxCount = TableDBConnet.count("select count(*) from content where e_mail='"+User.id+"@"+User.mail+"'"
														+" and passward = '"+User.passwd+"'"
														+" and foldername = '"+folderName+"';");

		if(contentsBoxCount == 0 && preContentsCount != 0){ //처음 저장할때 각 폴더당 100개만 저장한다.
			mCount = messageCount < defaultCount ? messageCount : defaultCount;
		} else{
			mCount = messageCount - preContentsCount;
		}
		//folder count 갱신
		folderDBConnet fconnet = new folderDBConnet();
		fconnet.DBconnect();
		fconnet.update("update folder set count = "+messageCount+" where e_mail='"+User.id+"@"+User.mail
				+"' and foldername = '"
				+folderName+"';");
		
		List<Message> messages = mList.getRecentMessages(folder, mCount);
		for(Message message : messages){
			bodytext = new StringBuffer();
			try{
				boolean reading = Arrays.asList(message.getFlags().getSystemFlags()).contains(Flags.Flag.SEEN);
				String subject = message.getSubject();
				String sender = ((InternetAddress)message.getFrom()[0]).getPersonal();
				String senderURL = ((InternetAddress)message.getFrom()[0]).getAddress();
				getMailContent((Part)message);
				String from_time = dateFormat.format(message.getSentDate());
				boolean attach = isContainAttach((Part)message);
				inputDBContents(reading, subject, sender, senderURL, bodytext, from_time, attach);
			} catch(NullPointerException e){
				e.printStackTrace();
			}
		}
		
	}
	
	private void inputDBContents(boolean reading, String subject, String sender, 
												  String senderURL, StringBuffer contents, String from_time, boolean attach){
		String query = "insert into content (e_mail,passward,folderName,reading,subject,sender,senderURL,from_time,contents,attach) values"
								+"(?,?,?,?,?,?,?,?,?,?)";
		System.out.println(query);
		
		TableDBConnet.insert(query,folderName,reading, subject, sender, senderURL, contents, from_time, attach);
	}
	
	public static void getMailContent(Part part) throws MessagingException,IOException {
	      String contenttype = part.getContentType();
	      int nameindex = contenttype.indexOf("name");
	      boolean conname = false;
	      if (nameindex != -1)
	          conname = true;
	      if (part.isMimeType("text/plain") && !conname) {
	          bodytext.append((String) part.getContent());
	      } else if (part.isMimeType("text/html") && !conname) {
	          bodytext.append((String) part.getContent());
	      } else if (part.isMimeType("multipart/*")) {
	          Multipart multipart = (Multipart) part.getContent();
	          int counts = multipart.getCount();
	          for (int i = 0; i <counts; i++) {
	              getMailContent(multipart.getBodyPart(i));
	          }
	      } else if (part.isMimeType("message/rfc822")) {
	          getMailContent((Part) part.getContent());
	      } else {
	      }
	  }
	
	
	public static boolean isContainAttach(Part part) throws MessagingException, IOException {
	      boolean attachflag = false;
	      if (part.isMimeType("multipart/*")) {
	          Multipart mp = (Multipart) part.getContent();
	          for (int i = 0; i <mp.getCount(); i++) {
	              BodyPart mpart = mp.getBodyPart(i);
	              String disposition = mpart.getDisposition();
	              if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE))))
	                  attachflag = true;
	              else if (mpart.isMimeType("multipart/*")) {
	                  attachflag = isContainAttach((Part) mpart);
	              } else {
	                  String contype = mpart.getContentType();
	                  if (contype.toLowerCase().indexOf("application") != -1)
	                      attachflag = true;
	                  if (contype.toLowerCase().indexOf("name") != -1)
	                      attachflag = true;
	              }
	          }
	      } else if (part.isMimeType("message/rfc822")) {
	          attachflag = isContainAttach((Part) part.getContent());
	      }
	      return attachflag;
	  }
}