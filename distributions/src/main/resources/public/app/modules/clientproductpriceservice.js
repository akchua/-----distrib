define(['jquery'], function ($) {
	return {
		getClientProductPrice: function(clientProductPriceId) {
			return $.ajax({
				url: '/services/clientproductprice/get',
				data: {
					clientProductPriceId: clientProductPriceId
				}
			});
		},
		
		getClientProductPriceList: function(currentPage, clientId) {
			return $.ajax({
				url: '/services/clientproductprice/list',
				data: {
					pageNumber: currentPage - 1,
					clientId: clientId
				}
			});
		},
		
		saveClientProductPrice: function(clientProductPriceFormData) {
    		return $.ajax({
    			url: '/services/clientproductprice/save',
    			method: 'POST',
    			data: {
    				clientProductPriceFormData: clientProductPriceFormData
    			} 
    		});
    	},
    	
    	removeClientProductPrice: function(clientProductPriceId) {
    		return $.ajax({
    			url: '/services/clientproductprice/remove',
    			method: 'POST',
    			data: {
    				clientProductPriceId: clientProductPriceId
    			}
    		});
    	}
	};
});