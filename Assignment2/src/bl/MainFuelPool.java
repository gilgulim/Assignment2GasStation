package bl;

import gasstation.GasStationUtility;

import java.util.List;
import java.util.ArrayList;

import bl.observers.FillingMainFuelPool_Observer;
import bl.observers.MinTarget_Observer;
import dal.dataObjects.FuelPoolObject;

public class MainFuelPool extends FuelPoolObject{
	private static final int ZERO = 0;
	private static final double MIN_TARGET = 0.2;
	private static Object theCapacityMutex = new Object();
	
	private List<FillingMainFuelPool_Observer> fillingFuelObservers = new ArrayList<FillingMainFuelPool_Observer>();
	private List<MinTarget_Observer> minTargetObservers = new ArrayList<MinTarget_Observer>();
	
	
	public MainFuelPool(int id, int maxCapacity,int currentCapacity) {
		super(id, maxCapacity, currentCapacity);
	}
	
	public MainFuelPool(int maxCapacity,int currentCapacity) {
		super(0, maxCapacity, currentCapacity);
	}
	
	public void fullGas(int quantity) throws FuelPoolException {
		
		Boolean fMaxCapacity = false;
		
		synchronized (theCapacityMutex) {
			
			if(currentCapacity + quantity > maxCapacity) {
				fMaxCapacity = true;
			}
			else {
		
				notifyAllObserversFuelingStarted();
				
				// Sleep for some time
				// Random fueling time factores by the number of liters needed
				double fuelingTime = (Math.random() * quantity);
				loggerSetMainFuelPoolFuelingTime(fuelingTime);
				try {
					Thread.sleep((long)fuelingTime * 10);
				}
				catch (InterruptedException e) {
					
				}
				
				currentCapacity += quantity;
				
				notifyAllObserversFuelingFinished();
			}
		
		}
		
		if (fMaxCapacity == true) {
			throw new  FuelPoolException("The current capacity plus filling quantity can not be great then max capacity.");
		}
		
	}
	
	private void loggerSetMainFuelPoolFuelingTime(double fuelingTime) {
	}

	public void getGas(int quantity) throws FuelPoolException {
		
		boolean fThrowException = false;
		FuelPoolException exp = null;
		
		synchronized (theCapacityMutex) {
			
		
			if(currentCapacity == ZERO) {
				fThrowException = true;
				exp =  new FuelPoolException("Fuel pool is empty");
			}
			else if(currentCapacity - quantity < ZERO) {
				fThrowException = true;
				exp =  new FuelPoolException("There is not enough fuel in the pool");
			}
			else {
				
				currentCapacity -= quantity;
				if(((float)currentCapacity / maxCapacity) < MIN_TARGET)
				{
					notifyAllMinTargetObservers();
				}
			}
		
		}
		
		if (fThrowException == true) {
			throw exp;
		}
	}
	
	public void attachFillingMainFuelPool(FillingMainFuelPool_Observer observer){
	      fillingFuelObservers.add(observer);		
	}
	
	public void attachMinimumTargetAlert(MinTarget_Observer observer){
	      minTargetObservers.add(observer);		
	}
	
	// Notify all observers that main pool fueling started
	private void notifyAllObserversFuelingStarted(){
	    for (FillingMainFuelPool_Observer observer : fillingFuelObservers) {
	         observer.updateMainPumpStartedFueling();
	    }
	}
	
	// Notify all observers that main pool fueling started
	private void notifyAllObserversFuelingFinished(){
	    for (FillingMainFuelPool_Observer observer : fillingFuelObservers) {
	         observer.updateMainPumpFinishedFueling();
	    }
	}
	
	private void notifyAllMinTargetObservers(){
	    for (MinTarget_Observer observer : minTargetObservers) {
	         observer.mainFuelPoolReachedMinimum();
	    }
	} 	
}
