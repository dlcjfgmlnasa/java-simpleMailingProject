package 메일링프로젝트;

import java.sql.DriverManager;
import java.sql.SQLException;

abstract class MySQLconnet implements connectDB{
	private String id = "root";
	private String passward = "zxc2051801";
	private String dburl = "jdbc:mysql://localhost:3306/mail?autoReconnect=true&useSSL=false";
	protected java.sql.Connection con = null;
	protected java.sql.Statement statement = null;
	protected java.sql.ResultSet rs = null;

	
	public MySQLconnet() {
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}catch(ClassNotFoundException e){}
	}
	
	@Override
	public void DBconnect() {	
		try{
			con =  DriverManager.getConnection(dburl,id,passward);
			statement = con.createStatement();
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
}
