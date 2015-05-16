package pl;

public class WashActionPacket extends BasePacket {
	
	public enum WashAction {
		Read,
		Play,
		Talk,
	}
	
	private WashAction washAction;
	
	public WashActionPacket(WashAction washAction){
		super();
		
		this.opcode = PacketsOpcodes.WashActionOpcode;
		this.washAction = washAction;
	}
	
	public WashAction getWashAction() {
		return washAction;
	}

}
