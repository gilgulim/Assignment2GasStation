package pl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import bl.Car;

public class AddCarPacket extends BasePacket {
	
	@Expose
	private Car car;
	
	public AddCarPacket(Car car){
		super();
		
		this.opcode = PacketsOpcodes.AddCarOpcode;
		this.car = car;
	}

	@Override
	public Class getClassType() {
		
		return AddCarPacket.class;
		
	}
	
}
