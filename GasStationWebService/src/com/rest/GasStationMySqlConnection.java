package com.rest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class GasStationMySqlConnection {

	private final String DB_URL = "jdbc:mysql://cloudscan.noip.me/gasstation";
	private Connection connection = null;
	private boolean isConnected = false;

	public GasStationMySqlConnection() {
		connect();
	}

	private void connect() {

		if (!isConnected) {

			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();

				connection = DriverManager
						.getConnection(DB_URL, "root", "1234");
				System.out.println("Database connection established");
				isConnected = true;

			} catch (Exception ex) {

				isConnected = false;
			}
		}

	}

	public void disconnect() {
		try {
			connection.close();
		} catch (Exception ex) {

		}
	}


	public int getCarPaymentByCarId(int carId) {
		try {
			Statement statement = connection.createStatement();
			ResultSet rs = statement
					.executeQuery(String
							.format("SELECT "
									+ "CarID, (PumpPricePerLiter * CarFuelQuantity) + CleanServicePrice as total "
									+ "FROM cars "
									+ "JOIN pumps on pumps.pumpID = cars.carPumpID "
									+ "JOIN cleanservices "
									+ "WHERE CarID = %d",
									carId));

			while (rs.next()) {
				return rs.getInt("total");
			}

		} catch (Exception ex) {
			return -1;
		}

		return -1;
	}
}
