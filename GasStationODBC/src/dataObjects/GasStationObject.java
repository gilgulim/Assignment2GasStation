package dataObjects;

import com.google.gson.annotations.Expose;

public class GasStationObject {
	private int id;
	private CleaningServiceObject cleaningServiceObject;
	private FuelPoolObject fuelPoolObject;
	
	public int getId() {
		return id;
	}
	public CleaningServiceObject getCleaningServiceObject() {
		return cleaningServiceObject;
	}
	
	public FuelPoolObject getFuelPoolObject(){
		return fuelPoolObject;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setCleaningServiceObject(CleaningServiceObject cleaningServiceObject) {
		this.cleaningServiceObject = cleaningServiceObject;
	}
	public void setFuelPoolObject(FuelPoolObject fuelPoolObject) {
		this.fuelPoolObject = fuelPoolObject;
	}

	
	
}
