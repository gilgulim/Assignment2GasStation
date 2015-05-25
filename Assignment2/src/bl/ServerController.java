package bl;

import java.util.ArrayList;

import dal.GasStationHistoryRecord.ActionType;
import dal.GasStationHistoryRecord;
import dal.GasStationMySqlConnection;
import pl.CarStatusPacket.CarStatusType;
import ui.ServerGUI;
import ui.StatisticsRecord;

public class ServerController {
	private static ServerController theServerController;
	private static Object theServerControllerMutex = new Object();
	
	private BlProxy blProxy; 
	private GasStationMySqlConnection dbConnection;
	
	
	private ServerController(){
		blProxy = BlProxy.getBlProxy();
		dbConnection = GasStationMySqlConnection.getInstance();
	}
	
	public static ServerController getServerController(){
		synchronized (theServerControllerMutex) {
			if(theServerController == null){
				theServerController = new ServerController();
			}
		}
		return theServerController;
		
	}
	
	public void addCar(int carId, boolean requiredFuel, int fuelAmount, boolean requiredWash){
  		Car car = new Car(carId, requiredFuel, fuelAmount,requiredWash);
		
		//Setting a random pump number to the car
  		int pumpNum = (int)(Math.random()*blProxy.getNumOfPumps())+1;
  		car.setPumpNum(pumpNum);
		blProxy.addCar(car);
	}
	
	public void updateCarStatus(int carId, CarStatusType status){
		String theCarID = String.valueOf(carId);		
		ServerGUI serverGUI = ServerGUI.getServerGUI();
		serverGUI.updateCarStatus(theCarID, status);
		}
	
	public ArrayList<StatisticsRecord> getStatistics(String actionType, String serviceId){
		
		ArrayList<StatisticsRecord> statisticsRecords = new ArrayList<StatisticsRecord>();
		
		ActionType action = ActionType.valueOf(actionType);
		int servId = Integer.parseInt(serviceId);
		
		//The DAL returns GasStationHistoryRecord
		ArrayList<GasStationHistoryRecord> gsHistoryRecordsList = dbConnection.getStatistics(action, servId);
		
		//Convert the GasStationHistoryRecord to UI - StatisticsRecord
		for(GasStationHistoryRecord gsHistoryRecord :  gsHistoryRecordsList){
			
			int profit = 0;

			
			if(gsHistoryRecord.getActionType() == ActionType.Fuel){
				
				int litters =  dbConnection.getLittersByCarId(gsHistoryRecord.getCarId());
				int pricePerLitter = dbConnection.getPricePerLittersByPumpId(gsHistoryRecord.getServiceEntityId());
				
				if(litters != -1 && pricePerLitter != -1){
					profit = litters * pricePerLitter;
				}
				
				
				
			}else if(gsHistoryRecord.getActionType() == ActionType.Wash){
				
				profit = blProxy.getBlProxy().GetCleaningServices().getPrice();
				
			}
			
			statisticsRecords.add(new StatisticsRecord(	gsHistoryRecord.getDateTime(),
					gsHistoryRecord.getCarId() + "",
					gsHistoryRecord.getActionType().toString(),
					gsHistoryRecord.getServiceEntityId() + "",
					profit + ""));

		}
		
		return statisticsRecords;
		
	}
}
