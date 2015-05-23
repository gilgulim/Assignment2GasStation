package ui;

public class CarStatusRecord {
	String carId, carFuel, carWash,carLeft;

	public CarStatusRecord(String carId, String carFuel, String carWash, String carLeft){
		this.carId = carId;
		this.carFuel = carFuel;
		this.carWash = carWash;
		this.carLeft = carLeft;
	}
	
	public String getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId = carId;
	}

	public String getCarFuel() {
		return carFuel;
	}

	public void setCarFuel(String carFuel) {
		carFuel = carFuel;
	}

	public String getCarWash() {
		return carWash;
	}

	public void setCarWash(String carWash) {
		carWash = carWash;
	}

	public String getCarLeft() {
		return carLeft;
	}

	public void setCarLeft(String carLeft) {
		carLeft = carLeft;
	}
	
}
