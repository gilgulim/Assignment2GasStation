package gasstation;

import java.io.IOException;

import bl.BlProxy;
import cl.TcpServer;
import dal.GasStationMySqlConnection;

public class ServerMain {

	public static void main(String[] args) {
		//TODO: remove!!!!
		GasStationMySqlConnection connection;
		connection = GasStationMySqlConnection.getInstance();
		connection.clearDatabase();
		
		
		Thread blThread = BlProxy.getBlProxy().runThread();
		
		TcpServer tcpServer = new TcpServer("10.0.0.17", 3456);
		tcpServer.start();
		
		System.out.println("Server started...");
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
