package 메일링프로젝트;

import static java.util.stream.Collectors.toList;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class SendEmail extends JFrame{
	private static final long serialVersionUID = 1L;
	private HashMap<String,String> smtpMap = new HashMap<>();
	private String[] hibitionList = new String[]{".bat",".cmd",".com",".cpl",".exe",".js",".scr",".vbs",".wsf"};
	private Properties props;
	private JTextField sendtoField = new JTextField(30);
	private JTextField subjectField = new JTextField(30);
	private JTextArea  jTextArea  = new JTextArea(30,50);
	private JScrollPane jScrollpane = new JScrollPane(jTextArea);
	private JFileChooser chooser = new JFileChooser("첨부파일");
	private List<File> fileList = new ArrayList<>();
	private JList<String> fileNameList = new JList<>();
	private JScrollPane jScrollPane2;
	private JButton jButton2 = new JButton("첨부");
	private JButton jButton3 = new JButton("리셋");
	private JButton jButton = new JButton("발송");
	private HashMap<String, String> contentsBox;
	private String sendTo;
	private String id;
	private String email;
	
	{
		smtpMap.put("naver.com","smtp.naver.com");
		smtpMap.put("gmail.com","smtp.gmail.com");
		smtpMap.put("hanmail.net","smtp.daum.com");
		
		props = new Properties();
		
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.user" , User.id);
	   	props.put("mail.smtp.auth", "true");
	   	props.put("mail.smtp.host",smtpMap.get(User.mail));
	   	props.put("mail.smtp.port", "465");
    	props.put("mail.smtp.starttls.enable", "true");
    	props.put("mail.smtp.starttls.required", "true");
    	props.put("mail.smtp.socketFactory.port", "465");
    	props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
    	props.put("mail.smtp.socketFactory.fallback", "false");
	}
	
	public void viewFrame(){
		setLayout(new BorderLayout());
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((dim.width/2)-(getWidth()/2), (dim.height/2)-(getHeight()/2));
		setTitle("메일 쓰기");
		JPanel panel = new JPanel(new BorderLayout());
		JPanel panel1 = new JPanel(new FlowLayout());
		JPanel panel2 = new JPanel(new FlowLayout());
		JPanel panel3 = new JPanel(new FlowLayout());
		JPanel panel4 = new JPanel(new BorderLayout());
		panel1.add(new JLabel(" 받는 사람   "));
		panel1.add(sendtoField);
		panel2.add(new JLabel(" 제        목  "));
		panel2.add(subjectField);
		jScrollPane2 = new JScrollPane(fileNameList,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		panel4.add(jButton2,BorderLayout.NORTH);
		panel4.add(jButton3,BorderLayout.CENTER);
		panel3.add(panel4);
		panel3.add(jScrollPane2);
		
		panel.add(panel1,BorderLayout.NORTH);
		panel.add(panel2,BorderLayout.CENTER);
		panel.add(panel3,BorderLayout.SOUTH);
		
		
		//첨부 버튼
		jButton2.addActionListener(e -> {
			int returnVal = chooser.showOpenDialog(this);
			if( returnVal == JFileChooser.APPROVE_OPTION){
                File file = chooser.getSelectedFile();
                String extension = file.getName();
                int begin = extension.indexOf(".");
                extension = extension.substring(begin, extension.length());
                
                if(fileList.size() == 10){
                	JOptionPane.showMessageDialog(null, "10개까지 첨부가 가능합니다.","Error!!",JOptionPane.ERROR_MESSAGE);
                	return;
                }
                for(String hibition : hibitionList){
                	if(extension.trim().equals(hibition)){
                		JOptionPane.showMessageDialog(null, "잘못된 형식를 첨부하였습니다.","Error!!",JOptionPane.ERROR_MESSAGE);
                		JOptionPane.showMessageDialog(null, " .bat .cmd .com .cpl .exe .js .scr .vbs .wsf 금지!!","Error!!",JOptionPane.ERROR_MESSAGE);
                		return;
                	}
                }
                
                fileList.add(file);
                
                //중복 제거
                fileList = fileList.stream()
                				   .distinct()
                				   .collect(toList());
                
                //파일리스트 절대경로 추출
                List<String> fileNames = fileList.stream()
                								  .map(f -> f.getAbsolutePath())
                								  .distinct()
                								  .collect(toList());
                Vector<String> v = new Vector<>(fileNames);
                fileNameList.setListData(new Vector<>());
                fileNameList.setListData(v);
            }
		});
		
		//리셋 버튼
		jButton3.addActionListener(e -> {
			fileList = new ArrayList<File>();
			fileNameList.setListData(new Vector<>());
		});
		
		jButton.addActionListener(e -> {send();});	//전송 버튼
		add(panel,BorderLayout.NORTH);
		add(jScrollpane,BorderLayout.CENTER);
		add(jButton,BorderLayout.SOUTH);
		setSize(450, 600);
		setVisible(true);
	}
	
	public SendEmail(HashMap<String, String> temp){
		viewFrame();
		contentsBox = temp;
		id = contentsBox.get("보낸이");
		email = contentsBox.get("보낸이 주소");
		sendTo = email+" <"+id+">";
		sendtoField.setText(sendTo);
		
	}
	
	public SendEmail(){
		viewFrame();
	}
	
	private List<InternetAddress> makeSendToList(String To) throws Exception{
		List<String> toList = new ArrayList<String>();
		for(String to : To.split("/")){
			try{
				to = to.substring(0,To.indexOf("<")).trim();
				if(!isValidEmail(to.trim())){
					throw new Exception();
				}
			} catch(StringIndexOutOfBoundsException e){}
			toList.add(to);
		}
		List<InternetAddress> temp = new ArrayList<>();
		for(int i=0 ; i < toList.size() ; i++){
			temp.add(new InternetAddress(toList.get(i)));
		}
		
		return temp;
	}
	
	private void send(){
		String subject = subjectField.getText();
		String From = User.id+"@"+User.mail;
		String To = sendtoField.getText();
		String msgText = jTextArea.getText();
		InternetAddress[] AddressList = null;
		
		
		try{
			List<InternetAddress> aList = makeSendToList(To);
			AddressList = new InternetAddress[aList.size()];
			for(int i=0 ; i<aList.size() ; i++){
				AddressList[i] = aList.get(i);
			}
		} catch(Exception e){
			JOptionPane.showMessageDialog(null, "메일 형식이 잘못되었습니다!!","Error!!",JOptionPane.ERROR_MESSAGE);
			sendtoField.setText("");
			return;
		}
		
		try{
			Session mailSession = Session.getInstance(props, null);
			@SuppressWarnings("static-access")
			Message mimeMessage = new MimeMessage(mailSession.getDefaultInstance(props, 
					new MyAuthenticator(User.id+"@"+User.mail, User.passwd)));
			mimeMessage.setFrom(new InternetAddress(From));
			mimeMessage.setRecipients(Message.RecipientType.TO, AddressList);
			mimeMessage.setSubject(subject);
			mimeMessage.setSentDate(new Date());
			
			Multipart mp = new MimeMultipart();
			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setContent(msgText, "text/html;charset=euc-kr" );
			mp.addBodyPart(mbp);
			
			for(File file : fileList){
				try{
					mbp = new MimeBodyPart();
					DataSource fds = new FileDataSource(file.getAbsolutePath()) ;
					mbp.setDataHandler(new DataHandler(fds));
					mbp.setHeader("Content-ID","<choels>");
					mbp.setFileName(MimeUtility.encodeText(file.getName(),"EUC-KR","B"));
				} catch(UnsupportedEncodingException e){
					mbp.setFileName(file.getName());
				} finally {
					mp.addBodyPart(mbp);
				}
			}
			
			
			mimeMessage.setContent(mp);
		
			Transport.send(mimeMessage);
			JOptionPane.showMessageDialog(null, "정상적으로 발신되었습니다","Send Message",JOptionPane.DEFAULT_OPTION);
		} catch(MessagingException e){
			JOptionPane.showMessageDialog(null, "발송 실패!!","Error!!",JOptionPane.ERROR_MESSAGE);
		} finally{
			setVisible(false);
		}
	}
	
	 private boolean isValidEmail(String email) {
		  boolean err = false;
		  String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";   
		  Pattern p = Pattern.compile(regex);
		  Matcher m = p.matcher(email);
		  if(m.matches()) {
		   err = true; 
		  }
		  return err;
	}
}
