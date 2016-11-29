package 메일링프로젝트;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

class MYSQLMailcontrol implements controlDB{
	private final String userID;
	private final String userPassward;
	private final String dbURL;
	private java.sql.Connection con = null;
	private java.sql.Statement statement = null;
	private java.sql.ResultSet rs = null;
	
	public MYSQLMailcontrol(String id, String passwd, String url) throws ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver"); 
		userID = id;
		userPassward = passwd;
		dbURL = url;                      
	}
	
	@Override
	public void DBconnet(){
		try{
			con =  DriverManager.getConnection(dbURL,userID,userPassward);
			if(con != null){
				System.out.println("db접속성공");
			}
			statement = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	public void close(){
		try {
			con.close();
			statement.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public int count(String query) {
		int count=0;
		try{
			rs = statement.executeQuery(query);
			while(rs.next()){
				count = rs.getInt(1);
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return count;
	}
	
	@Override
	public void update(String query) {
		// TODO Auto-generated method stub.
	}
	
	@Override
	public void insert(String query) {
		
	}
	

	
	public void initialization() {
		try {
			String name = User.id;
			HashMap<String, String> imapMap = new HashMap<>();
			imapMap.put("naver.com", "imap.naver.com");
			imapMap.put("gmail.com", "imap.gmail.com");
			imapMap.put("hanmail.net", "imap.hanmail.net");
			
			IMAPAgent imapAgent = new IMAPAgent(imapMap.get(User.mail), User.id+"@"+User.mail, User.passwd);
			List<String> folderListName = imapAgent.getFolderNameList();
			List<Folder> folderList = new ArrayList<Folder>();
			MessageList msList = new MessageList();
			for(String folderName : folderListName){
				folderList.add(imapAgent.getFolder(folderName));
			}
			String tableName = User.id+"_"+User.mail+"table";
			try{
				rs = statement.executeQuery("show tables like "+"'"+tableName+"'");
				int count=0;
				while(rs.next()) count=1;
				if(count == 0){
					String query = "create table "+tableName+"( " + 
							"no INT PRIMARY KEY AUTO_INCREMENT," + 
							"reading bool,"+
							"folderName varchar(30) ,"+
							"subject varchar(100) ,"+
							"sender varchar(100) ,"+
							"senderURL varchar(100),"+
							"from_time timestamp"+
							");";
					statement.execute(query);
					for(Folder folderName : folderList){
						String fName = folderName.getFullName();
						List<Message> messages = msList.getMessageList(folderName);
						for(Message message : messages){
							Flags flag = message.getFlags();
							int read = flag.contains(Flags.Flag.SEEN) ? 1 : 0;
							String sender = ((InternetAddress) message.getFrom()[0]).getPersonal();
							String senderURL = ((InternetAddress) message.getFrom()[0]).getAddress();
							String subject = message.getSubject();
							String receivedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(message.getReceivedDate());
							
							String insertQuery = "INSERT INTO "+tableName+"(reading,folderName,subject,sender,senderURL,from_time)"+
											"values (?,?,?,?,?,?);";
							try{
								java.sql.PreparedStatement ps = con.prepareStatement(insertQuery);
								ps.setString(1, read+"");
								ps.setString(2, fName);
								ps.setString(3, subject);
								ps.setString(4, sender);
								ps.setString(5, senderURL);
								ps.setString(6, receivedDate);
							
								ps.execute();
							} catch(SQLException e){
								e.printStackTrace();
							}
						}
					}
				}
			} catch(SQLException e){
				e.printStackTrace();
			}
			

		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<TableContents> select(String query) {
		List<TableContents> tempList = new ArrayList<TableContents>();
		try{
			rs = statement.executeQuery(query);
			while(rs.next()){
				TableContents t = new TableContents();
				t.setReading(rs.getBoolean("reading"));
				t.setFolderName(rs.getString("folderName"));
				t.setSubject(rs.getString("subject"));
				t.setSender(rs.getString("sender"));
				t.setSenderURL(rs.getString("senderURL"));
				t.setTime(rs.getString("from_time"));
				tempList.add(t);
			}
		} catch(SQLException e){
			e.printStackTrace();
		}
		return tempList;
	}
	@Override
	public void DBCreate(String query) throws SQLException {
		statement.executeQuery(query);
	}
	
	@Override
	public void DBconnet(String query) {
		
	}
}