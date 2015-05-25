package ui;

import javafx.beans.property.SimpleStringProperty;

public class CarStatusRecord {
	private String carId, carFuel, carWash,carLeft;

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
		this.carFuel = carFuel;
	}

	public String getCarWash() {
		return carWash;
	}

	public void setCarWash(String carWash) {
		this.carWash = carWash;
	}

	public String getCarLeft() {
		return carLeft;
	}

	public void setCarLeft(String carLeft) {
		this.carLeft = carLeft;
	}
	

	
}
