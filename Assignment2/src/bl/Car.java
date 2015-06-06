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

import dal.CarObject;
import dal.GasStationHistoryRecord;
import dal.GasStationHistoryRecord.ActionType;
import dal.GasStationHistoryRecord.ServiceEntityType;
import dal.GasStationMySqlConnection;


public class Car extends CarObject {
	
	private int washTeamID;

	private Boolean isWashing;
	
	private Boolean isFueling;
	
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
		isFueling = false;
		isWashing = false;
		finishCleaning = false;
		finishFuel = false;
	}
	
	public Car(CarObject carObject){
		this(	carObject.getId(),
				carObject.getWantFuel(), 
				carObject.getNumOfLiters(), 
				carObject.getWantCleaning());
		
	}
	
	public Car(int id, boolean wantFuel, int numOfLiters, boolean wantCleaning){
		this();
		this.id = id;
		this.numOfLiters = numOfLiters;
		this.wantFuel = wantFuel;
		this.wantCleaning = wantCleaning;	
	}
	
	public Car(int id,Boolean wantCleaning) {
		this();
		this.id = id;
		this.wantCleaning = wantCleaning;
		this.wantFuel = false;
	}
	
	public Car(int id,int numOfLiters,int pumpNum) {
		this();
		
		this.id = id;
		this.numOfLiters = numOfLiters;
		this.pumpNum = pumpNum;
		this.wantFuel = true;
		this.wantCleaning = false;		
	}
	
	public Car(int id,int numOfLiters,int pumpNum,Boolean wantCleaning) {
		this();
		
		this.id = id;
		this.numOfLiters = numOfLiters;
		this.pumpNum = pumpNum;
		this.wantCleaning = wantCleaning;
		this.wantFuel = true;
	}

	private void initLog() {
		
		theLogger = GasStationUtility.getCarLog(this, id);
		
	}
	
	public void setPumpNum(int pumpNum) {
		this.pumpNum = pumpNum;
	}
	
	public void finishCleaning() {
		
		theLogger.log(Level.INFO, "In Car::finishCleaning()",this);
		theLogger.log(Level.INFO, "In Car::finishCleaning() - Car id = " + id,this);
		
		finishCleaning = true;
		isWashing = false;
	}
	
	public void finishFuel() {
		
		theLogger.log(Level.INFO, "In Car::finishFuel()",this);
		theLogger.log(Level.INFO, "In Car::finishFuel() - Car id = " + id,this);
		
		finishFuel = true;
		isFueling = false;
	}
	
	public boolean isFueled() {
		return finishFuel;
	}
	
	public boolean isCleaned() {
		return finishCleaning;
	}
	
	public Boolean getIsWashing() {
		return isWashing;
	}

	public void setIsWashing(Boolean isWashing) {
		this.isWashing = isWashing;
	}

	public Boolean getIsFueling() {
		return isFueling;
	}

	public void setIsFueling(Boolean isFueling) {
		this.isFueling = isFueling;
	}
	
	public void setClientEntity(ClientEntity clientEntity) {
		
		this.clientEntity = clientEntity;
	}
	
	public boolean sendCarStatus(CarStatusType carStatus){		
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
						this.getWashTeamID()+1);
				 GasStationMySqlConnection.getInstance().insertGasStationHistoryRecord(historyRecord);
				 sendRandomActionToRemoteClient();
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
		
		//update server GUI
		ServerController.getServerController().updateCarStatus(this, carStatus);
		
		//send status to client
		CarStatusPacket carStatusPacket = new CarStatusPacket(carStatus);
		if(clientEntity!= null){
			return clientEntity.sendData(carStatusPacket.serialize());
		}
		return false;
	}
	
	@DriverActionAnnotation
	public String readAction(){
		return "Read Action";
	}
	
	@DriverActionAnnotation
	public String playAction(){
		return "PlayAction";
	}
	
	@DriverActionAnnotation
	public String talkAction(){
		return "TalkAction";
	}
	
	private boolean sendRandomActionToRemoteClient(){
		WashActionPacket washActionPacket = new WashActionPacket(getDriverRandomActionName());
		if(clientEntity!= null){
			return clientEntity.sendData(washActionPacket.serialize());
		}
		return false;
	}
	
	private String getDriverRandomActionName(){
		
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
	
}
