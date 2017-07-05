package com.chua.distributions.rest.endpoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.chua.distributions.constants.BusinessConstants;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Jul 4, 2017
 */
@Path("/constants")
public class ConstantsEndpoint {

	@GET
	@Path("/businessofficialemail")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getBusinessOfficialEmail() {
		return BusinessConstants.BUSINESS_OFFICIAL_EMAIL;
	}
	
	@GET
	@Path("/businessprimarycontact")
	@Produces({ MediaType.TEXT_PLAIN })
	public String getBusinessPrimaryContactNumber() {
		return BusinessConstants.BUSINESS_PRIMARY_CONTACT_NUMBER;
	}
}
