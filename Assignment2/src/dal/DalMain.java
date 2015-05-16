package dal;

public class DalMain {

	
	public static void main(String[] args) {
		
		MySqlConnection mysqlConn = new MySqlConnection("jdbc:mysql://cloudscan.noip.me/gasstation");
		mysqlConn.Connect();
		
	}
	
}
