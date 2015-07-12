package dalJPA;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import dal.dataObjects.*;
import dal.dataObjects.GasStationHistoryRecord.ActionType;
import dal.*;

public class GasStationJPAManager implements IDataBaseConnection {

	public boolean insertCar(CarObject carObject) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("GasStationJPADAL");
		EntityManager em = emf.createEntityManager();

		try {
			Car car = new Car();

			car.setCarID(carObject.getId());
			car.setCarFuelQuantity((carObject.getNumOfLiters() != 0 ? carObject
					.getNumOfLiters() : null));
			car.setIsCarReqFuel((carObject.getWantFuel() ? (byte) 1 : 0));
			car.setIsCarReqWash((carObject.getWantCleaning() ? (byte) 1 : 0));

			em.getTransaction().begin();
			em.persist(car);
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return false;
		} finally {
			em.close();
		}
	}

	@Override
	public boolean clearDatabase() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("GasStationJPADAL");
		EntityManager em = emf.createEntityManager();

		try {
			em.createQuery("DELETE FROM gasstationpumprelationship")
					.executeUpdate();
			em.createQuery("DELETE FROM gasstations").executeUpdate();
			em.createQuery("DELETE FROM mainfuelpool").executeUpdate();
			em.createQuery("DELETE FROM cleanservices").executeUpdate();
			em.createQuery("DELETE FROM gasstationhistorylog").executeUpdate();
			em.createQuery("DELETE FROM cars").executeUpdate();
			em.createQuery("DELETE FROM pumps").executeUpdate();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	@Override
	public boolean insertGasStationHistoryRecord(
			GasStationHistoryRecord historyRecord) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("GasStationJPADAL");
		EntityManager em = emf.createEntityManager();

		try {
			Car car = new Car();
			car.setCarID(historyRecord.getCarId());

			Gasstationhistorylog gasStationHistoryLog = new Gasstationhistorylog();
			gasStationHistoryLog.setCar(car);
			gasStationHistoryLog.setActionTypeID(historyRecord.getActionType()
					.ordinal());
			gasStationHistoryLog.setServiceEntityID(historyRecord
					.getServiceEntityId());
			gasStationHistoryLog.setServiceEntityTypeID(historyRecord
					.getServiceEntityType().ordinal());
			gasStationHistoryLog.setCreationTime(Date.valueOf(historyRecord
					.getDateTime()));

			em.getTransaction().begin();
			em.persist(gasStationHistoryLog);
			em.getTransaction().commit();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return false;
		} finally {
			em.close();
		}
	}

	@Override
	public boolean insertGasStation(GasStationObject gasStationObject) {

		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("GasStationJPADAL");
		EntityManager em = emf.createEntityManager();

		try {
			Cleanservice cleanService = new Cleanservice();
			cleanService.setCleanServiceID(gasStationObject
					.getCleaningServiceObject().getId());
			cleanService.setCleanServicePrice(BigDecimal
					.valueOf(gasStationObject.getCleaningServiceObject()
							.getPrice()));
			cleanService.setNumOfInsideTeams(gasStationObject
					.getCleaningServiceObject().getNumOfInsideTeams());

			Mainfuelpool mainFuelPool = new Mainfuelpool();
			mainFuelPool.setMainFuelPoolID(gasStationObject.getFuelPoolObject()
					.getId());
			mainFuelPool.setMaxCapacity(gasStationObject.getFuelPoolObject()
					.getMaxCapacity());

			Gasstation gasStation = new Gasstation();
			gasStation.setGasStationID(gasStationObject.getId());
			gasStation.setCleanservice(cleanService);
			gasStation.setMainfuelpool(mainFuelPool);
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return false;
		} finally {
			em.close();
		}
	}

	@Override
	public GasStationObject getGasStationById(int gasStationId) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("GasStationJPADAL");
		EntityManager em = emf.createEntityManager();

		try {
			Gasstation gasStation = em.find(Gasstation.class, gasStationId);
			GasStationObject gasStationObject = new GasStationObject(gasStationId);

			//TODO: Check why there is 0 in the current capacity 
			FuelPoolObject fuelPoolObject = new FuelPoolObject(gasStation.getMainfuelpool().getMainFuelPoolID(),
					gasStation.getMainfuelpool().getMaxCapacity(), 0);
			
			//fuelPoolObject.setId(gasStation.getMainfuelpool().getMainFuelPoolID());
			//fuelPoolObject.setMaxCapacity(gasStation.getMainfuelpool().getMaxCapacity());

			CleaningServiceObject cleaningServiceObject = new CleaningServiceObject(
												gasStation.getCleanservice().getCleanServiceID(),
												gasStation.getCleanservice().getNumOfInsideTeams(),
												Integer.valueOf(gasStation.getCleanservice().getCleanServicePrice().toString()),
												200);
			//cleaningServiceObject.setId(gasStation.getCleanservice().getCleanServiceID());
			//cleaningServiceObject.setNumOfInsideTeams(gasStation.getCleanservice().getNumOfInsideTeams());
			//cleaningServiceObject.setPrice(Float.valueOf(gasStation.getCleanservice().getCleanServicePrice().toString()));

			gasStationObject.setFuelPoolObject(fuelPoolObject);
			gasStationObject.setCleaningServiceObject(cleaningServiceObject);

			return gasStationObject;

		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return null;
		} finally {
			em.close();
		}
	}

	@Override
	public int getLittersByCarId(int carId) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("GasStationJPADAL");
		EntityManager em = emf.createEntityManager();

		try {
			String sql = String.format(
					"SELECT SUM(*) FROM cars WHERE CarID = %d", carId);
			Query q = em.createQuery(sql);
			return (int) q.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			em.close();
		}
	}

	@Override
	public int getPricePerLittersByPumpId(int pumpId) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("GasStationJPADAL");
		EntityManager em = emf.createEntityManager();

		try {
			String sql = String.format("SELECT * FROM pumps WHERE PumpID = %d",
					pumpId);
			Query q = em.createQuery(sql);
			return (int) q.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			em.close();
		}
	}

	@Override
	public int getNumberOfPumps() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("GasStationJPADAL");
		EntityManager em = emf.createEntityManager();

		try {
			String sql = "SELECT COUNT(pumpID) FROM pumps";
			Query q = em.createQuery(sql);
			return (int) q.getSingleResult();

		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return -1;
		} finally {
			em.close();
		}
	}

	@Override
	public ArrayList<GasStationHistoryRecord> getStatistics(
			ActionType actionType, int serviceId) {

		ArrayList<GasStationHistoryRecord> records = new ArrayList<GasStationHistoryRecord>();
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("GasStationJPADAL");
		EntityManager em = emf.createEntityManager();

		try {

			String sql = String
					.format("SELECT * FROM gasstationhistorylog WHERE ActionTypeID = %s",
							actionType.ordinal());
			if (actionType == ActionType.Fuel) {
				sql += String.format(" AND ServiceEntityID = %d", serviceId);
				Query q = em.createQuery(sql);

				List<Gasstationhistorylog> resulList = q.getResultList();
				for (Gasstationhistorylog result : resulList) {
					GasStationHistoryRecord historyRecord = new GasStationHistoryRecord(
							result.getCar().getCarID(),
							GasStationHistoryRecord.ActionType.values()[result.getActionTypeID()],
							GasStationHistoryRecord.ServiceEntityType.values()[result.getServiceEntityTypeID()],
							result.getServiceEntityID(),
							result.getCreationTime().toString());
					
					records.add(historyRecord);
				}
			}
			return records;
		} catch (Exception e) {
			e.printStackTrace();
			em.getTransaction().rollback();
			return null;
		} finally {
			em.close();
		}
	}



}
