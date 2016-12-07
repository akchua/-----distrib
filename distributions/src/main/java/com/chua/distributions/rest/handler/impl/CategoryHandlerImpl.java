package com.chua.distributions.rest.handler.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.chua.distributions.UserContextHolder;
import com.chua.distributions.beans.CategoryFormBean;
import com.chua.distributions.beans.ResultBean;
import com.chua.distributions.database.entity.Category;
import com.chua.distributions.database.service.CategoryService;
import com.chua.distributions.enums.Color;
import com.chua.distributions.objects.ObjectList;
import com.chua.distributions.rest.handler.CategoryHandler;
import com.chua.distributions.utility.Html;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 4, 2016
 */
@Transactional
@Component
public class CategoryHandlerImpl implements CategoryHandler {

	@Autowired
	private CategoryService categoryService;

	@Override
	public Category getCategory(Long categoryId) {
		return categoryService.find(categoryId);
	}

	@Override
	public ObjectList<Category> getCategoryObjectList(Integer pageNumber, String searchKey) {
		return categoryService.findAllWithPagingOrderByName(pageNumber, UserContextHolder.getItemsPerPage(), searchKey);
	}
	
	@Override
	public List<Category> getCategoryList() {
		return categoryService.findAllOrderByName();
	}

	@Override
	public ResultBean createCategory(CategoryFormBean categoryForm) {
		final ResultBean result;
		final ResultBean validateForm = validateCategoryForm(categoryForm);
		
		if(validateForm.getSuccess()) {
			if(categoryService.isExistsByName(categoryForm.getName().trim())) {
				result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Category name already exists.")));
			} else {
				final Category category = new Category();
				
				setCategory(category, categoryForm);
				
				result = new ResultBean();
				result.setSuccess(categoryService.insert(category) != null);
				if(result.getSuccess()) {
					result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " created Category " + Html.text(Color.BLUE, category.getName()) + "."));
				} else {
					result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
				}
			}
		} else {
			result = validateForm;
		}
		
		return result;
	}

	@Override
	public ResultBean updateCategory(CategoryFormBean categoryForm) {
		final ResultBean result;
		final Category category = categoryService.find(categoryForm.getId());
		
		if(category != null) {
			final ResultBean validateForm = validateCategoryForm(categoryForm);
			if(validateForm.getSuccess()) {
				final Category categoryy = categoryService.findByName(categoryForm.getName().trim());
				if(categoryy != null && category.getId() != categoryy.getId()) {
					result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Category name already exists.")));
				} else {
					setCategory(category, categoryForm);
					
					result = new ResultBean();
					result.setSuccess(categoryService.update(category));
					if(result.getSuccess()) {
						result.setMessage(Html.line("Category " + Html.text(Color.BLUE, category.getName()) + " has been successfully " + Html.text(Color.GREEN, "updated") + "."));
					} else {
						result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
					}
				}
			} else {
				result = validateForm;
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load category. Please refresh the page."));
		}
		
		return result;
	}

	@Override
	public ResultBean removeCategory(Long categoryId) {
		final ResultBean result;
		final Category category = categoryService.find(categoryId);
		
		if(category != null) {
			result = new ResultBean();
			
			result.setSuccess(categoryService.delete(category));
			if(result.getSuccess()) {
				result.setMessage(Html.line(Html.text(Color.GREEN, "Successfully") + " removed Category " + Html.text(Color.BLUE, category.getName()) + "."));
			} else {
				result.setMessage(Html.line(Html.text(Color.RED, "Server Error.") + " Please try again later."));
			}
		} else {
			result = new ResultBean(Boolean.FALSE, Html.line(Html.text(Color.RED, "Failed") + " to load category. Please refresh the page."));
		}
		
		return result;
	}
	
	private void setCategory(Category category, CategoryFormBean categoryForm) {
		category.setName(categoryForm.getName());
	}
	
	private ResultBean validateCategoryForm(CategoryFormBean categoryForm) {
		final ResultBean result;
		
		if(categoryForm.getName() == null || categoryForm.getName().trim().length() < 3) {
			result = new ResultBean(Boolean.FALSE, Html.line("All fields are " + Html.text(Color.RED, "required") + " and must contain at least 3 characters."));
		} else {
			result = new ResultBean(Boolean.TRUE, "");
		}
		
		return result;
	}
}
