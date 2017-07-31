define(['jquery'], function ($) {
	return {
		getClientPromo: function(clientPromoId) {
			return $.ajax({
				url: '/services/clientpromo/get',
				data: {
					clientPromoId: clientPromoId
				}
			});
		},
		
		getClientPromoList: function(currentPage, clientId) {
			return $.ajax({
				url: '/services/clientpromo/list',
				data: {
					pageNumber: currentPage - 1,
					clientId: clientId
				}
			});
		},
		
		saveClientPromo: function(clientPromoFormData) {
    		return $.ajax({
    			url: '/services/clientpromo/save',
    			method: 'POST',
    			data: {
    				clientPromoFormData: clientPromoFormData
    			} 
    		});
    	},
    	
    	removeClientPromo: function(clientPromoId) {
    		return $.ajax({
    			url: '/services/clientpromo/remove',
    			method: 'POST',
    			data: {
    				clientPromoId: clientPromoId
    			}
    		});
    	}
	};
});