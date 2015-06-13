package aspects;

import gasstation.GasStationUtility;

import java.util.logging.Level;
import java.util.logging.Logger;

import bl.Car;
import bl.GasStationsBL;
import bl.MainFuelPool;


public aspect GasStationBLLogger {
	private Logger theLogger;
	private GasStationsBL theGAsStationBL;

	pointcut instantiate() : execution (bl.GasStationsBL.new(..));

	after() : instantiate(){
		theGAsStationBL = (GasStationsBL) thisJoinPoint.getThis();
		theLogger = GasStationUtility.getSystemLog(theGAsStationBL);
		
		theLogger.log(Level.INFO, "GasStationsBL init", theGAsStationBL);
		theLogger.log(Level.INFO, "In GasStationsBL()::GasStationsBL()", theGAsStationBL);
	}
	
	pointcut init() : execution (public void bl.GasStationsBL.init());

	before() : init(){
		theLogger.log(Level.INFO, "In GasStationsBL()::init()", theGAsStationBL);
		theLogger.log(Level.INFO, "GasStationBL created DAL proxy object", theGAsStationBL);
		theLogger.log(Level.INFO, "GasStationBL getting GasStation object from configuration file", theGAsStationBL);
	}
	
	after() : init(){
		theLogger.log(Level.INFO, "GasStationBL the GasStation object was created", theGAsStationBL);
		theLogger.log(Level.INFO, "GasStationBL: there are " + theGAsStationBL.getTheGasStation().getCarsListSize() + " cars in the station", theGAsStationBL);
		theLogger.log(Level.INFO, "GasStationBL: there are " + theGAsStationBL.getTheGasStation().getNumOfPumps() + " pumps in the station", theGAsStationBL);
	}
	
	pointcut addCar(Car car) : ((execution (public void bl.GasStationsBL.addCar(Car))) && args(car)); 
	before(Car car) : addCar(car){
		theLogger.log(Level.INFO, "In GasStationsBL()::addCar()", theGAsStationBL);
		theLogger.log(Level.INFO, "In GasStationsBL():: Got car addition request", theGAsStationBL);
	}
	
	pointcut addFuelToMainPool() : execution (public void bl.GasStationsBL.addFuelToMainPool(..));

	after() : addFuelToMainPool(){
		theLogger.log(Level.INFO, "In GasStationsBL()::addFuelToMainPool()", theGAsStationBL);
		theLogger.log(Level.INFO, "In GasStationsBL():: Got fuel addition request", theGAsStationBL);
	}
	
	pointcut getNumOfPumps() : execution (public int bl.GasStationsBL.getNumOfPumps());

	after() : getNumOfPumps(){
		theLogger.log(Level.INFO, "In GasStationsBL()::getNumOfPumps()", theGAsStationBL);
		theLogger.log(Level.INFO, "In GasStationsBL():: Got num of pumps request", theGAsStationBL);
	}
	
	pointcut getMainFuelPool() : execution (public MainFuelPool bl.GasStationsBL.getMainFuelPool());

	after() : getMainFuelPool(){
	theLogger.log(Level.INFO, "In GasStationsBL()::getMainFuelPool()", theGAsStationBL);
	theLogger.log(Level.INFO, "In GasStationsBL():: Got main fuel pool request", theGAsStationBL);
	}
	
	pointcut getGasStationStatistics() : execution (public bl.GasStationStatistics bl.GasStationsBL.getGasStationStatistics());

	after() : getGasStationStatistics(){
		theLogger.log(Level.INFO, "In GasStationsBL()::getGasStationStatistics()", theGAsStationBL);
		theLogger.log(Level.INFO, "In GasStationsBL():: Got statistics request", theGAsStationBL);
	}

	pointcut closeGasStation() : execution (public void bl.GasStationsBL.closeGasStation());

	after() : closeGasStation(){
		theLogger.log(Level.INFO, "In GasStationsBL()::closeGasStation()", theGAsStationBL);
		theLogger.log(Level.INFO, "In GasStationsBL():: Got gas station closing request", theGAsStationBL);
	}

	pointcut run() : execution (public void bl.GasStationsBL.run());
	before() : run(){
		theLogger.log(Level.INFO, "In GasStationsBL::run()", theGAsStationBL);
		theLogger.log(Level.INFO, "GasStationBL began execution as seprate thread (id = " + Thread.currentThread().getId()
					  + ")", theGAsStationBL);
		theLogger.log(Level.INFO, "In GasStationBL::run() - executing the GasStation object in a seperate thread ", theGAsStationBL);
	}
	after() : run(){
		theLogger.log(Level.INFO, "In GasStationBL::run() -  Gas station thread finished executing", theGAsStationBL);
	}

}
