package dal;

public class GasStationHistoryRecord {
	
	public enum ActionType{
		
		Enter, 
		Wash,
		Fuel,
		Exit
	}
	
	public enum ServiceEntityType{
		WashTeam,
		FuelPump
	}
	
	
	private int carID;
	private ActionType actionType;
	private ServiceEntityType serviceEntityType;
	private Integer serviceEntityId;
	
	public GasStationHistoryRecord(	int carId, 
									ActionType actionType, 
									ServiceEntityType serviceEntityType, 
									Integer serviceEntityId ) {
		
		this.carID = carId;
		this.actionType = actionType;
		this.serviceEntityType = serviceEntityType;
		this.serviceEntityId = serviceEntityId;
	}
	
	public int getCarId(){
		return carID;
	}
	
	public ActionType getActionType(){
		return actionType;
	}
	
	public ServiceEntityType getServiceEntityType(){
		return serviceEntityType;
	}
	
	public Integer getServiceEntityId(){
		return serviceEntityId;
	}

}
