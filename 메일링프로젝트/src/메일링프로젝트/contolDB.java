package 메일링프로젝트;

import java.sql.SQLException;
import java.util.List;

interface controlDB{
	public void DBconnet();
	public void DBconnet(String query);
	public int count(String query);
	public void update(String query);
	public void insert(String query);
	public List<TableContents> select(String query);
	public void DBCreate(String query) throws SQLException;
	public void close();
}
