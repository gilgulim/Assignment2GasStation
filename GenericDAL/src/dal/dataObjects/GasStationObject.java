package dal.dataObjects;

import java.util.ArrayList;


public class GasStationObject {
	
	private int id;
	
	private CleaningServiceObject cleaningServiceObject;
	private FuelPoolObject fuelPoolObject;

	private ArrayList<CarObject> carsList;
	private ArrayList<PumpObject> pumpsList;

	
	public GasStationObject(int id){
		
		this.id = id;
		this.carsList = new ArrayList<CarObject>();
		this.pumpsList = new ArrayList<PumpObject>();
	}
	
	public int getId() {
		return id;
	}

	public void addCar(CarObject carObject){
		carsList.add(carObject);
	}
	
	public void addPump(PumpObject pumpObject){
		pumpsList.add(pumpObject);
	}
	
	public CleaningServiceObject getCleaningServiceObject() {
		return cleaningServiceObject;
	}

	public void setCleaningServiceObject(CleaningServiceObject cleaningServiceObject) {
		this.cleaningServiceObject = cleaningServiceObject;
	}

	public FuelPoolObject getFuelPoolObject() {
		return fuelPoolObject;
	}

	public void setFuelPoolObject(FuelPoolObject fuelPoolObject) {
		this.fuelPoolObject = fuelPoolObject;
	}
	
	public ArrayList<CarObject> getCarsList() {
		return carsList;
	}

	public ArrayList<PumpObject> getPumpsList() {
		return pumpsList;
	}
	
}
