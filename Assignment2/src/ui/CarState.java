package ui;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CarState {
	private ObjectProperty<CarCell> opDispatcher = new SimpleObjectProperty<CarCell>();
	private ObjectProperty<CarCell> opAutoWash = new SimpleObjectProperty<CarCell>();
	private ObjectProperty<CarCell> opManualWash = new SimpleObjectProperty<CarCell>();
	private ObjectProperty<CarCell> opFuel = new SimpleObjectProperty<CarCell>();
	private ObjectProperty<CarCell> opLeftStation = new SimpleObjectProperty<CarCell>();

	public CarState(ObjectProperty<CarCell> dispatcher, ObjectProperty<CarCell> autoWash, ObjectProperty<CarCell> manualWash, ObjectProperty<CarCell> fuel, ObjectProperty<CarCell> leftStation){
		setOpDispatcher(dispatcher);
		setOpManualWash(manualWash);
		setOpAutoWash(autoWash);
		setOpFuel(fuel);
		setOpLeftStation(leftStation);
	}

	public ObjectProperty<CarCell> getOpDispatcher() {
		return opDispatcher;
	}

	public void setOpDispatcher(ObjectProperty<CarCell> opDispatcher) {
		this.opDispatcher = opDispatcher;
	}

	public ObjectProperty<CarCell> getOpAutoWash() {
		return opAutoWash;
	}

	public void setOpAutoWash(ObjectProperty<CarCell> opAutoWash) {
		this.opAutoWash = opAutoWash;
	}

	public ObjectProperty<CarCell> getOpManualWash() {
		return opManualWash;
	}

	public void setOpManualWash(ObjectProperty<CarCell> opManualWash) {
		this.opManualWash = opManualWash;
	}

	public ObjectProperty<CarCell> getOpFuel() {
		return opFuel;
	}

	public void setOpFuel(ObjectProperty<CarCell> opFuel) {
		this.opFuel = opFuel;
	}

	public ObjectProperty<CarCell> getOpLeftStation() {
		return opLeftStation;
	}

	public void setOpLeftStation(ObjectProperty<CarCell> opLeftStation) {
		this.opLeftStation = opLeftStation;
	}


	
	
}
