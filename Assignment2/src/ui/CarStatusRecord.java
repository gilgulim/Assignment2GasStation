package ui;

import javafx.beans.property.SimpleStringProperty;

public class CarStatusRecord {
	private SimpleStringProperty carId, carFuel, carWash,carLeft;

	public CarStatusRecord(String carId, String carFuel, String carWash, String carLeft){
		this.carId = new SimpleStringProperty(carId);
		this.carFuel = new SimpleStringProperty(carFuel);
		this.carWash = new SimpleStringProperty(carWash);
		this.carLeft = new SimpleStringProperty(carLeft);
	}
	
	public SimpleStringProperty getCarId() {
		return carId;
	}

	public void setCarId(String carId) {
		this.carId.set(carId);
	}

	public SimpleStringProperty getCarFuel() {
		return carFuel;
	}

	public void setCarFuel(String carFuel) {
		this.carFuel.set(carFuel);
	}

	public SimpleStringProperty getCarWash() {
		return carWash;
	}

	public void setCarWash(String carWash) {
		this.carWash.set(carWash);
	}

	public SimpleStringProperty getCarLeft() {
		return carLeft;
	}

	public void setCarLeft(String carLeft) {
		this.carLeft.set(carLeft);
	}
	
}
