package bl;

import pl.AddCarPacket;
import cl.TcpClient;

public class ClientController {
	private TcpClient tcpClient;
	private boolean status = false;

	
	public ClientController(String serverIp, int port){
		tcpClient = new TcpClient(serverIp, port);
		status = tcpClient.connect();
		System.out.println("server status is: " + status);
	}
	
	
	public void addCar(int carId,boolean requiredFuel,int fuelAmount,boolean requiredWash){
		Car car = new Car(carId, requiredFuel,fuelAmount,requiredWash);
		
		AddCarPacket carPacket = new AddCarPacket(car);
		
		
		tcpClient.sendData(carPacket.serialize());
	}

}
