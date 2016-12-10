package 메일링프로젝트;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

class LoginPage extends JFrame{
	private static final long serialVersionUID = -2931433869439877345L;
	private HashMap<String, String> imapMap = new HashMap<>();
	
	@SuppressWarnings("deprecation")
	public LoginPage(String loginTitleName){
		imapMap.put("naver.com", "imap.naver.com");
		imapMap.put("gmail.com", "imap.gmail.com");
		imapMap.put("hanmail.net", "imap.hanmail.net");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		setTitle(loginTitleName);
		
		JLabel programName = new JLabel("     Simple Mailing ");
		programName.setFont(new Font("맑은 고딕",1,20));
		JLabel jLabel1 = new JLabel("아이디   :   ");
		JTextField jTextField1 = new JTextField(10);
		JLabel jLabel2 = new JLabel("비밀번호 : ");
		JPasswordField jPasswordField = new JPasswordField(10);
		JButton button2 = new JButton("로그인");
		
		
		button2.addActionListener(e ->{
			if("".equals(jTextField1.getText()) || "".equals(jPasswordField.getText())){
				JOptionPane.showMessageDialog(null, "아이디와 페스워드를 입력하시오!!","Error!!",JOptionPane.ERROR_MESSAGE);
				new LoginPage("login").setVisible(true);
				return;
			} else {
				if(!isValidEmail(jTextField1.getText())){
					JOptionPane.showMessageDialog(null, "이메일 형식이 아닙니다!!","Error!!",JOptionPane.ERROR_MESSAGE);
					new LoginPage("login").setVisible(true);
					return;
				}


				String mail = jTextField1.getText().split("@")[1];
				String id = jTextField1.getText().split("@")[0];
				String passwd = jPasswordField.getText();
				User.mail = mail;
				User.id = id;
				User.passwd = passwd;
				
				LoginDBConnet loginDBConnet = new LoginDBConnet();
				loginDBConnet.DBconnect();
				folderDBConnet folderDBConnet = new folderDBConnet();
				folderDBConnet.DBconnect();
	
				int count = loginDBConnet.count("select count(if(id='"+User.id+"',e_mail='"+User.mail+"',passward='"+User.passwd+"')) from user;");
				HashMap<String,Integer> messageMap = new HashMap<>();
				
				try{
					IMAPAgent imapAgent = new IMAPAgent(imapMap.get(User.mail), User.id, User.passwd);
					for(String folderName : imapAgent.getFolderNameList()){
						messageMap.put(folderName,imapAgent.getFolder(folderName).getMessageCount());
					}
					
					if(count == 0){
						loginDBConnet.insert("insert into user (id,e_mail,passward) values (?,?,?);");
						for(String folderName : messageMap.keySet()){
							folderDBConnet.insert("insert into folder (e_mail,foldername,count) values ('" 
														+ User.id+"@"+User.mail + "', '"
														+ folderName+"',"
														+imapAgent.getFolder(folderName).getMessageCount()+");");
						}
					} 
				} catch(MessagingException e1){
					JOptionPane.showMessageDialog(null, "로그인 실패!!","Error!!",JOptionPane.ERROR_MESSAGE);
					setVisible(false);
					new LoginPage("login").setVisible(true);
					return;
				}
				
				try {
					setVisible(false);
					new MainFrame("mailing Program 0.1v").setVisible(true);
				} catch (MessagingException e1) {
					e1.printStackTrace();
				}
				
			}
		});
		InputStream input = getDBimage();
		ImageIcon icon = null;
		try{
			BufferedImage srcImg = ImageIO.read(input);
			Image image = srcImg.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
			image.getScaledInstance(10, 10, 0);
			icon = new ImageIcon(image);
			
		} catch(Exception e){}
		JLabel label = new JLabel(icon);
		label.setBounds(30, 270, 100,30);
		add(programName);
		add(label);
		add(jLabel1);
		add(jTextField1);
		add(jLabel2);
		add(jPasswordField);
		add(button2);
		setSize(250,200);
		setLocationRelativeTo(null);
	}
	
	 public static boolean isValidEmail(String email) {
		  boolean err = false;
		  String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";   
		  Pattern p = Pattern.compile(regex);
		  Matcher m = p.matcher(email);
		  if(m.matches()) {
		   err = true; 
		  }
		  return err;
	}
	 
	public static InputStream getDBimage() {
		java.sql.Connection con = null;
		java.sql.Statement statement = null;
		java.sql.ResultSet rs = null;
		InputStream input = null;
			
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mail?autoReconnect=true&useSSL=false", 
										"root", "zxc2051801");
			statement = con.createStatement();
			String sql = "select thumb from blob_test where img_no = 1";
			rs = statement.executeQuery(sql);
			if (rs.next()) {
				input = rs.getBinaryStream("thumb");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
		return input;
	}
}
