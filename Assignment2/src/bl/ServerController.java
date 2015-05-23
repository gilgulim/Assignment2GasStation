package bl;

public class ServerController {
	BlProxy blProxy = BlProxy.getBlProxy();
	
	public void addCar(int carId, boolean requiredFuel, int fuelAmount, boolean requiredWash){
  		Car car = new Car(carId, requiredFuel, fuelAmount,requiredWash);
		
		//Setting a random pump number to the car
  		int pumpNum = (int)(Math.random()*blProxy.getNumOfPumps())+1;
  		car.setPumpNum(pumpNum);
		blProxy.addCar(car);
	}
}
