package aspects;

import java.util.logging.Level;
import java.util.logging.Logger;

public aspect CarLogger {
	private Logger logger;
	
	pointcut carLogger() : execution (public * * ());
	
	before() : carLogger(){
		logger.log(Level.INFO, "AAAAAAAAAAAAAAA In Car::Car()",thisJoinPoint.getThis());
	}
}
