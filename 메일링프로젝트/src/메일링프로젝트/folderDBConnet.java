package 메일링프로젝트;

import java.sql.SQLException;
import java.util.List;

public class folderDBConnet extends MySQLconnet {
	public folderDBConnet() {
		super();
	}

	@Override
	public void insert(String query) {
		try{
			statement.executeUpdate(query);
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	@Override
	public <T> List<T> select(String query) {
		return null;
	}

	@Override
	public void delect(String query) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(String query) {
		try{
			statement.executeUpdate(query);
		} catch(SQLException e){
			e.printStackTrace();
		}
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

}
