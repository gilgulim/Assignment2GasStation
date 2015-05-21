package bl;

import pl.CarStatusPacket.CarStatusType;

public interface CarChangeState_Observer {
	void updateCarState(Car car, CarStatusType state);
}
