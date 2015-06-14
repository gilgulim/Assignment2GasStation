package dal;

public class GasStationDataBaseManager {
	
	private static IDataBaseConnection instance;
	
	public static IDataBaseConnection getInstance(){
		if(instance == null){
			//TODO: Add a new instance using spring  of another data base connection.
			instance = new GasStationMySqlConnection();
		}
		return instance;
	}
}
