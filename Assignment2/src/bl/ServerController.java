package bl;

import dal.GasStationHistoryRecord.ActionType;
import dal.GasStationMySqlConnection;
import pl.CarStatusPacket.CarStatusType;

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
	
	public void updateCarStatus(Car car, CarStatusType status){
		
	}
	
	public void getStatistics(String actionType, String serviceId){
		
		ActionType action = ActionType.valueOf(actionType);
		int servId = Integer.parseInt(serviceId);
		
		dbConnection.getStatistics(action, servId);
		
	}
}
