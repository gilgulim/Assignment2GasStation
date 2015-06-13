package dal;

import dal.dataObjects.GasStationHistoryRecord;
import dal.dataObjects.GasStationHistoryRecord.ActionType;
import dal.dataObjects.GasStationHistoryRecord.ServiceEntityType;
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

		GasStationMySqlConnection mysqlConn = GasStationMySqlConnection.getInstance();
		mysqlConn.connect();
		mysqlConn.clearDatabase();
		mysqlConn.insertGasStation(gasStation);
		
		GasStationHistoryRecord historyRecord = new GasStationHistoryRecord(111, ActionType.Enter, ServiceEntityType.FuelPump, 1);
		mysqlConn.insertGasStationHistoryRecord(historyRecord);
		
	}
	
}
