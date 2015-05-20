package pl;

public class WashActionPacket extends BasePacket {
	
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
