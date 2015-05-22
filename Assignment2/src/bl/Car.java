package bl;

import gasstation.GasStationUtility;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.CarStatusPacket;
import pl.WashActionPacket;
import pl.CarStatusPacket.*;
import cl.ClientEntity;

import com.google.gson.annotations.*;

import dal.GasStationHistoryRecord;
import dal.GasStationHistoryRecord.ActionType;
import dal.GasStationHistoryRecord.ServiceEntityType;
import dal.GasStationMySqlConnection;


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
	
	private int washTeamID;
	private List<CarChangeState_Observer> carChangeStateObservers = new ArrayList<CarChangeState_Observer>();
	
	public int getWashTeamID() {
		return washTeamID;
	}
	
	public void setWashTeamID(int washTeamID) {
		this.washTeamID = washTeamID;
	}

	private ClientEntity clientEntity;
	
	public ClientEntity getClientEntity() {
		return clientEntity;
	}

	private Logger theLogger;
	
	public Car(){
		initLog();
		theLogger.log(Level.INFO, "In Car::Car()",this);
		theLogger.log(Level.INFO, "In Car::Car() - created Car id = " + id,this);
	}
	
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
	
	public void setClientEntity(ClientEntity clientEntity) {
		
		this.clientEntity = clientEntity;
	}
	
	public boolean updateCarStatus(CarStatusType carStatus){		
		//update DB
		GasStationHistoryRecord historyRecord;
		
		switch (carStatus){
			case Entered :
				historyRecord = new GasStationHistoryRecord(
						this.getId(),
						ActionType.Enter,
						null,
						null);
				GasStationMySqlConnection.getInstance().insertGasStationHistoryRecord(historyRecord);
				break;
		
			case Fueling : 
				historyRecord = new GasStationHistoryRecord(						
						this.getId(),
						ActionType.Fuel,
						ServiceEntityType.FuelPump,
						this.getPumpNum());		
				GasStationMySqlConnection.getInstance().insertGasStationHistoryRecord(historyRecord);
				break;
				
			case AutoWashing :
				 historyRecord = new GasStationHistoryRecord(
						this.getId(),
						ActionType.Wash,
						ServiceEntityType.WashTeam,
						this.getWashTeamID());
				 GasStationMySqlConnection.getInstance().insertGasStationHistoryRecord(historyRecord);
				break;
			
			case Exited : 
				historyRecord = new GasStationHistoryRecord(
						this.getId(),
						ActionType.Exit,
						null,
						null);
				GasStationMySqlConnection.getInstance().insertGasStationHistoryRecord(historyRecord);
				break;
				
			default :
				break;
		}
		
		//update server
		notifyAll(carStatus);
		
		//send status to client
		CarStatusPacket carStatusPacket = new CarStatusPacket(carStatus);
		if(clientEntity!= null){
			return clientEntity.sendData(carStatusPacket.serialize());
		}
		return false;
	}
	
	public boolean sendRandomActionToRemoteClient(){
		
		WashActionPacket washActionPacket = new WashActionPacket(getDriverRandomActionName());
		
		if(clientEntity!= null){
			return clientEntity.sendData(washActionPacket.serialize());
		}
		return false;
	}
	
	@DriverActionAnnotation
	public void readAction(){
		System.out.println("Read Action");
	}
	
	@DriverActionAnnotation
	public void playAction(){
		System.out.println("PlayAction");
	}
	
	@DriverActionAnnotation
	public void talkAction(){
		System.out.println("TalkAction");
	}
	
	public String getDriverRandomActionName(){
		
		List<Method> methods = getMethodsAnnotatedWith(Car.class, DriverActionAnnotation.class);
		Random r = new Random();
		int Low = 0;
		int High = methods.size();
		int index = (r.nextInt(High-Low) + Low);
		
		return methods.get(index).getName();
	}
	
	private List<Method> getMethodsAnnotatedWith(final Class<?> type, Class<? extends Annotation> annotation) {
		
	    List<Method> methods = new ArrayList<Method>();
	    Class<?> className = type;
	    while (className != Object.class) { 
	    	
	       List<Method> allMethods = new ArrayList<Method>(Arrays.asList(className.getDeclaredMethods()));       
	        
	        for (Method method : allMethods) {
	        	
	            if (annotation == null || method.isAnnotationPresent(annotation)) {
	                methods.add(method);
	            }
	        }
	        
	        className = className.getSuperclass();
	    }
	    return methods;
	}
	
	private void notifyAll(CarStatusType state){
		for(CarChangeState_Observer observer : carChangeStateObservers){
			observer.updateCarState(this, state);
		}
	}
	
	public void attachObserver(CarChangeState_Observer observer){
		
		if(observer != null){
			carChangeStateObservers.add(observer);	
		}
	}
	
}
