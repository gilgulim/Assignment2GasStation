package dalODBC;

import java.util.ArrayList;

import dataObjects.CarObject;
import dataObjects.GasStationHistoryRecord;
import dataObjects.GasStationObject;
import dataObjects.GasStationHistoryRecord.ActionType;

public interface IDataBaseConnection {
		
	boolean clearDatabase();
	boolean insertGasStationHistoryRecord(GasStationHistoryRecord historyRecord);
	boolean insertCar(CarObject car);
	boolean insertGasStation(GasStationObject gasStation);
	GasStationObject getGasStationById(int gasStationId);
	int getLittersByCarId(int carId);
	int getPricePerLittersByPumpId(int pumpId);
	int getNumberOfPumps();
	ArrayList<GasStationHistoryRecord> getStatistics(ActionType actionType, int serviceId);
	
	 
}
