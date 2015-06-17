import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dalJPA.GasStationJPAManager;
import dalODBC.GasStationMySqlConnection;





public class GasStationDataBaseManager {
	
	private static IDataBaseConnection instance;
	
	
	public static IDataBaseConnection getInstance(){
		if(instance == null){
			ApplicationContext theContext = new ClassPathXmlApplicationContext("DBConnection.xml");

			//XXX : not woriking currently
			//instance = (GasStationMySqlConnection)theContext.getBean("ODBC");
			//instance = (GasStationJPAManager)theContext.getBean("JPA");
		}
		return instance;
	}
	
}
