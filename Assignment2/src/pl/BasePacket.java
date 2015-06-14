package pl;
import java.nio.ByteBuffer;
import java.util.Arrays;

import cl.GlobalSettings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

public class BasePacket {
	
	public enum PacketsOpcodes{
		AddCarOpcode,
		CarStatusOpcode,
		WashActionOpcode
	}
	
	@Expose
	protected PacketsOpcodes opcode;
	
	
	public BasePacket(){
		
	}
	
	public byte[] serialize()
	{
		Gson gsonParser = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		String jsonStr = gsonParser.toJson(this);
		if(jsonStr != null){
			
			byte[] data = jsonStr.getBytes();
			byte[] len = ByteBuffer.allocate(GlobalSettings.PACKET_LEN_SIZE).putInt(data.length).array();
			
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
	
	public static BasePacket deserialize(byte[] data, Class type) {
		
		Gson gsonParser = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		
		int len = data.length;
		data = Arrays.copyOfRange(data, GlobalSettings.PACKET_LEN_SIZE, data.length);
		
		len = data.length;
		
		String jsonString = new String(data);
		BasePacket basePacket = (BasePacket) gsonParser.fromJson(jsonString, type);
		return basePacket;
		
	}
	
	public PacketsOpcodes getOpcode(){
		return opcode;
	}
}
