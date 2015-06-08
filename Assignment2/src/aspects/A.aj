package aspects;

import java.util.logging.Level;

public aspect A {
	pointcut carLogger() : execution (public * * ());
	before() : carLogger(){
		
		System.out.println( "AAAAAAAAAAAAAAA In Car::Car()");
		//logger.log(Level.INFO, "In Car::Car() - created Car id = " + thisJoinPointStaticPart.fie id,this);
	}
}
