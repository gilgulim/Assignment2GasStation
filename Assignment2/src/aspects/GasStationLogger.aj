package aspects;

import gasstation.GasStationUtility;

import java.util.logging.Level;
import java.util.logging.Logger;

import bl.Car;
import bl.GasStation;

public aspect GasStationLogger {
	private Logger theLogger;
	private GasStation theGasStation;

	pointcut instantiate() : execution (bl.GasStation.new());

	after() : instantiate(){
		theGasStation = (GasStation) thisJoinPoint.getThis();
		theLogger = GasStationUtility.getSystemLog(theGasStation);
		
		theLogger.log(Level.INFO, "GasStation init", theGasStation);
		theLogger.log(Level.INFO, "In GasStation()::GasStation()", theGasStation);
		
	}
	
	pointcut addCar(Car car) : (((execution (public void bl.GasStation.addCar(Car))) || (execution (public void bl.GasStation.addInitCar(Car)))) && args(car)); 
	after(Car car) : addCar(car){
		theLogger.log(Level.INFO, "Car id :" + car.getId() + " Want Cleaning :" + car.getWantCleaning(), theGasStation);
		if(car.getWantFuel()) {
			theLogger.log(Level.INFO, "Num of Liters :" + car.getNumOfLiters() + " Pump :" + car.getPumpNum(), theGasStation);
		}
	}
	
	pointcut addFuelToMainPool() : execution (public void bl.GasStation.addFuelToMainPool(..));

	before() : addFuelToMainPool(){
		theLogger.log(Level.INFO, "In GasStation()::addFuelToMainPool()", theGasStation);
	}
	
	after() throwing (Throwable t): execution (public void bl.GasStation.addFuelToMainPool(..)){
		theLogger.log(Level.INFO, "In GasStation()::addFuelToMainPool() - got fuel exception: ", theGasStation);
		theLogger.log(Level.INFO, t.getMessage(), theGasStation);
	}
	
	pointcut activatePumps() : execution (private void bl.GasStation.activatePumps());

	before() : activatePumps(){
		theLogger.log(Level.INFO, "In GasStation()::activatePumps()", theGasStation);
		theLogger.log(Level.INFO, "In GasStation()::activatePumps() - creating thread pool for " + theGasStation.getNumOfPumps() + " pumps", theGasStation);
	}
	
	pointcut createThreadForPump(int index) : ((execution (private void bl.GasStation.createThreadForPump(int)))  && args(index)); 
	after(int index) : createThreadForPump(index){
		theLogger.log(Level.INFO, "In GasStation()::activatePumps() - created thread for pump: " + theGasStation.getPumps().get(index).getId(), theGasStation);
	}
	
	pointcut deactivatePumps() : execution (private void bl.GasStation.deactivatePumps());

	after() : deactivatePumps(){
		theLogger.log(Level.INFO, "In GasStation()::deactivatePumps()", theGasStation);
		theLogger.log(Level.INFO, "In GasStation()::deactivatePumps() - sending close command to the pumps", theGasStation);	
		}
	
	pointcut activateCleaningService() : execution (private void bl.GasStation.activateCleaningService());

	after() : activateCleaningService(){
		theLogger.log(Level.INFO, "In GasStation()::activateCleaningService()", theGasStation);
		}
	
	pointcut deactivateCleaningService() : execution (private void bl.GasStation.deactivateCleaningService());

	after() : deactivateCleaningService(){
		theLogger.log(Level.INFO, "In GasStation()::deactivateCleaningService()", theGasStation);		
		}
	
	pointcut run() : execution (public void bl.GasStation.run());

	before() : run(){
		theLogger.log(Level.INFO, "In GasStation()::run()", theGasStation);
		theLogger.log(Level.INFO, "In GasStation()::run() -  Started running as a separate thread", theGasStation);		
		}
	after() : run(){
		theLogger.log(Level.INFO, "In GasStation()::run() -  Thread is closing", theGasStation);
	}
	pointcut loggerHandleCarIsFueledNotFuelingNotWashing(Car car) : ((execution (private void bl.GasStation.loggerHandleCarIsFueledNotFuelingNotWashing(Car))) && args(car)); 
	after(Car car) : loggerHandleCarIsFueledNotFuelingNotWashing(car){
		theLogger.log(Level.INFO, "In GasStation()::run() - car " + car.getId() + " was removed from handling queue", theGasStation);		
		theLogger.log(Level.INFO, "In GasStation()::run() - sending car " + car.getId() + " to cleaning", theGasStation);
	}
	
	pointcut loggerHandleCarNotFuelingNotFueled(Car car) : ((execution (private void bl.GasStation.loggerHandleCarNotFuelingNotFueled(Car))) && args(car)); 
	after(Car car) : loggerHandleCarNotFuelingNotFueled(car){
		theLogger.log(Level.INFO, "In GasStation()::run() - sendind car " + car.getId() + " to fueling", theGasStation);
		theLogger.log(Level.INFO, "In GasStation()::run() - car " + car.getId() + " was added to handling queue", theGasStation);
	}
	
	pointcut loggerHandleCarWantFuelNotCleanNotFueling(Car car) : ((execution (private void bl.GasStation.loggerHandleCarWantFuelNotCleanNotFueling(Car))) && args(car)); 
	after(Car car) : loggerHandleCarWantFuelNotCleanNotFueling(car){
		theLogger.log(Level.INFO, "In GasStation()::run() - sendind car " + car.getId() + " to fueling", theGasStation);
	}
	
	pointcut loggerHandleCarOnlyClean(Car car) : ((execution (private void bl.GasStation.loggerHandleCarOnlyClean(Car))) && args(car)); 
	after(Car car) : loggerHandleCarOnlyClean(car){
		theLogger.log(Level.INFO, "In GasStation()::run() - sendind car " + car.getId() + " to fueling", theGasStation);
	}
}
