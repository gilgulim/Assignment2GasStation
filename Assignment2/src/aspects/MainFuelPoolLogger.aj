package aspects;

import gasstation.GasStationUtility;

import java.util.logging.Level;
import java.util.logging.Logger;

import bl.Car;
import bl.MainFuelPool;


public aspect MainFuelPoolLogger {
	private Logger theLogger;
	private MainFuelPool theMainFuelPool;

	pointcut instantiate() : execution (bl.MainFuelPool.new(..));
	after() : instantiate(){
		theMainFuelPool = (MainFuelPool) thisJoinPoint.getThis();
		
		theLogger = GasStationUtility.getSystemLog(theMainFuelPool);
		
		theLogger.log(Level.INFO, "MainFuelPool init", theMainFuelPool);
		theLogger.log(Level.INFO, "In MainFuelPool()::MainFuelPool()", theMainFuelPool);
		theLogger.log(Level.INFO, "In MainFuelPool()::MainFuelPool() - current capacity: " + theMainFuelPool.getCurrentCapacity(), theMainFuelPool);
		theLogger.log(Level.INFO, "In MainFuelPool()::MainFuelPool() - max capacity: " + theMainFuelPool.getMaxCapacity(), theMainFuelPool);
	}
	
	pointcut fullGas(int quantity) : (execution (public void fullGas(int)) && args(quantity));
	before(int quantity) : fullGas(quantity){
		theLogger.log(Level.INFO, "In MainFuelPool()::fullGas() - Liters: " + quantity, theMainFuelPool);
	}
	
	pointcut notifyAllObserversFuelingStarted() : execution (private void notifyAllObserversFuelingStarted());
	before() : notifyAllObserversFuelingStarted(){
		theLogger.log(Level.INFO, "In MainFuelPool()::fullGas() - Notifying observers that the pool is being fueled", theMainFuelPool);
	}
	after() : notifyAllObserversFuelingStarted(){
		theLogger.log(Level.INFO, "In MainFuelPool::notifyAllObserversFuelingStarted()", theMainFuelPool);
	}
	
	
	pointcut loggerSetMainFuelPoolFuelingTime(double time) : (execution (public void loggerSetMainFuelPoolFuelingTime(double)) && args(time));
	after(double time) : loggerSetMainFuelPoolFuelingTime(time){
		theLogger.log(Level.INFO, "In MainFuelPool::fullGas() - pool is fueling for " + time + " ms", theMainFuelPool);
	}
	
	
	pointcut notifyAllObserversFuelingFinished() : execution (private void notifyAllObserversFuelingFinished());
	before() : notifyAllObserversFuelingFinished(){
		theLogger.log(Level.INFO, "In MainFuelPool::fullGas() - pool is fueled", theMainFuelPool);
		theLogger.log(Level.INFO, "In MainFuelPool()::fullGas() - Notifying observers that the pool is fueled", theMainFuelPool);
	}
	after() : notifyAllObserversFuelingFinished(){
		theLogger.log(Level.INFO, "In MainFuelPool::notifyAllObserversFuelingFinished()", theMainFuelPool);
	}
	
	pointcut notifyAllMinTargetObservers() : execution (private void notifyAllMinTargetObservers());
	before() : notifyAllMinTargetObservers(){
		theLogger.log(Level.INFO, "In MainFuelPool::getGas() - send exception", theMainFuelPool);
		theLogger.log(Level.INFO, "In MainFuelPool::getGas() - current: " + theMainFuelPool.getCurrentCapacity() + " max: " + theMainFuelPool.getMaxCapacity(), theMainFuelPool);
	}
	
	pointcut attachFillingMainFuelPool() : execution (public void attachFillingMainFuelPool(..));
	after() : attachFillingMainFuelPool(){
		 theLogger.log(Level.INFO, "In MainFuelPool::attach() - attched new fueling observer", theMainFuelPool);
	}
}
