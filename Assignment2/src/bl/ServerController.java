package bl;

import java.util.logging.Level;

import pl.CarStatusPacket.CarStatusType;

public class ServerController {
	private static ServerController theServerController;
	private static Object theServerControllerMutex = new Object();
	BlProxy blProxy = BlProxy.getBlProxy();
	
	
	public static ServerController getServerController(){
		synchronized (theServerControllerMutex) {
			if(theServerController == null){
				theServerController = new ServerController();
			}
		}
		return theServerController;
		
	}
	
	public void addCar(int carId, boolean requiredFuel, int fuelAmount, boolean requiredWash){
  		Car car = new Car(carId, requiredFuel, fuelAmount,requiredWash);
		
		//Setting a random pump number to the car
  		int pumpNum = (int)(Math.random()*blProxy.getNumOfPumps())+1;
  		car.setPumpNum(pumpNum);
		blProxy.addCar(car);
	}
	
	public void updateCarStatus(Car car, CarStatusType status){
		
	}
}
