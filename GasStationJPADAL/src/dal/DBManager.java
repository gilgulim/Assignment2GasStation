package dal;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dalJPA.GasStationJPAManager;
import dalMySQL.GasStationMySQLManager;
import dataObjects.CarObject;

public class DBManager {
	public static void main(String[] args){
		ApplicationContext theContext = new ClassPathXmlApplicationContext("DBConfiguration.xml");
		
//		GasStationManagerAbstract asd = (GasStationMySQLManager)theContext.getBean("MySQL");
//		asd.insertCar(new CarObject(123, true, true, 555));		
	}
	public DBManager(){
		

		
	}
}
