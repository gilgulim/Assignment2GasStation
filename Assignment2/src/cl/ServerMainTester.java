package cl;

import java.io.IOException;

import ui.ServerGUI;
import bl.BlProxy;
import dal.GasStationMySqlConnection;

public class ServerMainTester {

	public static void main(String[] args) {
		GasStationMySqlConnection connection;
		connection = GasStationMySqlConnection.getInstance();
		//connection.clearDatabase();
		
		
		Thread blThread = BlProxy.getBlProxy().runThread();
		
		TcpServer tcpServer = new TcpServer("192.168.43.59", 3456);
		tcpServer.start();
		
		System.out.println("Server started...");
		try {
			System.in.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
