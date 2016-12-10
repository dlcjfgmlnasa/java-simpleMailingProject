package 메일링프로젝트;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;

public class MailContents {
	public static String getMailContent(Part part) throws MessagingException, IOException{
		StringBuffer bodytext = new StringBuffer();// 메일 내용 맡기다.
		
		String contenttype = part.getContentType();
		int nameindex = contenttype.indexOf("name");
		boolean conname = false;
		if (nameindex != -1) conname = true;
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
	    } else {}
		
		return bodytext.toString();
	}
	
	
	public static boolean isContainAttach(Part part) throws MessagingException,IOException {
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
	
	/*
	  public static void saveAttachMent(Part part) throws Exception {
	      String fileName = "";
	      if (part.isMimeType("multipart/*")) {
	          Multipart mp = (Multipart) part.getContent();
	          for (int i = 0; i <mp.getCount(); i++) {
	              BodyPart mpart = mp.getBodyPart(i);//주요 부분 얻은 처리
	              String disposition = mpart.getDisposition();
	              if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT)) || (disposition .equals(Part.INLINE)))) {//ATTACHMENT첨부 파일, INLINE 않습니다.
	                  fileName = mpart.getFileName();
	                  if (fileName.toLowerCase().indexOf("gb18030") != -1) {
	                      fileName = MimeUtility.decodeText(fileName);
	                  }
	                 // saveFile(fileName, mpart.getInputStream());
	              } else if (mpart.isMimeType("multipart/*")) {
	                  saveAttachMent(mpart);
	              } else {
	                  fileName = mpart.getFileName();
	                  if ((fileName != null)
	                            && (fileName.toLowerCase().indexOf("GB18030") != -1)) {
	                      fileName = MimeUtility.decodeText(fileName);
	               //       saveFile(fileName, mpart.getInputStream());
	                  }
	              }
	          }
	      } else if (part.isMimeType("message/rfc822")) {
	    	  saveAttachMent((Part) part.getContent());
	      }
	 }
	 */
}
