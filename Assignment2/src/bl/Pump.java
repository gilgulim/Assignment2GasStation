package bl;

import gasstation.GasStationUtility;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.CarStatusPacket;
import pl.CarStatusPacket.CarStatusType;
import dal.dataObjects.GasStationHistoryRecord;
import dal.dataObjects.GasStationHistoryRecord.ActionType;
import dal.dataObjects.GasStationHistoryRecord.ServiceEntityType;
import dal.GasStationMySqlConnection;


public class Pump implements FillingMainFuelPool_Observer, Runnable {
	GasStationMySqlConnection connection = GasStationMySqlConnection.getInstance();
	private static int counter = 0;
	private int id;
	private double pricePerLiter;
	private BlockingQueue<Car>cars;
	private static final int WATING_QUEUE_LEN = 10;
	private static final int WATING_QUEUE_TIMEOUT = 50; //ms
	private Boolean fClosed;
	private int numOfCarServed;
	private Object numOfCarServedMutex;
	private int numOfLitersUsed;
	private Object numOfLitersUsedMutex;
	private boolean fWaitForMainPump;
	private Object fWaitForMainPumpMutex;
	private MainFuelPool mainFuelPool;
	
	
	public Pump(MainFuelPool subject,double pricePerLiter){
		
		this.mainFuelPool = subject;
		this.pricePerLiter = pricePerLiter;
		id = ++counter;
		
		cars = new ArrayBlockingQueue<Car>(WATING_QUEUE_LEN);
		
		numOfCarServed = 0;
		numOfCarServedMutex = new Object();
		
		numOfLitersUsed = 0;
		numOfLitersUsedMutex = new Object();
		
		fClosed = false;
		
		fWaitForMainPump = false;
		fWaitForMainPumpMutex = new Object();
	}
	
	public void refuel(Car car)
	{
		try {
			cars.put(car);
		}
		catch (InterruptedException e) {
			
		}
	}

	private Car getNextCar() {
		
		Car nextCar = null;
			
		
		if (fWaitForMainPump == false) { 
		
			// Try to get the next car from the queue or wait for it
			try {
				nextCar = cars.poll(WATING_QUEUE_TIMEOUT, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e) {
				
			}
			
			if (nextCar != null)
				loggerGetNextCar(nextCar);
		}
		
		
		return nextCar;
	}
	
	private void loggerGetNextCar(Car nextCar) {
	}

	// Cars queue check
	private boolean carsWaiting() {
		
		return (cars.isEmpty() == false);
		
	}
	
	// Car serving
	private void serveCar(Car car) {
		// Fuel the car if can
		
		// Random fueling time factors by the number of liters needed
		double fuelingTime = (Math.random() * car.getNumOfLiters());
		
		loggerSetFuelingTime(car, fuelingTime);
		try {
			
			mainFuelPool.getGas(car.getNumOfLiters());
			Thread.sleep((long)fuelingTime * 10);
			IncreaseNumOfCarsServed();
			IncreaseNumOfLitersUsed(car.getNumOfLiters());
			loggerCarIsFueled(car);
			
			
			car.finishFuel();
			
			//update car status
			car.sendCarStatus(CarStatusType.Fueling);	
			
			
			
			
		}
		catch (FuelPoolException e) {
			refuel(car);
		}
		catch(InterruptedException e) {
			
		}
	}
	
	private void loggerSetFuelingTime(Car car, double fuelingTime) {
		
	}

	private void loggerCarIsFueled(Car car) {
	}

	@Override
	public void updateMainPumpStartedFueling() {
		// Mark the waiting flag as true
		synchronized (fWaitForMainPumpMutex) {
			fWaitForMainPump = true;
		}
	}
	
	@Override
	public void updateMainPumpFinishedFueling() {
		// Mark the waiting flag as false
		synchronized (fWaitForMainPumpMutex) {
			fWaitForMainPump = false;
		}
		
	}
	
	public double getPricePerLiter() {
		return pricePerLiter;
	}
	
	public int getId() {
		return id;
	}
	
	// Get the number of cars served
	public int totalCarsFueled() {
		int num;
		
		synchronized (numOfCarServedMutex) {
			num = this.numOfCarServed;
		}
		
		return num;
	}

	public void IncreaseNumOfCarsServed() {
		
		synchronized (numOfCarServedMutex) {
			this.numOfCarServed++;
		}
		
	}
	
	
	public int totalLitersUsed() {
		int num;
		
		synchronized (numOfLitersUsedMutex) {
			num = this.numOfLitersUsed;
		}
		
		return num;
	}
	
	public void IncreaseNumOfLitersUsed(int amount) {
		
		synchronized (numOfLitersUsedMutex) {
			this.numOfLitersUsed += amount;
		}
		
	}
	
	// Get the pumps total profit
	public double totalProfit() {
		return totalLitersUsed() * pricePerLiter;
	}
	
	public void closePump() {
		fClosed = true;
	}
	
	// Thread entry point
	@Override
	public void run() {
		// Main loop
		while (fClosed == false || carsWaiting()) {
			
			Car nextCar = getNextCar();
			
			// Serve the next car if exits
			if (nextCar != null) {
				serveCar(nextCar);
			}
		}
	}
}
