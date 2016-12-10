package ∏ﬁ¿œ∏µ«¡∑Œ¡ß∆Æ;

import java.awt.BorderLayout;
import java.awt.Font;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ContentsView extends JFrame{
	private static final long serialVersionUID = 1L;
	private String filePath = "./temp/temp.html";
	private TableContents table;
	private String subject;
	private String sendPeople;
	private String sendTo;
	private String time;
	private boolean attach;
	
	public ContentsView(TableContents table){
		setLayout(new BorderLayout());
		this.table = table;
		Savefile(table.getContents());
		subject = table.getSubject();
		sendPeople = table.getSender();
		sendTo = table.getSenderURL();
		time = table.getTime();
		attach = table.getAttach();
		JTextArea jTextArea = new JTextArea();
		JScrollPane jScrollPane = new JScrollPane(jTextArea);
		jTextArea.setText(table.getContents());
		
		JPanel jpanel2 = new JPanel(new BorderLayout());
		JLabel title = new JLabel(subject);
		title.setFont(new Font("∏º¿∫ ∞ÌµÒ", 1, 20));
		jpanel2.add(title,BorderLayout.CENTER);
		JLabel sender = new JLabel(sendPeople+"< " + sendTo + " >"+"\t"+time);
		sender.setFont(new Font("∏º¿∫∞ÌµÒ", 1, 15));
		jpanel2.add(sender,BorderLayout.SOUTH);
		
		add(jpanel2,BorderLayout.NORTH);
		add(jScrollPane,BorderLayout.CENTER);
		setTitle("∏ﬁ¿œ ≥ªøÎ");
		setSize(500, 500);
		setVisible(true);
	}
	
	private void Savefile(String contents){
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
			bw.write(contents);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
