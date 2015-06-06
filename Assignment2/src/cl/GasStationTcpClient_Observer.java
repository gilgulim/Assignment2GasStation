package cl;

import pl.CarStatusPacket.CarStatusType;

public interface GasStationTcpClient_Observer {
	void ReceivedCarStatusHandler(CarStatusType carStatus);
	void ReceivedCarWashAction(String methodName);
}
