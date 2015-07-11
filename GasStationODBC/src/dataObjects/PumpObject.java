package dataObjects;

public class PumpObject {
	
	protected int id;
	protected double pricePerLiter;
	
	public PumpObject(int id, double pricePerLiter) {
		this.id = id;
		this.pricePerLiter = pricePerLiter;
	}
	
	public int getId() {
		return id;
	}
	public double getPricePerLiter() {
		return pricePerLiter;
	}
}
