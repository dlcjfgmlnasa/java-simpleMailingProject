package 메일링프로젝트;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableDBConnet extends MySQLconnet {
	public TableDBConnet() {
		super();
	}

	@Override
	public void insert(String query) {
		try{
		      java.sql.PreparedStatement preparedStmt = con.prepareStatement(query);
		      preparedStmt.setString(1, User.id);
		      preparedStmt.setString(2, User.mail);
		      preparedStmt.setString(3, User.passwd);
		      preparedStmt.execute();
			} catch(SQLException e){
				
			}
	}
	
	public void insert(String query,String folderName , boolean reading, String subject, 
									String sender, String senderURL, StringBuffer contents, String from_time, boolean attach){
		try{
			java.sql.PreparedStatement preparedStmt = con.prepareStatement(query);
			preparedStmt.setString(1, User.id+"@"+User.mail);
			preparedStmt.setString(2, User.passwd);
			preparedStmt.setString(3,folderName);
			preparedStmt.setBoolean(4,reading);
			preparedStmt.setString(5, subject);
			preparedStmt.setString(6, sender);
			preparedStmt.setString(7, senderURL);
			preparedStmt.setString(8, from_time);
			preparedStmt.setString(9, contents.toString());
			preparedStmt.setBoolean(10, attach);
			
			
			preparedStmt.execute();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> select(String query){
		List<TableContents> tempList = new ArrayList<>();
		try {
			rs = statement.executeQuery(query);
			while(rs.next()){
				TableContents t = new TableContents();
				String subject = rs.getString("subject");
				if(subject.equals("")) subject = "<제목없음>";
				t.setNo(rs.getInt("no"));
				t.setFolderName(rs.getString("foldername"));
				t.setReading(rs.getBoolean("reading"));
				t.setSubject(subject);
				t.setSender(rs.getString("sender"));
				t.setContents(rs.getString("contents"));
				t.setSenderURL(rs.getString("senderURL"));
				t.setTime(rs.getString("from_time"));
				t.setAttach(rs.getBoolean("attach"));
				tempList.add(t);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return (List<T>) tempList;
	}

	@Override
	public void delect(String query) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(String query) {
		// TODO Auto-generated method stub
		
	}

}
