package dalMySQL;

import dal.GasStationManagerAbstract;
import dataObjects.CarObject;

public class GasStationMySQLManager extends GasStationManagerAbstract {
	public boolean insertCar(CarObject carObject) {
		System.out.println("car" + carObject);
		return false;
	}
}
