package cl;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

import pl.AddCarPacket;
import pl.BasePacket;
import pl.BasePacket.PacketsOpcodes;

public class ClientEntity implements Runnable{


	protected Socket socket;
	protected DataInputStream inputStream;
	protected DataOutputStream outputStream;
	
	protected Thread receiveThread;
	protected boolean isActive;
	
	protected IPacketHandler packetHandler; 
	
	public ClientEntity() {
		
		isActive = true;
		receiveThread = new Thread(this);
	}
	
	public ClientEntity(Socket socket) throws IOException {
		this();
		
		this.socket = socket;
		inputStream = new DataInputStream(socket.getInputStream());
		outputStream = new DataOutputStream(socket.getOutputStream());
		receiveThread.start();
	}
	
	public void setPacketHandler(IPacketHandler packetHandler){
		this.packetHandler = packetHandler;
	}
	
	public boolean close(){
		if(socket!=null){
			isActive = false;
			try {
				socket.close();
				return true;
				
			} catch (IOException e) {
				
				return false;
				
			}
		}else{
			return true;
		}
	}

	public boolean sendData(byte[] data){
		try {
			outputStream.write(data);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	@Override
	public void run() {

		byte[] buffer = new byte[GlobalSettings.MAX_BUFFER_SIZE];
        byte[] message = new byte[0];
        int messageLen = -1;
        int bytesReceived;
        
        while (isActive)
        {
			 try
	         {
	             bytesReceived = inputStream.read(buffer);
	             int messageLastIndex = message.length;
	             message = Arrays.copyOf(message, message.length + bytesReceived);
	             System.arraycopy(buffer, 0, message, messageLastIndex, bytesReceived);
	
	             while (message.length >= GlobalSettings.PACKET_LEN_SIZE)
	             {
	                 if (messageLen == -1)
	                 {
	                	 ByteBuffer conv = ByteBuffer.wrap(message);
	                	 messageLen = conv.getInt();
	                 }
	
	                 if (message.length >= messageLen + GlobalSettings.PACKET_LEN_SIZE)
	                 {
	                     byte[] msgData = new byte[messageLen + GlobalSettings.PACKET_LEN_SIZE];
	                     System.arraycopy(message, 0, msgData, 0, messageLen + GlobalSettings.PACKET_LEN_SIZE);
	
	                     BasePacket basePacket = BasePacket.deserialize(msgData, BasePacket.class);
	                     
	                     packetHandler(basePacket.getOpcode(), msgData);
	                     
	                     byte[] temp = new byte[message.length - msgData.length];
	                     System.arraycopy(message, msgData.length, temp, 0, temp.length);
	                     message = temp;
	                     messageLen = -1;
	                 }
	                 else
	                 {
	                     break;
	                 }
	             }
	         }
	         catch (Exception ex)
	         {
	        	 close();
	         }
        }
	} 
	
	
	public void packetHandler(PacketsOpcodes opcode, byte[] msgData){
		
		  switch(opcode){
		  	case AddCarOpcode:
		  		
		  		//Deserializing the received add car packet
		  		AddCarPacket addCarPacket = (AddCarPacket) BasePacket.deserialize(msgData, AddCarPacket.class);
		  		if(packetHandler!=null){
		  			packetHandler.HandlePacket(this, opcode, addCarPacket.getCar());
		  		}
		  		
	         	break;
	
		  	default:
				break;
		  }	  
	}
}
