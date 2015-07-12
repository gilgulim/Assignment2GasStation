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
//http://localhost:8080/GasStationServlet/AddCar.html
public class GasStationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private TcpClient tcpClient;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GasStationServlet() {
        super();
        //Connect to main  gas station server
        String localIpAddress = "";
        
        try {
        	localIpAddress = Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {}
        
        tcpClient = new TcpClient(localIpAddress, 3456);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    public void doGet(HttpServletRequest request, 
            HttpServletResponse response) 
        throws ServletException, IOException {
    	 
    	PrintWriter out = response.getWriter();
    	
        if(tcpClient.connect()){
        	 
         	String carId = request.getParameter("carId").trim();
         	String gasAmount = request.getParameter("gasAmount").trim();
         	String actionType = request.getParameter("actionType").trim();
         	
         	CarObject carObject=null;
         	actionType = actionType.toLowerCase();
         	
         	try{
	         	if(actionType.contains("fuel")&& actionType.contains("wash")){
	         		
	         		carObject = new CarObject(Integer.parseInt(carId), true, true, Integer.parseInt(gasAmount));
	         		
	         	}else if(actionType.contains("wash")){
	         		carObject = new CarObject(Integer.parseInt(carId), true, false, 0);
	         		
	         	}else if(actionType.contains("fuel")){
	         		carObject = new CarObject(Integer.parseInt(carId), false, true, Integer.parseInt(gasAmount));
	         	}
         	}catch(Exception ex){
         		
         	}
         	
         	if(carObject!=null){
         		AddCarPacket addCarPacket = new AddCarPacket(carObject);
                tcpClient.sendData(addCarPacket.serialize());
                out.println("Sending car's data to server...");
         	}else{
         		out.println("An error accured while parsing cars data");
         	}
         	
         	//tcpClient.close();
             
        }else{
            out.println("An error accured while connecting to the GasStation tcp server.");
        }
       
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
