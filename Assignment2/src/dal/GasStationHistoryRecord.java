package dal;

public class GasStationHistoryRecord {
	
	public enum ActionType{
		
		Enter, 
		Wash,
		Fuel,
		Exit
	}
	
	public enum ServiceEntityType{
		
	}
	
	public enum ServiceEntity{
		
	}
	
	private int carID;
	private ActionType actionType;
	private ServiceEntityType serviceEntityType;
	private ServiceEntity serviceEntity;
	
	public GasStationHistoryRecord(int carId, ActionType actionType, ServiceEntityType serviceEntityType, ServiceEntity serviceEntity){
		
		
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
	
	public ServiceEntity getServiceEntity(){
		return serviceEntity;
	}

}
