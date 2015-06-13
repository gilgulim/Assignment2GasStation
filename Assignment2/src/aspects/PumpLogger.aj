package aspects;

import gasstation.GasStationUtility;

import java.util.logging.Level;
import java.util.logging.Logger;

import bl.Car;
import bl.Pump;

public aspect PumpLogger {
	
	private Logger logger;
	private int pumpId;
	private Pump thePump;

	pointcut instantiate() : execution (bl.Pump.new(..));
	after() : instantiate(){
		thePump = (Pump) thisJoinPoint.getThis();
		pumpId = thePump.getId();
		
		logger = GasStationUtility.getPumpLog(thePump,pumpId);
		
		logger.log(Level.INFO, "In Pump::Pump()", thePump);
		logger.log(Level.INFO, "Pump init (id=" + pumpId + ")", thePump);
	}
	
	pointcut refuel(Car car) : (execution (public void refuel(Car)) && args(car));
	before(Car car) : refuel(car){
		logger.log(Level.INFO, "In Pump::refuel()", thePump);
		logger.log(Level.INFO, "In Pump::refuel - car " + car.getId() + " wants to enter", thePump);
	}
	
	after(Car car) : refuel(car){
		logger.log(Level.INFO, "In Pump::refuel - car " + car.getId() + " is in queue", thePump);
	}
	
	
	pointcut loggerGetNextCar(Car car) : (execution (private void loggerGetNextCar(Car)) && args(car));
	after(Car car) : loggerGetNextCar(car){
		logger.log(Level.INFO, "In Pump::getNextCar() - car " + car.getId() + " is ready to be served", thePump);
	}
	
	pointcut serveCar(Car car) : (execution (private void bl.Pump.serveCar(Car)) && args(car));
	before(Car car) : serveCar(car){
		logger.log(Level.INFO, "In Pump::run() - serving car " + car.getId(), thePump);
		logger.log(Level.INFO, "In Pump::serveCar()", thePump);
		logger.log(Level.INFO, "In Pump::serveCar() - car " + car.getId() + " is being served", thePump);
		logger.log(Level.INFO, "In Pump::serveCar() - car needs " +  car.getNumOfLiters() + " litres", thePump);
	}
	
	after(Car car) throwing (Throwable t): (execution (private void bl.Pump.serveCar(Car)) && args(car)){
		logger.log(Level.INFO,t.getMessage(),thePump);
	}
	
	pointcut loggerCarIsFueled(Car car) : (execution (private void loggerCarIsFueled(Car)) && args(car));
	after(Car car) : loggerCarIsFueled(car){
		logger.log(Level.INFO, "In Pump::serveCar() - car  " +  car.getId() + 
				  " is fueled", thePump);
	}
	
	pointcut loggerSetFuelingTime(Car car, double time) : (execution (private void loggerSetFuelingTime(Car, double)) && args(car, time));
	after(Car car, double time) : loggerSetFuelingTime(car, time){
		logger.log(Level.INFO, "In Pump::serveCar() - car  " +  car.getId() + 
				  " is fueling for " + time + " ms", thePump);
	}
	
	pointcut closePump() : execution (public void closePump());

	after() : closePump(){
		logger.log(Level.INFO, "In Pump::closePump()", thePump);
	}	
	pointcut updateMainPumpStartedFueling() : execution (public void updateMainPumpStartedFueling());

	after() : updateMainPumpStartedFueling(){
		logger.log(Level.INFO, "In Pump::updateMainPumpStartedFueling() - main pump fueling",thePump);
	}
	
	pointcut updateMainPumpFinishedFueling() : execution (public void updateMainPumpFinishedFueling());

	after() : updateMainPumpFinishedFueling(){
		logger.log(Level.INFO, "In Pump::updateMainPumpFinishedFueling() - main pump finished fueling",thePump);
	}
	
	pointcut run() : execution (public void bl.Pump.run());
	before() : run(){
		logger.log(Level.INFO, "In Pump::run()", thePump);
		logger.log(Level.INFO, "In Pump::run() - pump number: " + thePump.getId() + 
					  " started running as a seperate thread", thePump);
	}
	
	after() : run(){
		logger.log(Level.INFO, "In Pump::run() - pump number: " + thePump.getId() + 
				  " is now closed", thePump);
	}

}
