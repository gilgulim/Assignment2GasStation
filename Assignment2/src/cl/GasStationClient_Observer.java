package cl;

import pl.CarStatusPacket.CarStatusType;

public interface GasStationClient_Observer {
	void ReceivedCarStatusHandler(CarStatusType carStatus);
	void ReceivedCarWashAction(String methodName);
}
