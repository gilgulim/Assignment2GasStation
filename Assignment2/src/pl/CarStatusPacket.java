package pl;

import com.google.gson.annotations.Expose;

public class CarStatusPacket extends BasePacket {
	
	public enum CarStatusType{
		Entered,
		Fueling,
		AutoWashing,
		InteranlCleaning,
		Exited,
	}
	
	@Expose
	private CarStatusType carStatus;
	
	public CarStatusPacket(CarStatusType carStatus){
		super();
		
		this.opcode = PacketsOpcodes.CarStatusOpcode;
		this.carStatus = carStatus;
	}
	
	public CarStatusType getCarStatus(){
		return carStatus;
	}

}
