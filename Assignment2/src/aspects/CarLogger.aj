package aspects;

import gasstation.GasStationUtility;

import java.util.logging.Level;
import java.util.logging.Logger;

import bl.Car;

public aspect CarLogger {
	private Logger logger;
	private int carId;
	private Car theCar;

	pointcut instantiate() : ((execution (bl.Car.new(..)) && !(execution (bl.Car.new()))));

	after() : instantiate(){
		theCar = (Car) thisJoinPoint.getThis();
		carId = theCar.getId();
		logger = GasStationUtility.getCarLog(theCar, carId);

		logger.log(Level.INFO, "In Car::Car()", theCar);
		logger.log(Level.INFO, "In Car::Car() - Car id = " + carId, theCar);
	}

	pointcut finishCleaning() : execution (public void finishCleaning());

	before() : finishCleaning(){
		logger.log(Level.INFO, "In Car::finishCleaning()", theCar);
		logger.log(Level.INFO, "In Car::finishCleaning() - Car id = " + carId,
				theCar);
	}

	pointcut finishFuel() : execution (public void finishFuel());

	before() : finishFuel(){
		logger.log(Level.INFO, "In Car::finishFuel()", theCar);

		logger.log(Level.INFO, "In Car::finishFuel() - Car id = " + carId,
				theCar);

	}
}
