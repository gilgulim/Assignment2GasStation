package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlConnection {
	
	private Connection connection = null;
	private String dbUrl;
	
	public MySqlConnection(String dbUrl){
		this.dbUrl = dbUrl;
	}
	
	public void Connect() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			connection = DriverManager.getConnection(dbUrl, "root", "1234");
			System.out.println("Database connection established");
			
		} catch (Exception ex) {
			System.out.println("Error!");
		}

		
	}
}
