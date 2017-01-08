package com.chua.distributions.beans;

import java.util.Calendar;
import java.util.Date;

import com.chua.distributions.enums.Warehouse;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 30, 2016
 */
public class SalesReportQueryBean {

	private Date from;
	
	private Date to;
	
	private Warehouse warehouse;
	
	private Long clientId;
	
	private Boolean includePaid;
	
	private Boolean includeDelivered;
	
	private Boolean includeDispatched;
	
	private Boolean includeAccepted;
	
	private Boolean includeSubmitted;
	
	private Boolean includeCreating;
	
	private Boolean showNetTrail;
	
	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	/**
	 * Inclusive end date.
	 * @param to The end date
	 */
	public void setTo(Date to) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(to);
		calendar.add(Calendar.HOUR_OF_DAY, 23);
		calendar.add(Calendar.MINUTE, 59);
		calendar.add(Calendar.SECOND, 59);
		this.to= calendar.getTime();
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Long getClientId() {
		return clientId;
	}

	public void setClientId(Long clientId) {
		this.clientId = clientId;
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
