define(['jquery'], function ($) {
	return {
		getClientCompanyPrice: function(clientCompanyPriceId) {
			return $.ajax({
				url: '/services/clientcompanyprice/get',
				data: {
					clientCompanyPriceId: clientCompanyPriceId
				}
			});
		},
		
		getClientCompanyPriceByClientAndProduct: function(clientId, productId) {
			return $.ajax({
				url: '/services/clientcompanyprice/getbyclientandproduct',
				data: {
					clientId: clientId,
					productId: productId
				}
			});
		},
		
		getClientCompanyPriceList: function(currentPage, clientId) {
			return $.ajax({
				url: '/services/clientcompanyprice/list',
				data: {
					pageNumber: currentPage - 1,
					clientId: clientId
				}
			});
		},
		
		saveClientCompanyPrice: function(clientCompanyPriceFormData) {
    		return $.ajax({
    			url: '/services/clientcompanyprice/save',
    			method: 'POST',
    			data: {
    				clientCompanyPriceFormData: clientCompanyPriceFormData
    			} 
    		});
    	},
    	
    	removeClientCompanyPrice: function(clientCompanyPriceId) {
    		return $.ajax({
    			url: '/services/clientcompanyprice/remove',
    			method: 'POST',
    			data: {
    				clientCompanyPriceId: clientCompanyPriceId
    			}
    		});
    	}
	};
});