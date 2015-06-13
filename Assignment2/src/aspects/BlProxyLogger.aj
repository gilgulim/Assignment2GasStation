package aspects;

import gasstation.GasStationUtility;

import java.util.logging.Level;
import java.util.logging.Logger;

import bl.BlProxy;
import bl.GasStationStatistics;
import bl.MainFuelPool;


public aspect BlProxyLogger {
	private Logger logger;
	private BlProxy theBlProxy;

	pointcut instantiate() : execution (bl.BlProxy.new());

	after() : instantiate(){
		theBlProxy = (BlProxy) thisJoinPoint.getThis();
		logger = GasStationUtility.getSystemLog(theBlProxy);

		logger.log(Level.INFO, "BLProxy init", theBlProxy);
		logger.log(Level.INFO, "In BLProxy()::blProxy()", theBlProxy);
	}
	
	pointcut getBlProxy() : execution (public BlProxy getBlProxy());

	after() : getBlProxy(){
		logger.log(Level.INFO, "In BLProxy::getBlProxy()", theBlProxy);
	}
	
	pointcut runBlThread() : execution (private Thread runBlThread());

	after() : runBlThread(){
		logger.log(Level.INFO, "In BLProxy::runThread()", theBlProxy);
		
		logger.log(Level.INFO, "In BLProxy:: Running the BL as a seperate thread", theBlProxy);
	}
	
	pointcut addFuelToMainPool() : execution (public void addFuelToMainPool(..));

	after() : addFuelToMainPool(){
		logger.log(Level.INFO, "In BLProxy()::addFuelToMainPool()", theBlProxy);
		logger.log(Level.INFO, "In BLProxy():: Got fuel addition request", theBlProxy);
	}
	
	pointcut getNumOfPumps() : execution (public int getNumOfPumps());

	after() : getNumOfPumps(){
		logger.log(Level.INFO, "In BLProxy()::getNumOfPumps()", theBlProxy);
		logger.log(Level.INFO, "In BLProxy():: Got num of pumps request",
				theBlProxy);
	}
	
	pointcut getMainFuelPool() : execution (public MainFuelPool getMainFuelPool());

	after() : getMainFuelPool(){
		logger.log(Level.INFO, "In BLProxy()::getMainFuelPool()", theBlProxy);
		logger.log(Level.INFO, "In BLProxy():: Got main fuel pool request",
				theBlProxy);
	}
	
	pointcut getGasStationStatistics() : execution (public GasStationStatistics getGasStationStatistics());

	after() : getGasStationStatistics(){
		logger.log(Level.INFO, "In BLProxy()::getGasStationStatistics()",
				theBlProxy);
		logger
				.log(Level.INFO, "In BLProxy():: Got statistics request", theBlProxy);
	}

	pointcut closeGasStation() : execution (public void closeGasStation());

	after() : closeGasStation(){
		logger.log(Level.INFO, "In BLProxy()::closeGasStation()", theBlProxy);
		logger.log(Level.INFO,
				"In BLProxy():: Got gas station closing request", theBlProxy);
	}
}
