package cl;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

public class ClientEntity implements Runnable{

	private final int MAX_BUFFER_SIZE = 1024;
	private Socket socket;
	private DataInputStream netInputStream;
	
	private Thread clientThread;
	private boolean isRunning;
	
	
	public ClientEntity(Socket socket) throws IOException
	{
		this.socket = socket;
		this.netInputStream = new DataInputStream(socket.getInputStream());
		
		this.isRunning = false;
		this.clientThread = new Thread(this);
	}
	
	public void start(){
		if(!isRunning){
			isRunning = true;
			clientThread.start();
		}
	}
	
	public void stop(){
		if(isRunning){
			try {
				
				isRunning = false;
				socket.close();
				
			} catch (IOException e) {
			}
		}
	}

	@Override
	public void run() {
		
		byte[] buffer = new byte[MAX_BUFFER_SIZE];
		short packetLength = -1;
		
		while(isRunning){	
			try {
				
				int bytesRead = netInputStream.read(buffer);
				
				//This is the beginning of the packet
				if(packetLength == -1){
						
					ByteBuffer conv = ByteBuffer.wrap(buffer);
					packetLength = conv.getShort();
					
				}else{
					
					//System.arraycopy(src, srcPos, dest, destPos, length);
					
				}
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				isRunning = false;
			}
			
		}
		
		
	} 
	
	
	
	
}
