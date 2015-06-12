package aspects;

import gasstation.GasStationUtility;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import bl.AutoClean;
import bl.Car;
import bl.CleaningService;
import bl.InsideClean;

public aspect CleaningServiceLogger {
	private Logger logger;
	private CleaningService theCleaningService;

	pointcut instantiate() : execution (bl.CleaningService.new(..));

	before() : instantiate(){
		theCleaningService = (CleaningService) thisJoinPoint.getThis();
		logger = GasStationUtility.getCleaningServiceLog(theCleaningService);

		logger.log(Level.INFO, "In CleaningService::CleaningService()",
				theCleaningService);
		logger.log(Level.INFO, "CleaningService init", theCleaningService);
	}

	pointcut createAutoCleaningTeam() : execution (public void createAutoCleaningTeam(..));

	before() : createAutoCleaningTeam(){
		logger.log(Level.INFO, "In CleaningService::createAutoCleaningTeam()",
				theCleaningService);
	}

	pointcut createInsideCleaningTeams(int numOfTeams) : (execution (public void createInsideCleaningTeams(int)) && args(numOfTeams));

	before(int numOfTeams) : createInsideCleaningTeams(numOfTeams){
		logger.log(Level.INFO, "In CleaningService::createAutoCleaningTeam()",
				theCleaningService);
	}

	after(int numOfTeams) : createInsideCleaningTeams(numOfTeams){
		logger.log(Level.INFO,
				"In CleaningService::createAutoCleaningTeam() - created "
						+ numOfTeams + " inside cleaning teams",
				theCleaningService);
	}

	pointcut cleaning(Car car) : (execution (public void cleaning(Car)) && args(car));

	before(Car car) : cleaning(car){
		logger.log(Level.INFO, "In CleaningService::cleaning()",
				theCleaningService);
		logger.log(Level.INFO,
				"In CleaningService::cleaning() - car " + car.getId()
						+ " wants to enter", theCleaningService);
	}

	after(Car car) : cleaning(car){
		logger.log(Level.INFO,
				"In CleaningService::cleaning() - car " + car.getId()
						+ " is in queue", theCleaningService);
	}

	pointcut closeCleaningService() : execution (public void closeCleaningService());

	before() : closeCleaningService(){
		logger.log(Level.INFO, "In CleaningService::closeCleaningService()",
				theCleaningService);
	}

	pointcut startServiceThreads() : execution (private void startServiceThreads());

	before() : startServiceThreads(){
		logger.log(Level.INFO, "In CleaningService::run()", theCleaningService);
		logger.log(
				Level.INFO,
				"In CleaningService::run() - started running as a seperate thread",
				theCleaningService);
		logger.log(Level.INFO, "In CleaningService::startServiceThreads()",
				theCleaningService);
		logger.log(
				Level.INFO,
				"In CleaningService::startServiceThreads() - starting auto clean thread",
				theCleaningService);
	}

	pointcut startServiceSingleThread(InsideClean insideClean) : (execution (private void startServiceSingleThread(InsideClean)) && args(insideClean));

	before(InsideClean insideClean) : startServiceSingleThread(insideClean){
		logger.log(
				Level.INFO,
				"In CleaningService()::startServiceThreads() - creating thread for inside team: "
						+ insideClean.getId(), theCleaningService);
	}

	after(InsideClean insideClean) : startServiceSingleThread(insideClean){
		logger.log(
				Level.INFO,
				"In CleaningService()::startServiceThreads() - created thread for inside team: "
						+ insideClean.getId(), theCleaningService);
	}

	pointcut stopServiceThreads() : execution (private void stopServiceThreads());

	before() : stopServiceThreads(){
		logger.log(Level.INFO, "In CleaningService::stopServiceThreads()",
				theCleaningService);
	}

	after() : stopServiceThreads(){
		logger.log(
				Level.INFO,
				"In CleaningService::stopServiceThreads() - service threads finished",
				theCleaningService);
		logger.log(Level.INFO,
				"In CleaningService::run() - Cleaning service is now closed",
				theCleaningService);
	}

	pointcut loggerNextCarExist(Car car) : (execution (private void loggerNextCarExist(Car)) && args(car));

	after(Car car) : loggerNextCarExist(car){
		logger.log(Level.INFO,
				"In CleaningService::getNextCar() - car " + car.getId()
						+ " is ready to be served", theCleaningService);
	}

	pointcut serveAutoCleanWithStatus(Car car, boolean status) : (execution (private void serveAutoCleanWithStatus(Car, boolean)) && args(car, status));

	after(Car car, boolean status) : serveAutoCleanWithStatus(car, status){
		if (status) {
			logger.log(Level.INFO, "In CleaningService::run() - sending car "
					+ car.getId() + " to auto cleaning", theCleaningService);
		} else {
			logger.log(Level.INFO, "In CleaningService::run() - sending car "
					+ car.getId() + " to auto cleaning queue",
					theCleaningService);
		}
	}

	pointcut sendCarToTeam(Car car, InsideClean team) : (execution (private void sendCarToTeam(Car, InsideClean)) && args(car, team));

	after(Car car, InsideClean team) : sendCarToTeam(car, team){
	}

	pointcut loggerTeamsAreBusy(): execution (private void loggerTeamsAreBusy());

	after() : loggerTeamsAreBusy(){
		logger.log(
				Level.INFO,
				"In CleaningService::serveInsideClean() - all inside cleaning teams are busy",
				theCleaningService);
	}

	pointcut autoCleanIsDone(Car car) : (execution (public void autoCleanIsDone(Car)) && args(car));

	before(Car car) : autoCleanIsDone(car){
		logger.log(Level.INFO, "In CleaningService::autoCleanIsDone()",
				theCleaningService);
		logger.log(Level.INFO, "In CleaningService::autoCleanIsDone() - car "
				+ car.getId() + " finished the auto clean", theCleaningService);
	}

	after(Car car) : autoCleanIsDone(car){
		logger.log(Level.INFO, "In CleaningService::run() - car " + car.getId()
				+ " is added to inside cleaning queue", theCleaningService);
	}

	pointcut assignCarToAutoClean(Car car) : (execution (private void assignCarToAutoClean(Car)) && args(car));

	after(Car car) : assignCarToAutoClean(car){
		logger.log(Level.INFO,
				"In CleaningService::run() - sending car " + car.getId()
						+ " to auto cleaning", theCleaningService);
	}

	pointcut insideCleanIsDone(Car car) : (execution (private void insideCleanIsDone(Car)) && args(car));

	after(Car car) : insideCleanIsDone(car){
		logger.log(Level.INFO, "In CleaningService::insideCleanIsDone()",
				theCleaningService);
		logger.log(Level.INFO, "In CleaningService::insideCleanIsDone() - car "
				+ car.getId() + " finished the inside clean",
				theCleaningService);
	}
}
