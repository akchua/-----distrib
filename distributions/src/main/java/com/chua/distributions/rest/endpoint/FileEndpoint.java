package com.chua.distributions.rest.endpoint;

import java.io.File;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.chua.distributions.rest.handler.FileHandler;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	9 Feb 2017
 */
@Path("/file")
public class FileEndpoint {

	@Autowired
	private FileHandler fileHandler;
	
	@GET
	@Path("/salesreport/{fileName}")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response getSalesReportByFileName(@PathParam("fileName") String fileName) throws IOException {
		File salesReport = fileHandler.findSalesReportByFileName(fileName);
		if(salesReport.exists())
			return Response.ok(salesReport, MediaType.APPLICATION_OCTET_STREAM)
				.header("Content-Disposition", "attachment; filename=\"" + salesReport.getName() + "\"" )
				.build();
		else return null;
	}
}
