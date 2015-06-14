package bl;

import dal.GasStationDataBaseManager;
import dal.IDataBaseConnection;
import dal.ReadXmlFileNative;
import dal.dataObjects.CarObject;
import dal.dataObjects.GasStationObject;
import dal.dataObjects.PumpObject;

public class BlProxy implements Runnable {
	private static BlProxy theBlProxy = null;
	private static Object theBlProxyMutex = new Object();
	
	private Thread theBlThread;
	private GasStation gasStation;
	
	protected BlProxy() {
		theBlThread = null;
		init();
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
			theBlThread = new Thread(this);
			theBlThread.start();
		}
		return theBlThread;

	}
	
	private void init() {

		//Reading the GasStation initial data from the XML.
		ReadXmlFileNative readXmlFile = new ReadXmlFileNative("GasStationsConfig.xml");
		GasStationObject gsDal = readXmlFile.getGasStation();
		
		//Loading the inserting the data to the DB
		IDataBaseConnection connection = GasStationDataBaseManager.getInstance();
		connection.insertGasStation(gsDal);
		
		//Loading the GasStation data into the BL objects
		gasStation = new GasStation(gsDal.getId());
		gasStation.setFuelPool(gsDal.getFuelPoolObject().getMaxCapacity(), gsDal.getFuelPoolObject().getCurrentCapacity());
		gasStation.setCleaningService(gsDal.getCleaningServiceObject().getNumOfInsideTeams(), gsDal.getCleaningServiceObject().getPrice(), gsDal.getCleaningServiceObject().getSecondsPerAutoClean());
		
		System.out.println("Cleaning Services: " + gsDal.getCleaningServiceObject().getNumOfInsideTeams() + " " + gsDal.getCleaningServiceObject().getPrice());
		
		for(CarObject car : gsDal.getCarsList()){
			addCar(car.getId(), car.getWantFuel(), car.getPumpNum(), car.getNumOfLiters(), car.getWantCleaning());
		}
		
		for(PumpObject pump : gsDal.getPumpsList()){
			gasStation.addPump(pump.getPricePerLiter());
		}
	}
	
	@Override
	public void run() {
		
		Thread stationThread = new Thread(gasStation);
		stationThread.start();
		
		try {
			stationThread.join();
		}
		catch(InterruptedException e) {
		}
		
	}

	/* User actions from UI */

	/* Add car */
	// ****DEPRICATED***
	public void addCar(int id, boolean fAddToPump, int pumpNum,
			int numOfLiters, boolean addToCleaning) {
		Car car = null;
		
		if (fAddToPump == true) {
			// Fuel and maybe cleaning
			car = new Car(id, numOfLiters, pumpNum, addToCleaning);
		}
		else {
			// No fuel
			car = new Car(id, addToCleaning);
		}
		
		gasStation.addCar(car);
	}

	public void addCar(Car car) {
		GasStationDataBaseManager.getInstance().insertCar(car);
		gasStation.addCar(car);
	}

	/* Add Fuel to main pool */
	public void addFuelToMainPool(int numOfLiters) {
		gasStation.addFuelToMainPool(numOfLiters);
	}

	/* Get the number of pumps */
	public int getNumOfPumps() {
		return gasStation.getNumOfPumps();
	}

	public MainFuelPool getMainFuelPool() {
		return gasStation.getFuelPool();
	}

	/* Get gas station statistics */
	public GasStationStatistics getGasStationStatistics() {
		return gasStation.getStatistics();
	}

	public CleaningService GetCleaningServices() {
		return gasStation.getCleaningServices();
	}

	/* close the gas station */
	public void closeGasStation() {
		gasStation.closeGasStation();
	}

}
