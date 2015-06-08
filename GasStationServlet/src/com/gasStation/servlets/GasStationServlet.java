package com.gasStation.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.UnknownHostException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import pl.AddCarPacket;
import cl.TcpClient;
import dal.CarObject;

/**
 * Servlet implementation class GasStationServlet
 */
public class GasStationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private TcpClient tcpClient;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GasStationServlet() {
        super();
        
        String localIpAddress = "";
        
        try {
        	localIpAddress = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {}
        
        tcpClient = new TcpClient(localIpAddress, 3456);
        tcpClient.connect();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request, 
            HttpServletResponse response) 
        throws ServletException, IOException {
    	
        PrintWriter out = response.getWriter();
        out.println("Sending car's data to server...");
        
        CarObject carObject = new CarObject(111, true, true, 20);
        
        AddCarPacket addCarPacket = new AddCarPacket(carObject);
        
        tcpClient.sendData(addCarPacket.serialize());
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
