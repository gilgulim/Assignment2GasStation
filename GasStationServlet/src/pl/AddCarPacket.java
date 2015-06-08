package pl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import dal.CarObject;

public class AddCarPacket extends BasePacket {
	
	@Expose
	private CarObject car;
	
	public AddCarPacket(CarObject car){
		super();
		
		this.opcode = PacketsOpcodes.AddCarOpcode;
		this.car = car;
	}
	
	public CarObject getCar(){
		return car;
	}
	
}
