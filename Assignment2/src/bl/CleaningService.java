package bl;

import gasstation.GasStationUtility;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import dal.GasStationMySqlConnection;
import dal.dataObjects.CleaningServiceObject;
import dal.dataObjects.GasStationHistoryRecord;
import dal.dataObjects.GasStationHistoryRecord.ActionType;
import dal.dataObjects.GasStationHistoryRecord.ServiceEntityType;

public class CleaningService extends CleaningServiceObject implements Runnable, CleaningDoneIF {
	
	private AutoClean autoClean;
	private ArrayList<InsideClean> insideCleanArr;
	private ArrayList<Thread> insideCleanThreads = null;
	
	private int numOfCarsServed;
	private Object numOfCarsServedMutex;
	
	private BlockingQueue<Car>cars;
	private static final int WATING_QUEUE_LEN = 10;
	private static final int WATING_QUEUE_TIMEOUT = 10; // MS
	
	private Queue<Car> autoCleaningWaitingQueue;
	
	private Queue<Car> insideCleaningWaitingQueue;
	private Object insideCleaningWaitingQueueMutex;
	
	private boolean fClosed;
	
	private Thread autoCleanThread;
	
	public CleaningService(int numOfTeams, int price, int secondsPerAutoClean) {
		
		super(0, numOfTeams, price, secondsPerAutoClean);
		
		insideCleanArr = new ArrayList<InsideClean>();
		
		this.numOfCarsServed = 0;
		numOfCarsServedMutex = new Object();
		
		cars = new ArrayBlockingQueue<Car>(WATING_QUEUE_LEN);
		
		autoCleaningWaitingQueue		= new LinkedBlockingQueue<Car>();
		insideCleaningWaitingQueue 		= new LinkedBlockingQueue<Car>();
		insideCleaningWaitingQueueMutex	= new Object();
		
		fClosed = false;
		
		createAutoCleaningTeam(secondsPerAutoClean);
		createInsideCleaningTeams(numOfTeams);
	}
	
	public void createAutoCleaningTeam(int secondsPerAutoClean) {
		autoClean = new AutoClean(secondsPerAutoClean);
		
	}
	
	public void createInsideCleaningTeams(int numOfTeams) {
		numOfInsideTeams = numOfTeams;
		for (int i = 0; i < numOfInsideTeams; i++) {
			insideCleanArr.add(new InsideClean());
		}
	}
	
	public void cleaning(Car car) {
		// Send the car to the auto clean and then to the inside cleaning
		try {
			cars.put(car);
		}
		catch (InterruptedException e) {
			
		}
	}

	public void closeCleaningService() {
		fClosed = true;
	}
	
	/*
	 * start threads
	 */
	private void startServiceThreads() {
		autoCleanThread = new Thread(autoClean);
		autoCleanThread.start();
		
		// Create inside teams threads
		insideCleanThreads = new ArrayList<Thread>();
		
		for (InsideClean i : insideCleanArr) {
			startServiceSingleThread(i);
		}
	}
	
	private void startServiceSingleThread(InsideClean insideClean){
		Thread t = new Thread(insideClean);
		insideCleanThreads.add(t);
		t.start();
	}
	
	/*
	 * start threads
	 */
	private void stopServiceThreads() {
		// Send the close command to the auto cleaning team
		autoClean.closeService();
		
		// Wait for the thread to finish
		try {
			autoCleanThread.join();
		}
		catch (InterruptedException e) {
			
		}
		
		// Send close commands to the inside cleaning teams
		for(InsideClean i : insideCleanArr) {
			i.closeService();
		}

		// Wait for the inside cleaning teams threads
		try {
			for (Thread t : insideCleanThreads) {
				t.join();
			}
		}
		catch(InterruptedException e) {
			
		}
	}
	
	// Get the next car
	private Car getNextCar() {
		
		Car nextCar = null;
		
		// Try to get the next car from the queue or wait for it
		try {
			nextCar = cars.poll(WATING_QUEUE_TIMEOUT, TimeUnit.SECONDS);
		}
		catch (InterruptedException e) {
			
		}
		
		if (nextCar != null)
			loggerNextCarExist(nextCar);
		return nextCar;
	}
	
	private void loggerNextCarExist(Car car){	
	}
	
	private void serveAutoClean(Car nextCar) {
		serveAutoCleanWithStatus(nextCar, autoClean.isBusy());	
	}
	
	private void serveAutoCleanWithStatus(Car car, boolean status){
		if (!status){
			autoClean.cleaning(car,this);
		}else{
			autoCleaningWaitingQueue.add(car);
		}
	}
	
	// Get the first team ready for job
	private InsideClean getFirstReadyInsideTeam(int size) {
		
		InsideClean first = null;
		
		for (int i = 0 ; i < size ; i++){
			if (insideCleanArr.get(i).isBusy() == false) {
				first = insideCleanArr.get(i);
			}
		}
		
		return first;
		
	}
	
	// serve inside cleaning
	private void serveInsideClean() {
		
		boolean fTeamsFull = false;
		
		// Check whether there are cars waiting for inside cleaning
		synchronized (insideCleaningWaitingQueueMutex) {
			
			while (insideCleaningWaitingQueue.size() > 0 && fTeamsFull == false) {
				InsideClean team = getFirstReadyInsideTeam(insideCleaningWaitingQueue.size());
				if (team != null) {
					Car car = insideCleaningWaitingQueue.poll();
					sendCarToTeam(car,team);
				}
				else {
					fTeamsFull = true;
					loggerTeamsAreBusy();
				}
				
			}
			
		}
	}
	
	
	private void loggerTeamsAreBusy() {
	}

	private void sendCarToTeam(Car car, InsideClean team) {
		team.cleaning(car, this);
	}

	@Override
	public void autoCleanIsDone(Car car) {
		// Handle the auto clean is done event by adding antoher to the auto clean service
		// Send the car to inside cleaning
		insideCleaningWaitingQueue.add(car);
		
		// Get the next car if exists
		Car next = autoCleaningWaitingQueue.poll();
		
		if (next != null) {
			assignCarToAutoClean(next);
		}
	}
	
	private void assignCarToAutoClean(Car car) {
		autoClean.cleaning(car, this);	
	}

	@Override
	public void insideCleanIsDone(Car car) {

		// Handle the inside clean is done event by marking the car cleaning as finished
		synchronized (numOfCarsServedMutex) {
			numOfCarsServed++;
		}
		
		// Mark as finished
		car.finishCleaning();
		
		// Allow more cars to be handled
		serveInsideClean();
	}
	
	// Cleaning service thread entry point
	@Override
	public void run() {
		// Start service threads
		startServiceThreads();
		
		// Main loop
		while (fClosed == false) {
			
			// Give inside clean to the cars that are waiting for it
			serveInsideClean();
			
			Car nextCar = getNextCar();
			
			// Serve the next car if exits
			if (nextCar != null) {
				serveAutoClean(nextCar);
			}
		}
		
		// Stop service threads
		stopServiceThreads();
	}
	
	// Current number of cars served
	public int getNumOfCarsServed() {
		int num;
		
		synchronized (numOfCarsServedMutex) {
			num = this.numOfCarsServed;
		}
		
		return num;
	}
	
	// Total profit calculation
	public float getTotalProfit() {
		return (getNumOfCarsServed() * price);
	}
}
