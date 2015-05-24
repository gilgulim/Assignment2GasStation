package ui;

public class StatisticsRecord {
	
	private String dateTime;
	private String carId;
	private String actionType;
	private String serviceId;
	private String Profit;
	
	public StatisticsRecord(String dateTime, String carId, String actionType,
			String serviceId, String profit) {
		
		this.dateTime = dateTime;
		this.carId = carId;
		this.actionType = actionType;
		this.serviceId = serviceId;
		this.Profit = profit;
	}
	
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String datetime) {
		this.dateTime = datetime;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getProfit() {
		return Profit;
	}
	public void setProfit(String profit) {
		Profit = profit;
	}
}
