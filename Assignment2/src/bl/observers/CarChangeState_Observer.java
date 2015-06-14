package bl.observers;

import bl.Car;
import pl.CarStatusPacket.CarStatusType;

public interface CarChangeState_Observer {
	void updateCarState(Car car, CarStatusType state);
}
