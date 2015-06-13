package dalJPA;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class GasStationJPAManager {

	public static void main(String[] args) {
		Car car = new Car();
		car.setCarID(999);
		car.setIsCarReqFuel((byte)1);
		car.setIsCarReqWash((byte)1);

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("GasStationJPADAL");
		EntityManager em = emf.createEntityManager();
		
		em.getTransaction().begin();
		
		try{
			em.persist(car);
			em.getTransaction().commit();
		}catch (Exception e){
			em.getTransaction().rollback();
		}finally{
			em.close();
		}
	}

}
