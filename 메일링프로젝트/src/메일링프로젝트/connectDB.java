package 메일링프로젝트;

import java.util.List;

public interface connectDB {
	public void DBconnect();
	public int count(String query);
	public void insert(String query);
	public <T> List<T> select(String query);
	public void delect(String query);
	public void update(String query);
	public void close();
}
