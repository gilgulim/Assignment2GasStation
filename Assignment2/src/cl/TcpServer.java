package cl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class TcpServer implements Runnable {
	private ServerSocket serverSocket;
	private String ipAddress;
	private int portNumber;

	private Thread serverThread;
	private boolean isRunning;
	
	private ArrayList<ClientEntity> clientsList;
	
	public TcpServer(String ipAddress, int portNumber) {
		this.portNumber = portNumber;
		this.ipAddress = ipAddress;
		
		this.isRunning = false;
		serverThread = new Thread(this);
		
		clientsList = new ArrayList<ClientEntity>();
	}

	public void start() {
		if(!isRunning){
			serverThread.start();
		}
	}
	
	public void stop() {
		if(isRunning){
			try {
				
				//Closing tcp server
				isRunning = false;
				serverSocket.close();
				
				//Closing all registered tcp clients
				for(ClientEntity clientEntity : clientsList){
					clientEntity.stop();
				}
				
			} catch (IOException e) {
			}
		}
	}
	
	@Override
	public void run() {
		
		try {
			
			InetAddress addr = InetAddress.getByName(ipAddress);
			serverSocket = new ServerSocket(portNumber, 50, addr);
			
			Socket socket  = serverSocket.accept();
			if(socket!=null){
			
				ClientEntity clientEntity = new ClientEntity(socket);
				clientsList.add(clientEntity);
				clientEntity.start();
			}
			
			
		} catch (UnknownHostException e) {

		}catch (IOException e) {

		}
		
	}
	
	
}
