package in.bloodapp.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class jdbcUtil {

	static {
		//load and register driver
				try {
					Class.forName("com.mysql.cj.jdbc.Driver");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	public static Connection getDbConnection() throws SQLException {
		//Establish the connection
				String url="jdbc:mysql://localhost:3306/blood_db";
				String user="root";
				String password="root123";
				return DriverManager.getConnection(url, user, password);
	}
	
	public static void closeResources(ResultSet resultset,Statement statement,Connection connection) throws SQLException{
		//close the resources
				if(resultset!=null) 
					resultset.close();
				if(statement!=null) 
					statement.close();
				if(connection!=null) 
					connection.close();
	}
}
