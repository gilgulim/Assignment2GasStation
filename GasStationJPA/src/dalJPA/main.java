package dalJPA;

import dal.dataObjects.CarObject;


public class main {
	public static void main(String[] args) {
		
		GasStationJPAManager jpaManager = new GasStationJPAManager();
		jpaManager.insertCar(new CarObject(111, true, true, 23));
		
	}
}
