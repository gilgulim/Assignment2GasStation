package bl;

import gasstation.GasStationUtility;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.CarStatusPacket.CarStatusType;
import dal.CarObject;
import dal.GasStationHistoryRecord;
import dal.GasStationHistoryRecord.ActionType;
import dal.GasStationHistoryRecord.ServiceEntityType;
import dal.GasStationMySqlConnection;

public class GasStation implements Runnable{
	
	private final int WATING_QUEUE_LEN = 500;
	private final int WATING_QUEUE_TIMEOUT = 500; 
	
	private int id;
	private MainFuelPool fuelPool;
	
	private Thread cleanServiceThread;
	private CleaningService cleanService;
	
	private List<Pump> pumps;


	private ArrayList<Thread> threadsPumps;
	
	private BlockingQueue<CarObject> cars;
	private ArrayList<Car> handledCars;
	
	private boolean fClosed;
	private Logger theLogger;

	
	public GasStation(){
		
		id = 0;
		fClosed = false;
		threadsPumps = null;
		pumps = new ArrayList<Pump>();
		cars = new ArrayBlockingQueue<CarObject>(WATING_QUEUE_LEN);
		handledCars = new ArrayList<Car>();
		
	}
	
	public GasStation(int id) {
		this();
		this.id = id;
	}

	public void setCleaningService(int numOfTeams, int price, int secondsPerAutoClean) {
		cleanService = new CleaningService(numOfTeams, price, secondsPerAutoClean);
	}
	
	public CleaningService getCleaningServices(){
		return cleanService;
	}
	
	public void setFuelPool(int maxCapacity,int currentCapacity) {
		fuelPool = new MainFuelPool(maxCapacity, currentCapacity);
	}

	public MainFuelPool getFuelPool() {
		return fuelPool;
	}
	
	public void addPump(double pricePerLiter) {

		//Create a new pump
		Pump pump = new Pump(fuelPool,pricePerLiter);

		//Attach the pump to the fuel pool observer
		fuelPool.attachFillingMainFuelPool(pump);
		
		//Add the pump to the pumps list
		pumps.add(pump);
	}
	
	public Pump getPump(int id) {
		
		if(id > pumps.size() || id <= 0) {
			
			throw new IllegalArgumentException("Invalid pump id");
		}
		else {
			
			for (Pump pump : pumps) {
		         if(pump.getId() == id) {
		        	 return pump;
		         }
		    }
		}
		
		return null;
	}
	
	public Iterator<CarObject> getCarsIterator(){
		return cars.iterator();
	}
	
	public int getNumOfPumps() {
		return pumps.size();
	}
	
	public void addCar(Car car) {
		
		// Add the car to the list
		cars.add(car);
		
		//update car status
		car.sendCarStatus(CarStatusType.Entered);
	}
	
	public void addInitCar(Car car) {
		// Add the car to the list
		cars.add(car);
	}
	
	public int getCarsListSize() {
		
		return cars.size();
		
	}
	
	public void addFuelToMainPool(int numOfLiters) {
		try {
			fuelPool.fullGas(numOfLiters);
		}
		catch(FuelPoolException e) {
		}
	}
	
	public void closeGasStation() {
		fClosed = true;
	}
	
	private void activatePumps() {
		threadsPumps = new ArrayList<Thread>(pumps.size());
		
		// Create a thread for each pump
		for (int i = 0; i < pumps.size(); i++) {
			createThreadForPump(i);
		}
		
	}
	
	private void createThreadForPump(int i) {
		Thread t = new Thread(pumps.get(i));
		threadsPumps.add(t);
		t.start();
	}

	private void deactivatePumps() {
		for (Pump p : pumps) {
			p.closePump();
		}
		
		// Wait for the pumps threads
		try {
			for (Thread t : threadsPumps) {
				t.join();
			}
		}
		catch(InterruptedException e) {
			
		}
		
	}
	
	private void activateCleaningService() {
		cleanServiceThread = new Thread(cleanService);
		cleanServiceThread.start();
	}
	
	private void deactivateCleaningService() {
		cleanService.closeCleaningService();
		try {
			cleanServiceThread.join();
		}
		catch (InterruptedException e) {
			
		}
	}
	
