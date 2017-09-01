package com.chua.distributions.beans;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	23 Jan 2017
 */
public class PurchaseReportQueryBean extends ReportQueryBean {

	private Long warehouseId;
	
	private Long companyId;
	
	private Boolean includePaid;
	
	private Boolean includeDelivered;
	
	private Boolean includeToFollow;
	
	private Boolean includeAccepted;
	
	private Boolean includeSubmitted;
	
	private Boolean includeCreating;
	
	private Boolean showNetTrail;

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
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

	public Boolean getShowNetTrail() {
		return showNetTrail;
	}

	public void setShowNetTrail(Boolean showNetTrail) {
		this.showNetTrail = showNetTrail;
	}
}
