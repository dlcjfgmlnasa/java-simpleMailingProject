package 메일링프로젝트;

import java.sql.SQLException;
import java.util.List;

public class LoginDBConnet extends MySQLconnet{
	public LoginDBConnet() {
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
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(String query) {
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> select(String query) {
		return null;
	}

	@Override
	public void delect(String query) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}



}
