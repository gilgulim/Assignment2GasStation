package aspects;

import gasstation.GasStationUtility;

import java.util.logging.Level;
import java.util.logging.Logger;

import bl.AutoClean;
import bl.Car;
import bl.InsideClean;

public aspect AutoCleanLogger {

	private Logger logger;
	private AutoClean theAutoClean;

	pointcut instantiate() : execution (bl.AutoClean.new(..));

	after() : instantiate(){
		theAutoClean = (AutoClean) thisJoinPoint.getThis();
		logger = GasStationUtility.getCleaningServiceLog(theAutoClean);

		logger.log(Level.INFO, "In AutoClean::AutoClean()", theAutoClean);
		logger.log(Level.INFO, "AutoCleaningTeam init", theAutoClean);
	}

	pointcut serveCarAutoClean(Car car) : (execution (private void serveCarAutoClean(Car)) && args(car));

	before(Car car) : serveCarAutoClean(car){
		logger.log(Level.INFO, "AutoCleaningTeam - car " + car.getId() + " Arrived", theAutoClean);
		logger.log(Level.INFO, "In AutoClean::serveCar()", theAutoClean);
		logger.log(Level.INFO, "In AutoClean::serveCar() - Auto clean car " + car.getId(), theAutoClean);
	}
	
	after(Car car) : serveCarAutoClean(car){
		logger.log(Level.INFO, "In AutoClean::serveCar() - Auto clean car " + car.getId() + " finished", theAutoClean);
	}
	
	pointcut autoCleanRunThread() : execution (private void autoCleanRunThread());

	before() : autoCleanRunThread(){
		logger.log(Level.INFO, "In AutoClean::run()", theAutoClean);
		logger.log(Level.INFO, "AutoCleaningTeam - started execution as a seperate thread", theAutoClean);
	}
	
	after() : autoCleanRunThread(){
		logger.log(Level.INFO, "AutoCleaningTeam - finished execution as a seperate thread", theAutoClean);		
	}
}
