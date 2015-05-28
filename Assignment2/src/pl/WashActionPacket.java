package pl;

import com.google.gson.annotations.Expose;

public class WashActionPacket extends BasePacket {
	
	@Expose
	private String methodName;
	
	public WashActionPacket(String methodName){
		super();
		
		this.opcode = PacketsOpcodes.WashActionOpcode;
		this.methodName = methodName;
	}
	
	public String getWashAction() {
		return methodName;
	}

}
