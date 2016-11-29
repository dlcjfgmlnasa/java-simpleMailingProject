package 메일링프로젝트;

import java.awt.FlowLayout;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.MessagingException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

class LoginPage extends JFrame{
	public LoginPage(String loginTitleName){
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		setTitle(loginTitleName);
		
		JLabel jLabel1 = new JLabel("아이디   :   ");
		JTextField jTextField1 = new JTextField(10);
		JLabel jLabel2 = new JLabel("비밀번호 : ");
		JPasswordField jPasswordField = new JPasswordField(10);
		JButton button = new JButton("회원가입");
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
				try {
					setVisible(false);
					new MainFrame("mailing Program 0.1v").setVisible(true);
				} catch (MessagingException e1) {
					e1.printStackTrace();
				}
			}
		});
		
		add(jLabel1);
		add(jTextField1);
		add(jLabel2);
		add(jPasswordField);
		//add(button);
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
}
