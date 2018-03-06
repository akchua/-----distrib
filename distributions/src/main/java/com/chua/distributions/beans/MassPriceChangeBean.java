package com.chua.distributions.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Mar 5, 2018
 */
public class MassPriceChangeBean {

	Long companyId;
	
	String includeString;
	
	String excludeString;
	
	Float percentGrossIncrease;
	
	Float percentSellingIncrease;

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public String getIncludeString() {
		return includeString;
	}

	public void setIncludeString(String includeString) {
		this.includeString = includeString;
	}
	
	public String getExcludeString() {
		return excludeString;
	}

	public void setExcludeString(String excludeString) {
		this.excludeString = excludeString;
	}

	public Float getPercentGrossIncrease() {
		return percentGrossIncrease;
	}

	public void setPercentGrossIncrease(Float percentGrossIncrease) {
		this.percentGrossIncrease = percentGrossIncrease;
	}

	public Float getPercentSellingIncrease() {
		return percentSellingIncrease;
	}

	public void setPercentSellingIncrease(Float percentSellingIncrease) {
		this.percentSellingIncrease = percentSellingIncrease;
	}
}
