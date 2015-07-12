package dal.dataObjects;

public class FuelPoolObject {
	protected int id;
	protected int maxCapacity;
	protected int currentCapacity;

	
	public FuelPoolObject(int id, int maxCapacity, int currentCapacity){
		this.id = id;
		this.maxCapacity = maxCapacity;
		this.currentCapacity = currentCapacity;
	}
	
	public int getId() {
		return id;
	}
	public int getMaxCapacity() {
		return maxCapacity;
	}
	public int getCurrentCapacity() {
		return currentCapacity;
	}
}
