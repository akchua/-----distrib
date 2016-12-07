package com.chua.distributions.beans;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 20, 2016
 */
public class SettingsFormBean extends FormBean {

	private Integer itemsPerPage;
	
	public SettingsFormBean() {
		
	}
	
	public SettingsFormBean(Integer itemsPerPage) {
		setItemsPerPage(itemsPerPage);
	}

	public Integer getItemsPerPage() {
		return itemsPerPage;
	}

	public void setItemsPerPage(Integer itemsPerPage) {
		this.itemsPerPage = itemsPerPage;
	}
}
