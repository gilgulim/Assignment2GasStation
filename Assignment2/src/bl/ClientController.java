package bl;

import java.util.ArrayList;
import java.util.List;

import bl.observers.GasStationRemoteClient_Observer;
import pl.AddCarPacket;
import pl.BasePacket.PacketsOpcodes;
import pl.CarStatusPacket.CarStatusType;
import cl.ClientEntity;
import cl.IPacketHandler;
import cl.TcpClient;

public class ClientController implements IPacketHandler{
	private TcpClient tcpClient;
	private boolean status = false;
	private List<GasStationRemoteClient_Observer> gasStationClientObservers;

	public ClientController(String serverIp, int port){
		gasStationClientObservers = new ArrayList<GasStationRemoteClient_Observer>();
		
		tcpClient = new TcpClient(serverIp, port);
		tcpClient.setPacketHandler(this);
		status = tcpClient.connect();
		
	}
	
	public void addCar(int carId,boolean requiredFuel,int fuelAmount,boolean requiredWash){
		Car car = new Car(carId, requiredFuel,fuelAmount,requiredWash);
		
		AddCarPacket carPacket = new AddCarPacket(car);
		tcpClient.sendData(carPacket.serialize());
	}

	public boolean isStatus() {
		return status;
	}
	
	public void attachObserver(GasStationRemoteClient_Observer observer){
		gasStationClientObservers.add(observer);
	}
	
	private void carStatusNotifyAll(CarStatusType carStatus) {
		
		for(GasStationRemoteClient_Observer observer : gasStationClientObservers){
			observer.ReceivedCarStatusHandler(carStatus);
		}
	}
	
	private void carWashActionNotifyAll(String washAction) {
		
		for(GasStationRemoteClient_Observer observer : gasStationClientObservers){
			observer.ReceivedCarWashAction(washAction);
		}
	}

	@Override
	public void HandlePacket(ClientEntity sender, PacketsOpcodes opcode, Object data) {
		
		switch(opcode){
			case CarStatusOpcode:
				CarStatusType carStatusType = (CarStatusType)data;
				carStatusNotifyAll(carStatusType);
				break;
			case WashActionOpcode:
				String washAction = (String)data;
				carWashActionNotifyAll(washAction);
				break;
			default:
				break;
			
		}
	}
}
