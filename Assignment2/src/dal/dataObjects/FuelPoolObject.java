package dal.dataObjects;

public class FuelPoolObject {
	protected int id;
	protected int maxCapacity;
	protected int currentCapacity;
	
	public FuelPoolObject(int id, int maxCapacity, int currentCapacity){
		this.maxCapacity = maxCapacity;
		this.currentCapacity = currentCapacity;
	}
	
	public int getMaxCapacity() {
		return maxCapacity;
	}
	public int getCurrentCapacity() {
		return currentCapacity;
	}
}
