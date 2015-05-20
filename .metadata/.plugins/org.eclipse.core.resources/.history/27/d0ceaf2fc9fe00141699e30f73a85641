package dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.Iterator;

import dal.GasStationHistoryRecord.ActionType;
import bl.Car;
import bl.GasStation;
import bl.Pump;

public class GasStationMySqlConnection {
	
	private final String DB_URL = "jdbc:mysql://cloudscan.noip.me/gasstation";
	
	private Connection connection = null;
	private static boolean isConnected = false;
	private static GasStationMySqlConnection instance = null;
	
	public static GasStationMySqlConnection getInstance(){
		
		if(instance == null){
			instance = new GasStationMySqlConnection();
<<<<<<< HEAD
			
=======
			if(!isConnected){
				instance = null;
			}
>>>>>>> dc7c5c4886165bd13dff0ea89a2072b403c56467
		}
		
		return instance;
	}
	
	private GasStationMySqlConnection(){
		connect();
	}
	
	public void connect() {
		
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
	
	public boolean insertCar(Car car){

		try{
			
			String insertQuery;
			Statement statement = connection.createStatement();
			
			if(car.wantsFuel()){
         		insertQuery = String.format("INSERT INTO `cars` (CarID, IsCarReqFuel, IsCarReqWash, CarFuelQuantity, CarPumpID) VALUES (%s, %s, %s, %s, %s)", car.getId(), car.wantsFuel(), car.wantsCleaning(), car.getNumOfLiters(), car.getPumpNum());
         	}else{
         		insertQuery = String.format("INSERT INTO `cars` (CarID, IsCarReqFuel, IsCarReqWash) VALUES (%s, %s, %s)", car.getId(), car.wantsFuel(), car.wantsCleaning());
         	}
         	statement.executeUpdate(insertQuery);
			
		}catch(Exception ex){
			return false;
		}
		
		return true;
	}
	
	public boolean insertGasStation(GasStation gasStation){
		
		try{
			
			String insertQuery;
			Statement statement = connection.createStatement();
			
			//Insert Cleaning Service Info
			int insideTeams = gasStation.getCleaningServices().getNumOfInsideTeams();
			int price = gasStation.getCleaningServices().getPrice();
			insertQuery = String.format("INSERT INTO cleanservices (CleanServiceID ,NumOfInsideTeams, CleanServicePrice) VALUES (1, %s, %s)", insideTeams, price);
			statement.executeUpdate(insertQuery);
			
			//Insert Main Fuel Pool Info
			int maxCapacity = gasStation.getFuelPool().getMaxCapacity();
			insertQuery = String.format("INSERT INTO mainfuelpool (MainFuelPoolID, MaxCapacity) VALUES (1, %s)", maxCapacity);
			statement.executeUpdate(insertQuery);
			
			//Insert Gas Station Info
			insertQuery = "INSERT INTO `gasstations` (GasStationID, GasStation_MainFuelPoolID, GasStation_CleanServiceID) VALUES (1,1,1)";
			statement.executeUpdate(insertQuery);
			
			//Insert All Pumps
			for(int i=1; i<=gasStation.getNumOfPumps(); i++){
				Pump pump = gasStation.getPump(i);
			
				insertQuery = String.format("INSERT INTO `pumps` (PumpID, PumpPricePerLiter) VALUES (%s, %s)", pump.getId(), pump.getPricePerLiter());
				statement.executeUpdate(insertQuery);
				
				insertQuery = String.format("INSERT INTO `gasstationpumprelationship` (GasStationID, PumpID) VALUES (%s, %s)", 1, pump.getId());
				statement.executeUpdate(insertQuery);
				
			}
			
			//Insert All Cars
			
			Iterator<Car> carIt = gasStation.getCarsIterator();
			while(carIt.hasNext()) {
		         Car car = carIt.next();
		         
		         insertCar(car);
		         insertCarHistoryLog(car);
		    }
			
		}catch(Exception ex) {
			return false;
		}
		
		return true;
	}
	
	private void insertCarHistoryLog(Car car) {
		insertGasStationHistoryRecord(new GasStationHistoryRecord(car.getId(),ActionType.Enter,null,null));
	}

	public GasStation getGasStationById(int gasStationId){
		GasStation gasStation = null;
		
		try{
			Statement statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(String.format("SELECT * FROM gasstation WHERE GasStationID = %s", gasStationId));
			
			while(rs.next()){
			
				gasStation = new GasStation(gasStationId);
				
				
			}

		}catch(Exception ex){
			gasStation = null;
		}
		
		return gasStation;
	}
}
