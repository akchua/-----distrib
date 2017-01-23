package com.chua.distributions.rest.handler;

import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.enums.Warehouse;

/**
 * @author	Adrian Jasper K. Chua
 * @version	1.0
 * @since	19 Jan 2017
 */
public interface WarehouseSyncHandler {

	ResultBean syncExisting(Warehouse warehouse);
}
