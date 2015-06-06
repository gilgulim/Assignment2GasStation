package bl;

import pl.CarStatusPacket.CarStatusType;

public interface GasStationRemoteClient_Observer {
	void ReceivedCarStatusHandler(CarStatusType carStatus);
	void ReceivedCarWashAction(String methodName);
}
