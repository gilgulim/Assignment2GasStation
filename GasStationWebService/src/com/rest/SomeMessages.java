package com.rest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/SomeMessages")
public class SomeMessages {

	@GET
	@Produces(MediaType.TEXT_HTML)
	public String getPlainMessage(@PathParam("carId") String carId) {
		
		
		
		return "<html>"
				+ "<head></head>"
				+ "<body>"
				+ ""
				+ "</body>"
				+ "</html>";
	}
	
}
