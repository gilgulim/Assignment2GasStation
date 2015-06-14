package cl;

import java.io.IOException;
import bl.BlProxy;
import dal.GasStationDataBaseManager;
import dal.IDataBaseConnection;

public class ServerMainTester {

	public static void main(String[] args) {
		IDataBaseConnection connection = GasStationDataBaseManager.getInstance();
		//connection.clearDatabase();
		
		
		Thread blThread = BlProxy.getBlProxy().runThread();
		
		TcpServer tcpServer = new TcpServer("192.168.43.59", 3456);
		tcpServer.start();
		
		
		System.out.println("Server started1...");
		
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
