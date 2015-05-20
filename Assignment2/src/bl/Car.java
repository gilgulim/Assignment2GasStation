package bl;

import gasstation.GasStationUtility;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.annotations.*;


public class Car {
	
	@Expose
	private int id;
	@Expose
	private Boolean finishCleaning;
	@Expose
	private Boolean finishFuel;
	@Expose
	private Boolean wantCleaning;
	@Expose
	private Boolean wantFuel;
	@Expose
	private int numOfLiters;
	@Expose
	private int pumpNum;
	
	private Logger theLogger;
	
	public Car(int id, boolean wantFuel, int numOfLiters, boolean wantCleaning){
		this.id = id;
		this.numOfLiters = numOfLiters;
		this.wantFuel = wantFuel;
		this.wantCleaning = wantCleaning;
		this.finishCleaning = false;
		this.finishFuel = false;
		
		initLog();
		theLogger.log(Level.INFO, "In Car::Car()",this);
		theLogger.log(Level.INFO, "In Car::Car() - created Car id = " + id,this);
	}
	
	public Car(int id,Boolean wantCleaning) {
		this.id = id;
		this.wantCleaning = wantCleaning;
		this.wantFuel = false;
		this.finishCleaning = false;
		this.finishFuel = false;
		
		initLog();
		theLogger.log(Level.INFO, "In Car::Car()",this);
		theLogger.log(Level.INFO, "In Car::Car() - created Car id = " + id,this);
	}
	
	public void setPumpNum(int pumpNum) {
		this.pumpNum = pumpNum;
	}

	public Car(int id,int numOfLiters,int pumpNum) {
		this.id = id;
		this.numOfLiters = numOfLiters;
		this.pumpNum = pumpNum;
		this.wantFuel = true;
		this.wantCleaning = false;
		this.finishCleaning = false;
		this.finishFuel = false;
		
		initLog();
		theLogger.log(Level.INFO, "In Car::Car()",this);
		theLogger.log(Level.INFO, "In Car::Car() - created Car id = " + id,this);
	}
	
	public Car(int id,int numOfLiters,int pumpNum,Boolean wantCleaning) {
		this.id = id;
		this.numOfLiters = numOfLiters;
		this.pumpNum = pumpNum;
		this.wantCleaning = wantCleaning;
		this.wantFuel = true;
		this.finishCleaning = false;
		this.finishFuel = false;
		
		initLog();
		theLogger.log(Level.INFO, "In Car::Car()",this);
		theLogger.log(Level.INFO, "In Car::Car() - created Car id = " + id,this);
	}
	
	private void initLog() {
	
		theLogger = GasStationUtility.getCarLog(this, id);
		
	}
	
	public void finishCleaning() {
		
		theLogger.log(Level.INFO, "In Car::finishCleaning()",this);
		theLogger.log(Level.INFO, "In Car::finishCleaning() - Car id = " + id,this);
		
		finishCleaning = true;
	}
	
	public void finishFuel() {
		
		theLogger.log(Level.INFO, "In Car::finishFuel()",this);
		theLogger.log(Level.INFO, "In Car::finishFuel() - Car id = " + id,this);
		
		finishFuel = true;
	}
	
	public boolean isFueled() {
		return finishFuel;
	}
	
	public boolean isCleaned() {
		return finishCleaning;
	}
	
	public int getNumOfLiters() {
		return numOfLiters;
	}
	
	public boolean wantsFuel() {
		return wantFuel;
	}
	
	public boolean wantsCleaning() {
		return wantCleaning;
	}
	
	public int getPumpNum() {
		return pumpNum;
	}
	
	public int getId() {
		return id;
	}
	
	@DriverAction
	public void readAction(){
		
	}
	
	@DriverAction
	public void playAction(){
		
	}
	
	@DriverAction
	public void talkAction(){
		
	}
	
	public static void getActions(){
		
		List<Method> methods = getMethodsAnnotatedWith(Car.class, DriverAction.class);
		for(Method method : methods){
			
			System.out.println(method.getName());
		}
	}
	
	public static List<Method> getMethodsAnnotatedWith(final Class<?> type, final Class<? extends Annotation> annotation) {
	    final List<Method> methods = new ArrayList<Method>();
	    Class<?> klass = type;
	    while (klass != Object.class) { // need to iterated thought hierarchy in order to retrieve methods from above the current instance
	        // iterate though the list of methods declared in the class represented by klass variable, and add those annotated with the specified annotation
	        final List<Method> allMethods = new ArrayList<Method>(Arrays.asList(klass.getDeclaredMethods()));       
	        for (final Method method : allMethods) {
	            if (annotation == null || method.isAnnotationPresent(annotation)) {
	                Annotation annotInstance = method.getAnnotation(annotation);
	                // TODO process annotInstance
	                methods.add(method);
	            }
	        }
	        // move to the upper class in the hierarchy in search for more methods
	        klass = klass.getSuperclass();
	    }
	    return methods;
	}
	
}
