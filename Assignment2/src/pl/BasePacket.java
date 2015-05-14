package pl;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public abstract class BasePacket {
	
	public enum PacketsOpcodes{
		AddCarOpcode
	}
	
	private final int PACKET_LEN_SIZE = 4;
	@Expose
	protected PacketsOpcodes opcode;
	protected Gson gsonParser;
	
	
	public BasePacket(){
		 gsonParser = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
	}
	
	public byte[] serialize()
	{
		String jsonStr = gsonParser.toJson(this);
		if(jsonStr != null){
			
			byte[] data = jsonStr.getBytes();
			byte[] len = ByteBuffer.allocate(PACKET_LEN_SIZE).putInt(data.length).array();
			
			int len1 = data.length;
			
			//Adding the length of the data at the beginning of the buffer
			byte[] dataBuffer = new byte[ data.length + len.length ];
			len1 = dataBuffer.length;
			System.arraycopy(len, 0, dataBuffer, 0, len.length);
			System.arraycopy(data, 0, dataBuffer, len.length, data.length);
			
			return dataBuffer;
			
		}
		return null;
	}
	
	public BasePacket deserialize(byte[] data) {
		
		int len = data.length;
		data = Arrays.copyOfRange(data, PACKET_LEN_SIZE, data.length);
		
		len = data.length;
		
		String jsonString = new String(data);
		BasePacket basePacket = (BasePacket) gsonParser.fromJson(jsonString, getClassType());
		return basePacket;
		
	}
	
	public abstract Class getClassType();
	
}