	@Override
	public void run() {
		// Activate the pumps and the cleaning service
		activatePumps();
		activateCleaningService();
		
		while (fClosed == false) {
		
			Car theCar = null;
			
			// Poll one car from the queue
			try {
				theCar = (Car)cars.poll(WATING_QUEUE_TIMEOUT, TimeUnit.MILLISECONDS);
			}
			catch(InterruptedException e) {
				
			}
			
			if (theCar != null) {	

				//If the car finished both of the services
				if ((theCar.isCleaned() && theCar.isFueled()) ){
					
					theCar.sendCarStatus(CarStatusType.Exited);
					
				}else if (theCar.getWantFuel() && theCar.getWantCleaning()) { //The car requires both of the services
					if (handledCars.contains(theCar)) {
						
						//If the car has already been fueled, it is not currently fueling and is not currently in washing then add it the cleaning service 
						if ((theCar.isFueled()) && (!theCar.getIsFueling()) && (!theCar.getIsWashing()) ) {
							loggerHandleCarIsFueledNotFuelingNotWashing(theCar);
							theCar.setIsWashing(true);
							
							// Remove from the handled list and send to cleaning
							handledCars.remove(theCar);
							cleanService.cleaning(theCar);
							
							putCarIntoQueue(theCar);
						}else{
							
							putCarIntoQueue(theCar);
						}
					}
					else if(!theCar.getIsFueling() && !theCar.isFueled()) { //The car is not fueled and not currently fueling
						loggerHandleCarNotFuelingNotFueled(theCar);
						theCar.setIsFueling(true);

						// Add the car to the handled cars list
						handledCars.add(theCar);
						
						// Send it to fueling 
						pumps.get(theCar.getPumpNum()-1).refuel(theCar);
											
						// Place it back on the queue for later being cleaned
						putCarIntoQueue(theCar);
						
					}else{
						putCarIntoQueue(theCar);
					}
				}
				//The car wants fuel & doesn't wants cleaning
				else if(theCar.getWantFuel() && !theCar.getWantCleaning()) {
					
					if (theCar.isFueled()){
						 
						theCar.sendCarStatus(CarStatusType.Exited);
						
					}else if(!theCar.getIsFueling()){
						
						theCar.setIsFueling(true);
						
						// Fuel the car
						loggerHandleCarWantFuelNotCleanNotFueling(theCar);
						pumps.get(theCar.getPumpNum()-1).refuel(theCar);
						
						//Returning the car back to the cars queue
						putCarIntoQueue(theCar);
						
					}else{
						putCarIntoQueue(theCar);
					}
				}
				else if (theCar.getWantCleaning()){

					if (theCar.isCleaned()){
						
						theCar.sendCarStatus(CarStatusType.Exited);
						
					}else if(!theCar.getIsWashing()){
						
						theCar.setIsWashing(true);
						
						// The car wants only cleaning	
						loggerHandleCarOnlyClean(theCar);
						cleanService.cleaning(theCar);
						
						putCarIntoQueue(theCar);
					}else{
						putCarIntoQueue(theCar);
					}
				}
				else{
					theCar.sendCarStatus(CarStatusType.Exited);
				}
			}
		}

		deactivatePumps();
		deactivateCleaningService();
	}
	
	private void loggerHandleCarOnlyClean(Car theCar) {
	}

	private void loggerHandleCarWantFuelNotCleanNotFueling(Car theCar) {
	}

	private void loggerHandleCarNotFuelingNotFueled(Car theCar) {
	}

	private void loggerHandleCarIsFueledNotFuelingNotWashing(Car theCar) {
	}

	private void putCarIntoQueue(Car car){
		try {
			cars.put(car);
		} catch (InterruptedException e) {}
	}
	
	// Get current statistics
	public GasStationStatistics getStatistics() {
		
		GasStationStatistics stats = new GasStationStatistics();
		
		// Fill the statistics object
		stats.setNumOfCarsThatWasCleaned(cleanService.getNumOfCarsServed());
		stats.setCleaningRevenew(cleanService.getTotalProfit());
		
		int numOfCarsFueled = 0;
		float totalProfit = 0;
		
		// Sum the amounts of cars and liters from the pumps
		for (Pump p : pumps) {
			numOfCarsFueled += p.totalCarsFueled();
			totalProfit		+= p.totalProfit();
		}
		
		stats.setNumOfCarsThatFueled(numOfCarsFueled);
		stats.setPumpsRevenew(totalProfit);
		
		return stats;
	}

	public List<Pump> getPumps() {
		return pumps;
	}
}
