package dalJPA;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import dal.GasStationManagerAbstract;
import dataObjects.CarObject;

public class GasStationJPAManager extends GasStationManagerAbstract {

	public boolean insertCar(CarObject carObject){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("GasStationJPADAL");
		EntityManager em = emf.createEntityManager();
		
		try {
			Car car = new Car();
			
			car.setCarID(carObject.getId());
			car.setCarFuelQuantity((carObject.getNumOfLiters() != 0 ? carObject.getNumOfLiters() : null));	
			car.setIsCarReqFuel((carObject.getWantFuel() ? (byte) 1 : 0));
			car.setIsCarReqWash((carObject.getWantCleaning() ? (byte) 1 : 0));

			em.getTransaction().begin();
			em.persist(car);
			em.getTransaction().commit();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			em.getTransaction().rollback();
			return false;
		}finally{
			em.close();
		}
	}
	
	
}
