package gasstation;

import java.io.IOException;

import ui.ServerGUI;
import bl.BlProxy;
import cl.TcpServer;
import dal.GasStationMySqlConnection;

public class ServerMain {

	public static void main(String[] args) {
		GasStationMySqlConnection connection;
		connection = GasStationMySqlConnection.getInstance();
		connection.clearDatabase();
		
		
		Thread blThread = BlProxy.getBlProxy().runThread();
		
		TcpServer tcpServer = new TcpServer("192.168.43.59", 3456);
		tcpServer.start();
		
		System.out.println("Server started...");
		ServerGUI.main(args);
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
