package dal;

import java.util.ArrayList;

import dal.dataObjects.CarObject;
import dal.dataObjects.GasStationHistoryRecord;
import dal.dataObjects.GasStationObject;
import dal.dataObjects.GasStationHistoryRecord.ActionType;

public interface IDataBaseConnection {
		
	public abstract boolean clearDatabase();
	public abstract boolean insertGasStationHistoryRecord(GasStationHistoryRecord historyRecord);
	public abstract boolean insertCar(CarObject car);
	public abstract boolean insertGasStation(GasStationObject gasStation);
	public abstract GasStationObject getGasStationById(int gasStationId);
	public abstract int getLittersByCarId(int carId);
	public abstract int getPricePerLittersByPumpId(int pumpId);
	public abstract int getNumberOfPumps();
	public abstract ArrayList<GasStationHistoryRecord> getStatistics(ActionType actionType, int serviceId);
	
	 
}
