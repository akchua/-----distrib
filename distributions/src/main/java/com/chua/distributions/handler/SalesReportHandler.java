package com.chua.distributions.handler;

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.beans.SalesReportQueryBean;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	19 Jan 2017
 */
public interface SalesReportHandler {

	ResultBean generateReport(SalesReportQueryBean salesReportQuery, String recipient);
}
