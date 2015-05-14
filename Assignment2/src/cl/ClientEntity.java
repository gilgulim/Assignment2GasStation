package cl;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;


public class ClientEntity implements Runnable{


	private Socket socket;
	private DataInputStream inputStream;
	
	private Thread clientThread;
	private boolean isActive;
	
	
	public ClientEntity(Socket socket) throws IOException
	{
		this.socket = socket;
		inputStream = new DataInputStream(socket.getInputStream());
		
		isActive = true;
		clientThread = new Thread(this);
		clientThread.start();
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

	@Override
	public void run() {
		
		byte[] buffer = new byte[GlobalSettings.MAX_BUFFER_SIZE];
		byte[] packetData = null;
		int packetLen = 0;
		int bytesReceived;
		
		while(isActive){	
			try {
				
				bytesReceived = inputStream.read(buffer);
				
				if(packetData == null){
					
					packetLen =0;
					packetData = new byte[bytesReceived];
					System.arraycopy(buffer, 0, packetData, 0, bytesReceived);
					
				}else{
					
					 //TODO: Fix in here in case the buffer contains part of the next packet
					int currentIndex = packetData.length;
					packetData = Arrays.copyOf(packetData, packetData.length + bytesReceived);
					System.arraycopy(buffer, 0, packetData, currentIndex, bytesReceived);
				}
				
				if((packetLen == 0) && (bytesReceived >= GlobalSettings.PACKET_LEN_SIZE))
				{
					ByteBuffer conv = ByteBuffer.wrap(packetData);
					packetLen = conv.getInt();
				}
				
				if(packetData.length == packetLen+ GlobalSettings.PACKET_LEN_SIZE){
					
					//Raise an event that a packet has been received.
					
					packetData = null;
				}
				
				
				
			} catch (IOException e) {
				isActive = false;
				try {
					if(socket!=null){
						socket.close();
						socket = null;
					}
				} catch (IOException e1) {
					
				}
			}
			
		}
		
		System.out.println("Client entity closed.");
		
	} 
	
	
	
	
}
