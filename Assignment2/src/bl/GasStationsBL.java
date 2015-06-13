
package bl;

import gasstation.GasStationUtility;

import java.util.logging.Level;
import java.util.logging.Logger;

import dal.DalProxy;
import dal.GasStationMySqlConnection;

public class GasStationsBL extends GasStationBlBase implements Runnable {

	private DalProxy theDalProxy;
	// The actual gas station object
	private GasStation theGasStation;
	


	public GasStationsBL() {
	}
	
	public void init() {
		// Get the DAL proxy object
		theDalProxy = DalProxy.getDalProxy();
		
		// Init the DAL proxy
		theDalProxy.init();
		
		// Get the GasStation from configuration
		theGasStation = theDalProxy.getGasStationFromConfiguration();

		GasStationMySqlConnection connection = GasStationMySqlConnection.getInstance();
		connection.insertGasStation(theGasStation);
	}
	
	
	/* User actions from Proxy */
	
	/* Add car */
	public void addCar(int id, boolean fAddToPump, int pumpNum, int numOfLiters, boolean addToCleaning) {
		Car car;
		
		if (fAddToPump == true) {
			// Fuel and maybe cleaning
			car = new Car(id, numOfLiters, pumpNum, addToCleaning);
		}
		else {
			// No fuel
			car = new Car(id, addToCleaning);
		}
		
		addCar(car);
	}
	
	public void addCar(Car car){
		theGasStation.addCar(car);
	}
	
	/* Add Fuel to main pool */
	public void addFuelToMainPool(int numOfLiters) {
		theGasStation.addFuelToMainPool(numOfLiters);
	}
	
	/* Get the number of pumps */
	public int getNumOfPumps() {
		return theGasStation.getNumOfPumps();
		
	}
	
	/* Get the main fuel pool */
	public MainFuelPool getMainFuelPool() {
		return theGasStation.getFuelPool();
	}
	
	/* Get gas station statistics */
	public GasStationStatistics getGasStationStatistics() {
		return theGasStation.getStatistics();
	}
	
	
	public CleaningService GetCleaningServices(){
		
		return theGasStation.getCleaningServices();
	}
	
	
	/* close the gas station */
	public void closeGasStation() {
		theGasStation.closeGasStation();
	}
	
	// Runnable run method override
	public void run() {
		// Run the gas station
		Thread stationThread = new Thread(theGasStation);
		
		// Message loop to get commands
		stationThread.start();
		
		try {
			stationThread.join();
		}
		catch(InterruptedException e) {
		}
	}
	
	public GasStation getTheGasStation() {
		return theGasStation;
	}
}
