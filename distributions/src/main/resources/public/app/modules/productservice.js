define(['jquery'], function ($) {
	return {
		getProduct: function(productId, warehouse) {
			return $.ajax({
				url: '/services/product/get',
				data: {
					productId: productId,
					warehouse: warehouse
				}
			});
		},
		
		getProductImageByFileName: function(fileName) {
			return '/services/product/getimage/' + fileName;
		},
		
		getPartialProduct: function(productId, warehouse) {
			return $.ajax({
				url: '/services/product/getpartial',
				data: {
					productId: productId,
					warehouse: warehouse
				}
			});
		},
		
		getPartialProductFor: function(productId, clientId) {
			return $.ajax({
				url: '/services/product/getpartialfor',
				data: {
					productId: productId,
					clientId: clientId
				}
			});
		},
		
		getProductList: function(currentPage, searchKey, companyId, categoryId, warehouse, async) {
			return $.ajax({
				url: '/services/product/list',
				async: async,
				data: {
					pageNumber: currentPage - 1,
					searchKey: searchKey,
					companyId: companyId,
					categoryId: categoryId,
					warehouse, warehouse
				}
			});
		},
		
		getProductListByName: function() {
			return $.ajax({
				url: '/services/product/listbyname'
			});
		},
		
		getPartialProductList: function(currentPage, searchKey, companyId, categoryId, warehouse, async) {
			return $.ajax({
				url: '/services/product/listpartial',
				async: async,
				data: {
					pageNumber: currentPage - 1,
					searchKey: searchKey,
					companyId: companyId,
					categoryId: categoryId,
					warehouse, warehouse
				}
			});
		},
		
		getProductImageList: function(productId) {
			return $.ajax({
				url: '/services/product/imagelist',
				data: {
					productId: productId
				}
			});
		},
		
		getPartialProductImageList: function(productId) {
			return $.ajax({
				url: '/services/product/partialimagelist',
				data: {
					productId: productId
				}
			});
		},
		
		saveProduct: function(productFormData) {
    		return $.ajax({
    			url: '/services/product/save',
    			method: 'POST',
    			data: {
    				productFormData: productFormData
    			} 
    		});
    	},
    	
    	uploadProductImage: function(productId, imageFile) {
    		var formData = new FormData();
    		formData.append('imageFile', imageFile);
    		formData.append('productId', productId);
    		
    		return $.ajax({
    			url: '/services/product/uploadimage',
    			method: 'POST',
    			data: formData,
    			cache: false,
    			contentType: false,
    			processData: false
    		});
    	},
    	
    	setProductImageAsThumbnail: function(productImageId) {
    		return $.ajax({
    			url: '/services/product/setthumbnail',
    			method: 'POST',
    			data: {
    				productImageId: productImageId
    			}
    		});
    	},
    	
    	removeProduct: function(productId) {
    		return $.ajax({
    			url: '/services/product/remove',
    			method: 'POST',
    			data: {
    				productId: productId
    			}
    		});
    	},
    	
    	removeProductImage: function(productImageId) {
    		return $.ajax({
    			url: '/services/product/removeimage',
    			method: 'POST',
    			data: {
    				productImageId: productImageId
    			}
    		});
    	},
    	
    	generatePriceList: function(companyId, sendEmail) {
    		return $.ajax({
    			url: '/services/product/generatepricelist',
    			method: 'POST',
    			data: {
    				companyId: companyId,
    				sendEmail: sendEmail
    			}
    		});
    	}
	};
});