package bl;

import gasstation.GasStationUtility;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import pl.CarStatusPacket.CarStatusType;
import dal.GasStationHistoryRecord;
import dal.GasStationMySqlConnection;
import dal.GasStationHistoryRecord.ActionType;
import dal.GasStationHistoryRecord.ServiceEntityType;


public class InsideClean extends CleaningServiceBase {
	private GasStationMySqlConnection connection = GasStationMySqlConnection.getInstance();
	private static int counter = 0;
	private int id;
	
	public InsideClean() {
		super();
		// Create the team id
		id = ++counter;
	}
	
	public int getId() {
		return this.id;
	}

	
	/*
	 * Serve the inside clean
	 */
	protected void serveCar(Car theCar) {
		serveCarInsideClean(theCar);

	}

	private void serveCarInsideClean(Car theCar) {
		double cleaningTime = Math.random();

		countCleaningTime(theCar, cleaningTime);
		setBusy(false);
		
		//update car status
		theCar.sendCarStatus(CarStatusType.AutoWashing);
		this.doneListener.insideCleanIsDone(theCar);
	}

	private void countCleaningTime(Car theCar, double cleaningTime) {
		try {
			Thread.sleep((long)cleaningTime * 1000);
		}
		catch (InterruptedException e) {
			
		}
	}

	@Override
	public void run() {
		insideCleanRunThread();
	}
	
	private void insideCleanRunThread(){
		// Main waiting loop
		while (fClosed == false) {
			
			Car theCar = null;
			
			// Get the new car
			try {
				theCar = cars.poll(WATING_QUEUE_TIMEOUT, TimeUnit.MILLISECONDS);
			}
			catch(InterruptedException e) {
				
			}
			
			if (theCar != null) {
				serveCar(theCar);
			}
		}
	}
}
