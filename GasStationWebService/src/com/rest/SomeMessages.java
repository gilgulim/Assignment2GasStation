package com.rest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/SomeMessages")
public class SomeMessages {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getPlainMessage(){
		return "Hello RestWEBService!";
	}
	
}
