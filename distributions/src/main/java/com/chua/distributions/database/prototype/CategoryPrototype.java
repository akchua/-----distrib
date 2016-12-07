package com.chua.distributions.database.prototype;

import com.chua.distributions.database.entity.Category;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 4, 2016
 */
public interface CategoryPrototype extends Prototype<Category, Long> {

	Category findByName(String name);
}
