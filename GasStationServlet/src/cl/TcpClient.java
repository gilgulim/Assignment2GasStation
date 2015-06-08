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
	
	public TcpClient(String ipAddress, int portNumber){
		super();
		
		socket = null;
		remoteIp = ipAddress;
		remotePort = portNumber;
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
	
	
	@Override
	public void packetHandler(PacketsOpcodes opcode, byte[] msgData){
		
		switch(opcode){
		case CarStatusOpcode:
			CarStatusPacket carStatusPacket = (CarStatusPacket)BasePacket.deserialize(msgData, CarStatusPacket.class);
			if(packetHandler != null){
				packetHandler.HandlePacket(this, opcode, carStatusPacket.getCarStatus());
			}
			break;
		case WashActionOpcode:
			WashActionPacket washActionPacket = (WashActionPacket)BasePacket.deserialize(msgData, WashActionPacket.class);
			
			if(packetHandler != null){
				packetHandler.HandlePacket(this, opcode, washActionPacket.getWashAction());
			}
			break;
		default:
			break;
		}
	
	}
	
	
}
