package aspects;

import gasstation.GasStationUtility;

import java.util.logging.Level;
import java.util.logging.Logger;

import bl.Car;
import bl.InsideClean;

public aspect InsideCleanLogger {
	private Logger logger;
	private InsideClean theInsideClean;
	pointcut instantiate() : execution (bl.InsideClean.new());
	
	after() : instantiate(){
		theInsideClean = (InsideClean) thisJoinPoint.getThis();
		logger = GasStationUtility.getInsideCleaningServiceLog(theInsideClean, theInsideClean.getId());
		
		logger.log(Level.INFO, "In InsideClean::InsideClean()", theInsideClean);
		logger.log(Level.INFO, "InsideCleaningTeam init ( id = " + theInsideClean.getId() + " )", theInsideClean);
	}

	pointcut serveCarInsideClean(Car car) : (execution (private void serveCarInsideClean(Car)) && args(car));

	before(Car car) : serveCarInsideClean(car){
		logger.log(Level.INFO, "AutoCleaningTeam - car " + car.getId() + " Arrived", theInsideClean);
		logger.log(Level.INFO, "In InsideClean::serveCar() - inside clean car " + car.getId(), theInsideClean);
	}
	
	after(Car car) : serveCarInsideClean(car){
		logger.log(Level.INFO, "In InsideClean::serveCar() - inside clean car " + car.getId() + " finished", theInsideClean);
	}
	
	pointcut countCleaningTime(Car car, double time) : (execution (private void countCleaningTime(Car, double)) && args(car, time));

	after(Car car, double time) : countCleaningTime(car, time){
		logger.log(Level.INFO, "In InsideClean::serveCar() - car  " +  car.getId() + 
		  " is being cleaned for " + time * 1000 + " ms", theInsideClean);
	}

	pointcut insideCleanRunThread() : execution (private void insideCleanRunThread());

	before() : insideCleanRunThread(){
		logger.log(Level.INFO, "In InsideClean::run()", theInsideClean);
		logger.log(Level.INFO, "InsideCleaningTeam (id = " + theInsideClean.getId() + ") - started execution as a seperate thread", theInsideClean);
	}
	
	after() : insideCleanRunThread(){
		logger.log(Level.INFO, "InsideCleaningTeam (id = " + theInsideClean.getId() + ") - finished execution as a seperate thread", theInsideClean);
	}
}
