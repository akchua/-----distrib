package com.chua.distributions.database.prototype;

import com.chua.distributions.database.entity.Company;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 3, 2016
 */
public interface CompanyPrototype extends Prototype<Company, Long> {

	Company findByName(String name);
}
