package com.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


// URL path: http://localhost:8080/GasStationWebService/rest/CarStatistics/<carID>

@Path("/CarStatistics/{carId}")
public class CarStatistics {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getPlainMessage(@PathParam("carId") String carId) {
		int carTotalPayment = getCarData(Integer.valueOf(carId));
		String carResult;
		if (carTotalPayment != -1){
			carResult = "Car ID is: " + carId + ", Total Payment - " + carTotalPayment;
		}else{
			carResult = "Car doesn't exists";
		}
		String htmlString = ""
		+ "<html>"
		+ "<head></head>"
		+ "<body>"
		+ "<H2>"
		+ carResult
		+ "</H2>"
		+ "</body>"
		+ "</html>";
		return htmlString;
		
	}

	private int getCarData(int carId) {
		GasStationMySqlConnection gasStationDB = new GasStationMySqlConnection();
		int data = gasStationDB.getCarPaymentByCarId(carId);
		gasStationDB.disconnect();
		
		return data;
		
 	}
	
}
