package com.chua.distributions.constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 11, 2016
 */
@Component
public class BusinessConstants {

	private final String businessName;
	
	private final String businessShortName;
	
	private final String businessChiefOfficer;
	
	private final String businessOfficialEmail;
	
	private final String businessPrimaryContactNumber;
	
	private final String defaultReportReceiver;
	
	@Autowired
	public BusinessConstants(@Value("${business.name}") String businessName,
							@Value("${business.shortName}") String businessShortName,
							@Value("${business.chiefOfficer}") String businessChiefOfficer,
							@Value("${business.officialEmail") String businessOfficialEmail,
							@Value("${business.primaryContactNumber") String businessPrimaryContactNumber,
							@Value("${business.defaultReportReceiver}") String defaultReportReceiver) {
		this.businessName = businessName;
		this.businessShortName = businessShortName;
		this.businessChiefOfficer = businessChiefOfficer;
		this.businessOfficialEmail = businessOfficialEmail;
		this.businessPrimaryContactNumber = businessPrimaryContactNumber;
		this.defaultReportReceiver = defaultReportReceiver;
	}

	public String getBusinessName() {
		return businessName;
	}

	public String getBusinessShortName() {
		return businessShortName;
	}

	public String getBusinessChiefOfficer() {
		return businessChiefOfficer;
	}

	public String getBusinessOfficialEmail() {
		return businessOfficialEmail;
	}

	public String getBusinessPrimaryContactNumber() {
		return businessPrimaryContactNumber;
	}

	public String getDefaultReportReceiver() {
		return defaultReportReceiver;
	}
}
