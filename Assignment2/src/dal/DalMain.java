package dal;

import bl.GasStation;

public class DalMain {

	private static final String ConfigXmlFileName = "GasStationsConfig.xml";
	
	public static void main(String[] args) {
		
		GasStation gasStation = null;
		
		try {
			gasStation = (new ConfigDOMParser()).ParseXMLFile(ConfigXmlFileName);
			
		}
		catch(Exception exp) {
			exp.printStackTrace();
		}
		
		
		MySqlConnection mysqlConn = new MySqlConnection("jdbc:mysql://cloudscan.noip.me/gasstation");
		mysqlConn.connect();
		mysqlConn. clearDatabase();
		mysqlConn.insertGasStation(gasStation);
		
	}
	
}
