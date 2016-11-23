package 메일링프로젝트;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class loginPage extends JFrame {
	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					loginPage frame = new loginPage();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public loginPage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 337, 253);
		setTitle("로그인 페이지");
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblLogin = new JLabel("아이디");
		lblLogin.setBounds(31, 52, 57, 15);
		contentPane.add(lblLogin);
		
		JLabel lblPasswd = new JLabel("페스워드");
		lblPasswd.setBounds(31, 98, 57, 15);
		contentPane.add(lblPasswd);
		
		textField = new JTextField();
		textField.setBounds(160, 49, 149, 21);
		contentPane.add(textField);
		textField.setColumns(10);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(160, 95, 149, 21);
		contentPane.add(passwordField);
		
		JButton accounts = new JButton("회원가입");
		accounts.setBounds(12, 186, 93, 23);
		contentPane.add(accounts);
		
		JButton login = new JButton("로그인");
		login.setBounds(226, 186, 83, 23);
		contentPane.add(login);
	}
}
