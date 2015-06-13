package bl;

import gasstation.GasStationUtility;

import java.util.logging.Level;
import java.util.logging.Logger;

import dal.GasStationMySqlConnection;

public class BlProxy extends GasStationBlBase {
	private static BlProxy theBlProxy = null;
	private static Object theBlProxyMutex = new Object();
	private static Logger theLogger;

	private GasStationsBL theBL;
	private Thread theBlThread = null;

	protected BlProxy() {
	}

	public static BlProxy getBlProxy() {

		synchronized (theBlProxyMutex) {

			if (theBlProxy == null) {
				theBlProxy = new BlProxy();
			} else {
			}

		}
		return theBlProxy;
	}

	public Thread runThread() {
		if (theBlThread == null) {
			return runBlThread();
		}
		return theBlThread;

	}

	private Thread runBlThread() {
		// Create the BL actual object and run it as a seperate thread
		theBL = new GasStationsBL();
		theBL.init();

		theBlThread = new Thread(theBL);
		theBlThread.start();

		return theBlThread;

	}

	/* User actions from UI */

	/* Add car */
	// ****DEPRICATED***
	public void addCar(int id, boolean fAddToPump, int pumpNum,
			int numOfLiters, boolean addToCleaning) {
		theBL.addCar(id, fAddToPump, pumpNum, numOfLiters, addToCleaning);
	}

	public void addCar(Car car) {
		GasStationMySqlConnection.getInstance().insertCar(car);
		theBL.addCar(car);
	}

	/* Add Fuel to main pool */
	public void addFuelToMainPool(int numOfLiters) {
		theBL.addFuelToMainPool(numOfLiters);
	}

	/* Get the number of pumps */
	public int getNumOfPumps() {
		return theBL.getNumOfPumps();
	}

	public MainFuelPool getMainFuelPool() {
		return theBL.getMainFuelPool();
	}

	/* Get gas station statistics */
	public GasStationStatistics getGasStationStatistics() {
		return theBL.getGasStationStatistics();
	}

	public CleaningService GetCleaningServices() {
		return theBL.GetCleaningServices();
	}

	/* close the gas station */
	public void closeGasStation() {
		theBL.closeGasStation();
	}

}
