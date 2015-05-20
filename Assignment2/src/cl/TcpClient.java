package cl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import pl.BasePacket.PacketsOpcodes;
import pl.BasePacket;
import pl.CarStatusPacket;
import pl.CarStatusPacket.CarStatusType;
import pl.WashActionPacket;

public class TcpClient extends  ClientEntity{
	
	private String remoteIp;
	private int remotePort;
	private List<GasStationClient_Observer> gasStationClientObservers;
	
	public TcpClient(String ipAddress, int portNumber){
		super();
		
		socket = null;
		remoteIp = ipAddress;
		remotePort = portNumber;
		
		gasStationClientObservers = new ArrayList<GasStationClient_Observer>();
	}
	
	public boolean connect() {
		
		if(socket != null && socket.isConnected()){
			
			return true;
			
		}else {
			
			try {
				
				socket = new Socket(remoteIp, remotePort);
				outputStream = new DataOutputStream(socket.getOutputStream());
				inputStream = new DataInputStream(socket.getInputStream());
				receiveThread.start();
				return true;
				
			} catch (IOException e) {
				return false;
			}
		}
		
	}
	
	public void attachObserver(GasStationClient_Observer observer){
		gasStationClientObservers.add(observer);
	}
	
	@Override
	public void packetHandler(PacketsOpcodes opcode, byte[] msgData){
		
		switch(opcode){
		case CarStatusOpcode:
			CarStatusPacket carStatusPacket = (CarStatusPacket)BasePacket.deserialize(msgData, CarStatusPacket.class);
			carStatusNotifyAll(carStatusPacket.getCarStatus());
			break;
		case WashActionOpcode:
			WashActionPacket washActionPacket = (WashActionPacket)BasePacket.deserialize(msgData, WashActionPacket.class);
			carWashActionNotifyAll(washActionPacket.getWashAction());
			break;
		default:
			break;
		}
	
	}
	
	private void carStatusNotifyAll(CarStatusType carStatus) {
		
		for(GasStationClient_Observer observer : gasStationClientObservers){
			observer.ReceivedCarStatusHandler(carStatus);
		}
	}
	
	private void carWashActionNotifyAll(String washAction) {
		
		for(GasStationClient_Observer observer : gasStationClientObservers){
			observer.ReceivedCarWashAction(washAction);
		}
	}
	
	
}
