package com.chua.distributions;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import com.chua.distributions.rest.endpoint.CategoryEndpoint;
import com.chua.distributions.rest.endpoint.CompanyEndpoint;
import com.chua.distributions.rest.endpoint.ProductEndpoint;
import com.chua.distributions.rest.endpoint.PurchaseOrderEndpoint;
import com.chua.distributions.rest.endpoint.SecurityEndpoint;
import com.chua.distributions.rest.endpoint.UserEndpoint;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 13, 2016
 */
@Component
@ApplicationPath("services")
public class JerseyConfig extends ResourceConfig {

	public JerseyConfig() {
		
		register(SecurityEndpoint.class);
		register(UserEndpoint.class);
		register(CategoryEndpoint.class);
		register(CompanyEndpoint.class);
		register(ProductEndpoint.class);
		register(PurchaseOrderEndpoint.class);
	}
}
