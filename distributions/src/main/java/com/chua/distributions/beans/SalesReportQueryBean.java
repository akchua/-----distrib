package com.chua.distributions.beans;

import com.chua.distributions.enums.Area;
import com.chua.distributions.enums.ClientSalesReportType;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 30, 2016
 */
public class SalesReportQueryBean extends ReportQueryBean {

	private Long warehouseId;
	
	private Long clientId;
	
	private Long companyId;
	
	private Area area;
	
	private ClientSalesReportType clientSalesReportType;
	
	private Boolean includePaid;
	
	private Boolean includeDelivered;
	
	private Boolean includeDispatched;
	
	private Boolean includeToFollow;
	
	private Boolean includeAccepted;
	
	private Boolean includeSubmitted;
	
	private Boolean includeCreating;
	
	private Boolean sendMail;
	
	private Boolean downloadFile;

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public ClientSalesReportType getClientSalesReportType() {
		return clientSalesReportType;
	}

	public void setClientSalesReportType(ClientSalesReportType clientSalesReportType) {
		this.clientSalesReportType = clientSalesReportType;
	}

	public Boolean getIncludePaid() {
		return includePaid;
	}

	public void setIncludePaid(Boolean includePaid) {
		this.includePaid = includePaid;
	}

	public Boolean getIncludeDelivered() {
		return includeDelivered;
	}

	public void setIncludeDelivered(Boolean includeDelivered) {
		this.includeDelivered = includeDelivered;
	}

	public Boolean getIncludeDispatched() {
		return includeDispatched;
	}

	public void setIncludeDispatched(Boolean includeDispatched) {
		this.includeDispatched = includeDispatched;
	}

	public Boolean getIncludeToFollow() {
		return includeToFollow;
	}

	public void setIncludeToFollow(Boolean includeToFollow) {
		this.includeToFollow = includeToFollow;
	}

	public Boolean getIncludeAccepted() {
		return includeAccepted;
	}

	public void setIncludeAccepted(Boolean includeAccepted) {
		this.includeAccepted = includeAccepted;
	}

	public Boolean getIncludeSubmitted() {
		return includeSubmitted;
	}

	public void setIncludeSubmitted(Boolean includeSubmitted) {
		this.includeSubmitted = includeSubmitted;
	}

	public Boolean getIncludeCreating() {
		return includeCreating;
	}

	public void setIncludeCreating(Boolean includeCreating) {
		this.includeCreating = includeCreating;
	}

	public Boolean getSendMail() {
		return sendMail;
	}

	public void setSendMail(Boolean sendMail) {
		this.sendMail = sendMail;
	}

	public Boolean getDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(Boolean downloadFile) {
		this.downloadFile = downloadFile;
	}
}
