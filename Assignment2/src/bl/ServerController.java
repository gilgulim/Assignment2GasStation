package bl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cl.GasStationClient_Observer;
import cl.TcpServer;
import dal.GasStationHistoryRecord.ActionType;
import dal.GasStationHistoryRecord;
import dal.GasStationMySqlConnection;
import pl.CarStatusPacket.CarStatusType;
import ui.ServerGUI;
import ui.StatisticsRecord;

public class ServerController implements FillingMainFuelPool_Observer {
	private static ServerController theServerController;
	private static Object theServerControllerMutex = new Object();
	
	private Lock changeUICarStatusLock;
	private List<CarChangeState_Observer> carChangeStateObservers;
	private BlProxy blProxy; 
	private GasStationMySqlConnection dbConnection;
	private TcpServer tcpServer;
	
	private ArrayList<FillingMainFuelPool_Observer> mainFuelPoolStatusObservers;
	
	
	private ServerController(){
		
		changeUICarStatusLock = new ReentrantLock();
		tcpServer = new TcpServer("192.168.43.59", 3456);
		tcpServer.start();
		
		System.out.println("Server started...");
		
		dbConnection = GasStationMySqlConnection.getInstance();
		dbConnection.clearDatabase();
		
		mainFuelPoolStatusObservers = new ArrayList<FillingMainFuelPool_Observer>();
		carChangeStateObservers = new ArrayList<CarChangeState_Observer>();
		
		blProxy = BlProxy.getBlProxy();
		blProxy.runThread();
		
		
		
		

	}
	public void attachToFuelPool(){
		blProxy.getMainFuelPool().attach(this);
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
	
	public void updateCarStatus(Car car, CarStatusType status) {
		changeUICarStatusLock.lock();
		carStatusNotifyAll(car, status);
		changeUICarStatusLock.unlock();
	}
	
	public int getNumberOfPumps() {
		
		return dbConnection.getNumberOfPumps();
		
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
	
	public boolean addFuelToMainRepository(int amount){
		
		try {
			blProxy.getMainFuelPool().fullGas(amount);
			return true;
		} catch (FuelPoolException e) {
			return false;
		}
	}
	
	private void carStatusNotifyAll(Car car, CarStatusType carStatus) {
		
		for(CarChangeState_Observer observer : carChangeStateObservers){
			observer.updateCarState(car, carStatus);
		}
	}
	
	public void attachCarStateChanedObserver(CarChangeState_Observer observer){
		carChangeStateObservers.add(observer);
	}
	
	public void attachMainFuelPoolObserver(FillingMainFuelPool_Observer observer){
		mainFuelPoolStatusObservers.add(observer);
	}

	@Override
	public void updateMainPumpStartedFueling() {
		
		for(FillingMainFuelPool_Observer observer : mainFuelPoolStatusObservers){
			observer.updateMainPumpStartedFueling();
		}
	}

	@Override
	public void updateMainPumpFinishedFueling() {

		for(FillingMainFuelPool_Observer observer : mainFuelPoolStatusObservers){
			observer.updateMainPumpFinishedFueling();
		}
	}
}
