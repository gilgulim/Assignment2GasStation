package bl;

import java.util.ArrayList;
import java.util.List;

import pl.AddCarPacket;
import pl.CarStatusPacket.CarStatusType;
import cl.GasStationTcpClient_Observer;
import cl.TcpClient;

public class ClientController implements  GasStationTcpClient_Observer{
	private TcpClient tcpClient;
	private boolean status = false;
	private List<GasStationTcpClient_Observer> gasStationClientObservers;

	public ClientController(String serverIp, int port){
		gasStationClientObservers = new ArrayList<GasStationTcpClient_Observer>();
		
		tcpClient = new TcpClient(serverIp, port);
		tcpClient.attachObserver(this);
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
	
	public void attachObserver(GasStationTcpClient_Observer observer){
		gasStationClientObservers.add(observer);
	}

	@Override
	public void ReceivedCarStatusHandler(CarStatusType carStatus) {
		carStatusNotifyAll(carStatus);
	}

	@Override
	public void ReceivedCarWashAction(String methodName) {
		carWashActionNotifyAll(methodName);
	}
	
	private void carStatusNotifyAll(CarStatusType carStatus) {
		
		for(GasStationTcpClient_Observer observer : gasStationClientObservers){
			observer.ReceivedCarStatusHandler(carStatus);
		}
	}
	
	private void carWashActionNotifyAll(String washAction) {
		
		for(GasStationTcpClient_Observer observer : gasStationClientObservers){
			observer.ReceivedCarWashAction(washAction);
		}
	}
}
