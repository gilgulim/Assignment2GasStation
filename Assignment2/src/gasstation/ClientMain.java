package gasstation;

import java.io.IOException;

import pl.AddCarPacket;
import pl.BasePacket;
import bl.Car;
import cl.TcpClient;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ClientMain {

	public static void main(String[] args) {
		
		TcpClient tcpClient = new TcpClient("192.168.2.102", 8000);
		tcpClient.connect();
		
		AddCarPacket addCarPacket = new AddCarPacket(new Car(2, 200 ,3,true));
		byte[] data = addCarPacket.serialize();
		BasePacket basePacket =  addCarPacket.deserialize(data);
	
		tcpClient.sendData(addCarPacket.serialize());
		
		System.out.println("Client started...");
		
		
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
