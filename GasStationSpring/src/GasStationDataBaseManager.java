import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import dal.IDataBaseConnection;
import dalJPA.GasStationJPAManager;
import dalODBC.GasStationMySqlConnection;





public class GasStationDataBaseManager {
	
	private static IDataBaseConnection instance;
	
	
	public static IDataBaseConnection getInstance(){
		if(instance == null){
			ApplicationContext theContext = new ClassPathXmlApplicationContext("Settings.xml");

			//XXX : not woriking currently
			instance = (IDataBaseConnection)theContext.getBean("ODBC");
			//instance = (GasStationJPAManager)theContext.getBean("JPA");
			
			instance.clearDatabase();
			
		}
		return instance;
	}
	
}
