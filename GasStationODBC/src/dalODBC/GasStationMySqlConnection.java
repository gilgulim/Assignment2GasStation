package dalODBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;

import dal.dataObjects.*;
import dal.dataObjects.GasStationHistoryRecord.ActionType;
import dal.dataObjects.GasStationHistoryRecord.ServiceEntityType;
import dal.*;


public class GasStationMySqlConnection implements IDataBaseConnection {
	
	private final String DB_URL = "jdbc:mysql://cloudscan.noip.me/gasstation";
	private Connection connection = null;
	private boolean isConnected = false;
	
	
	public GasStationMySqlConnection(){
		connect();
	}
	
	private void connect() {
		
		if(!isConnected){
			
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				
				connection = DriverManager.getConnection(DB_URL, "root", "1234");
				System.out.println("Database connection established");
				isConnected = true;
				
			} catch (Exception ex) {
				
				isConnected = false;
			}
		}
		
	}
	
	public void disconnect(){
		try{
		connection.close();
		}catch(Exception ex) {
			
		}
	}
	
	public boolean clearDatabase(){
		
		try{
			
			Statement statement = connection.createStatement();
			statement.executeUpdate("DELETE FROM gasstationpumprelationship");
			statement.executeUpdate("DELETE FROM gasstations");
			statement.executeUpdate("DELETE FROM mainfuelpool");
			statement.executeUpdate("DELETE FROM cleanservices");
			statement.executeUpdate("DELETE FROM gasstationhistorylog");
			statement.executeUpdate("DELETE FROM cars");
			statement.executeUpdate("DELETE FROM pumps");
			
		}catch(Exception ex) {
			return false;
		}
		return true;
	}
	
	public boolean insertGasStationHistoryRecord(GasStationHistoryRecord historyRecord) {
		
		try{
			
			String dateTime = LocalDateTime.now().toString();
			
			String insertQuery;
			Statement statement = connection.createStatement();
			
			if (historyRecord.getActionType() == ActionType.Enter || historyRecord.getActionType() == ActionType.Exit){
				insertQuery = String.format("INSERT INTO `gasstationhistorylog` "
						+ "(CreationTime, CarID, ActionTypeID) "
						+ "VALUE ('%s', %s, %s)", 	dateTime, 
														historyRecord.getCarId(), 
														historyRecord.getActionType().ordinal());
			}else{
				insertQuery = String.format("INSERT INTO `gasstationhistorylog` "
						+ "(CreationTime, CarID, ActionTypeID, ServiceEntityTypeID, ServiceEntityID) "
						+ "VALUE ('%s', %s, %s, %s, %s)", 	dateTime, 
														historyRecord.getCarId(), 
														historyRecord.getActionType().ordinal(), 
														historyRecord.getServiceEntityType().ordinal(), 
														historyRecord.getServiceEntityId());	
			}
			
			statement.executeUpdate(insertQuery);
			
			
		}catch(Exception ex){
			return false;
		}
		
		return true;
	}
	
	public boolean insertCar(CarObject car){

		try{
			
			String insertQuery;
			Statement statement = connection.createStatement();
			
			if(car.getWantFuel()){
         		insertQuery = String.format("INSERT INTO `cars` (CarID, IsCarReqFuel, IsCarReqWash, CarFuelQuantity, CarPumpID) VALUES (%s, %s, %s, %s, %s)", car.getId(), car.getWantFuel(), car.getWantCleaning(), car.getNumOfLiters(), car.getPumpNum());
         	}else{
         		insertQuery = String.format("INSERT INTO `cars` (CarID, IsCarReqFuel, IsCarReqWash) VALUES (%s, %s, %s)", car.getId(), car.getWantFuel(), car.getWantCleaning());
         	}
			
         	statement.executeUpdate(insertQuery);
			
		}catch(Exception ex){
			return false;
		}
		
		return true;
	}
	
	public boolean insertGasStation(GasStationObject gasStation){
		
		try{
			
			String insertQuery;
			Statement statement = connection.createStatement();
			
			//Insert Cleaning Service Info
			int insideTeams = gasStation.getCleaningServiceObject().getNumOfInsideTeams();
			int price = (int) gasStation.getCleaningServiceObject().getPrice();
			insertQuery = String.format("INSERT INTO cleanservices (CleanServiceID ,NumOfInsideTeams, CleanServicePrice) VALUES (1, %s, %s)", insideTeams, price);
			statement.executeUpdate(insertQuery);
			
			//Insert Main Fuel Pool Info
			int maxCapacity = gasStation.getFuelPoolObject().getMaxCapacity();
			insertQuery = String.format("INSERT INTO mainfuelpool (MainFuelPoolID, MaxCapacity) VALUES (1, %s)", maxCapacity);
			statement.executeUpdate(insertQuery);
			
			//Insert Gas Station Info
			insertQuery = "INSERT INTO `gasstations` (GasStationID, GasStation_MainFuelPoolID, GasStation_CleanServiceID) VALUES (1,1,1)";
			statement.executeUpdate(insertQuery);
			
			//Insert All Pumps
			for(PumpObject pump : gasStation.getPumpsList()){
				
				insertQuery = String.format("INSERT INTO `pumps` (PumpID, PumpPricePerLiter) VALUES (%s, %s)", pump.getId(), pump.getPricePerLiter());
				statement.executeUpdate(insertQuery);
				
				insertQuery = String.format("INSERT INTO `gasstationpumprelationship` (GasStationID, PumpID) VALUES (%s, %s)", 1, pump.getId());
				statement.executeUpdate(insertQuery);
			}
			
			//Insert All Cars
			
			for(CarObject car : gasStation.getCarsList()){
				insertCar(car);
		        insertCarHistoryLog(car);
			}
			
		}catch(Exception ex) {
			return false;
		}
		
		return true;
	}
	
	private void insertCarHistoryLog(CarObject car) {
		insertGasStationHistoryRecord(new GasStationHistoryRecord(car.getId(),ActionType.Enter,null,null));
	}

	public GasStationObject getGasStationById(int gasStationId){
		GasStationObject gasStation = null;
		
		try{
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(String.format("SELECT * FROM gasstation WHERE GasStationID = %s", gasStationId));
			
			while(rs.next()){			
				gasStation = new GasStationObject(gasStationId);
				
				//TODO: Read all the other parts of the GasStation object from the DB
			}

		}catch(Exception ex){
			gasStation = null;
		}
		
		return gasStation;
	}
	
	public int getLittersByCarId(int carId){
		

		try{
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(String.format("SELECT * FROM cars WHERE CarID = %d", carId));
			
			while(rs.next()){			
				return rs.getInt("CarFuelQuantity");
			}

		}catch(Exception ex){
			return -1;
		}
		
		return -1;
		
	}
	
	public int getPricePerLittersByPumpId(int pumpId){
		

		try{
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(String.format("SELECT * FROM pumps WHERE PumpID = %d", pumpId));
			
			while(rs.next()){			
				return rs.getInt("PumpPricePerLiter");
			}

		}catch(Exception ex){
			return -1;
		}
		
		return -1;
		
	}
	
	public int getNumberOfPumps(){
		int numOfPumps = 0;
		try{
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(String.format("SELECT * FROM pumps"));
			
			while(rs.next()){			
				numOfPumps++;
			}

		}catch(Exception ex){
			return -1;
		}
		
		return numOfPumps;
	}
	
	public ArrayList<GasStationHistoryRecord> getStatistics(ActionType actionType, int serviceId){
		
		ArrayList<GasStationHistoryRecord> records = new ArrayList<GasStationHistoryRecord>();
		
		try{
			Statement statement = connection.createStatement();
			String query = String.format("SELECT * FROM gasstationhistorylog WHERE ActionTypeID = %s", actionType.ordinal());
			if(actionType == ActionType.Fuel){
				query +=	String.format(" AND ServiceEntityID = %d", serviceId);
			}
			ResultSet rs = statement.executeQuery(query);
			
			while(rs.next()){	
				records.add(new GasStationHistoryRecord(
														rs.getInt("CarID"), 
														ActionType.values()[rs.getInt("ActionTypeID")], 
														ServiceEntityType.values()[rs.getInt("ServiceEntityTypeID")],
														rs.getInt("ServiceEntityID"),
														rs.getString("CreationTime")
														));
			}

		}catch(Exception ex){
			//gasStation = null;
		}
		
		return records;
		
	}
}
