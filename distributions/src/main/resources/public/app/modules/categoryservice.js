define(['jquery'], function ($) {
	return {
		getCategory: function(categoryId) {
			return $.ajax({
				url: '/services/category/get',
				data: {
					categoryId: categoryId
				}
			});
		},
		
		getCategoryList: function(currentPage, searchKey) {
			return $.ajax({
				url: '/services/category/list',
				data: {
					pageNumber: currentPage - 1,
					searchKey: searchKey
				}
			});
		},
		
		getCategoryListByName: function() {
			return $.ajax({
				url: '/services/category/listbyname'
			});
		},
		
		saveCategory: function(categoryFormData) {
    		return $.ajax({
    			url: '/services/category/save',
    			method: 'POST',
    			data: {
    				categoryFormData: categoryFormData
    			} 
    		});
    	},
    	
    	removeCategory: function(categoryId) {
    		return $.ajax({
    			url: '/services/category/remove',
    			method: 'POST',
    			data: {
    				categoryId: categoryId
    			}
    		});
    	}
	};
});