package cl;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;

import dal.GasStationMySqlConnection;
import bl.BlProxy;
import bl.Car;
import bl.ClientController;
import pl.AddCarPacket;
import pl.BasePacket;
import pl.BasePacket.PacketsOpcodes;
import pl.CarStatusPacket;
import pl.CarStatusPacket.CarStatusType;


public class ClientEntity implements Runnable{


	protected Socket socket;
	protected DataInputStream inputStream;
	protected DataOutputStream outputStream;
	
	protected Thread receiveThread;
	protected boolean isActive;
	
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
	             isActive = false;
	         }
        }
	} 
	
	
	public void packetHandler(PacketsOpcodes opcode, byte[] msgData){
		BlProxy blProxy = BlProxy.getBlProxy();
		
		  switch(opcode){
		  	case AddCarOpcode:
		  		//Deserializing the received add car packet
		  		AddCarPacket addCarPacket = (AddCarPacket) BasePacket.deserialize(msgData, AddCarPacket.class);
		  		Car receivedCar = addCarPacket.getCar();
		  		
		  		//Setting a random pump number to the car
		  		int pumpNum = (int)(Math.random()*blProxy.getNumOfPumps())+1;
		  		receivedCar.setPumpNum(pumpNum);
		  		
		  		//Setting the client entity to the car object
		  		receivedCar.setClientEntity(this);
	
		  		//Adding car to the business cars queue
	         	blProxy.addCar(	receivedCar.getId(), 
	         					receivedCar.wantsFuel(), 
	         					receivedCar.getPumpNum(), 
	         					receivedCar.getNumOfLiters(), 
	         					receivedCar.wantsCleaning());
	         	 
	         	 //Adding car to DB
	         	 GasStationMySqlConnection connection = GasStationMySqlConnection.getInstance();
	         	 connection.insertCar(addCarPacket.getCar());
	         	 
	         	 //Updated client on car status
	         	 receivedCar.sendStatusToRemoteClient(CarStatusType.Entered);	         	 
	         	 break;
	
		  	default:
				break;
		  }	  
	}
}
