package bl;

import gasstation.GasStationUtility;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoClean extends CleaningServiceBase {
	private int secondsPerAutoClean;
	
	public AutoClean(int secondsPerAutoClean) {
		
		super();
		this.secondsPerAutoClean = secondsPerAutoClean;
	}
	
	
	protected void serveCar(Car theCar) {
		serveCarAutoClean(theCar);
	}
	
	private void serveCarAutoClean(Car theCar) {
		// Clean the car by sleeping for a fixed time
		try {
			Thread.sleep(secondsPerAutoClean * 1000);
		}
		catch (InterruptedException e) {
			
		}
		setBusy(false);
		
		this.doneListener.autoCleanIsDone(theCar);
		
	}


	// Thread entry point
	@Override
	public void run() {
		autoCleanRunThread();

	}


	private void autoCleanRunThread() {
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
