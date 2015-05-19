package gasstation;

import java.io.IOException;

import cl.TcpServer;

public class ServerMain {

	public static void main(String[] args) {
		
		TcpServer tcpServer = new TcpServer("192.168.2.101", 8000);
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
