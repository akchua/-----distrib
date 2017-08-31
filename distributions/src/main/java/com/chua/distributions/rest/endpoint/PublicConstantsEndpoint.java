package com.chua.distributions.rest.endpoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.distributions.rest.handler.PublicConstantsHandler;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Jul 4, 2017
 */
@Path("/publicconstants")
public class PublicConstantsEndpoint {
	
	@Autowired
	private PublicConstantsHandler publicConstantsHandler;
	
	@GET
	@Path("/businessofficialemail")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getBusinessOfficialEmail() {
		return publicConstantsHandler.getBusinessOfficialEmail();
	}
	
	@GET
	@Path("/businessprimarycontact")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getBusinessPrimaryContactNumber() {
		return publicConstantsHandler.getBusinessPrimaryContactNumber();
	}
	
	@GET
	@Path("/systemversion")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getSystemVersion() {
		return publicConstantsHandler.getSystemVersion();
	}
}
