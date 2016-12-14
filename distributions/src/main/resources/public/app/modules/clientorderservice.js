define(['jquery'], function ($) {
	return {
		getClientOrder: function(clientOrderId) {
			return $.ajax({
				url: '/services/clientorder/get',
				data: {
					clientOrderId: clientOrderId
				}
			});
		},
		
		getClientOrderList: function(currentPage, showPaid) {
			return $.ajax({
				url: '/services/clientorder/list',
				data: {
					pageNumber: currentPage - 1,
					showPaid: showPaid
				}
			});
		},
		
		getClientOrderRequestList: function(currentPage, showAccepted) {
			return $.ajax({
				url: '/services/clientorder/requestlist',
				data: {
					pageNumber: currentPage - 1,
					showAccepted: showAccepted
				}
			});
		},
		
		getAcceptedClientOrderList: function(currentPage, warehouse, async) {
			return $.ajax({
				url: '/services/clientorder/acceptedlist',
				async: async,
				data: {
					pageNumber: currentPage - 1,
					warehouse: warehouse
				}
			});
		},
		
		getPaidClientOrderList: function(currentPage, warehouse) {
			return $.ajax({
				url: '/services/clientorder/paidlist',
				data: {
					pageNumber: currentPage - 1,
					warehouse: warehouse
				}
			});
		},
		
		addClientOrder: function() {
    		return $.ajax({
    			url: '/services/clientorder/add',
    			method: 'POST'
    		});
    	},
    	
    	submitClientOrder: function(clientOrderId) {
    		return $.ajax({
    			url: '/services/clientorder/submit',
    			method: 'POST',
    			data: {
    				clientOrderId: clientOrderId
    			}
    		});
    	},
    	
    	testAcceptClientOrder: function(clientOrderId, warehouse) {
    		return $.ajax({
    			url: '/services/clientorder/testaccept',
    			method: 'POST',
    			data: {
    				clientOrderId: clientOrderId,
    				warehouse: warehouse
    			}
    		});
    	},
    	
    	acceptClientOrder: function(clientOrderId, warehouse) {
    		return $.ajax({
    			url: '/services/clientorder/accept',
    			method: 'POST',
    			data: {
    				clientOrderId: clientOrderId,
    				warehouse: warehouse
    			}
    		});
    	},
    	
    	adjustAndAcceptClientOrder: function(clientOrderId, warehouse) {
    		return $.ajax({
    			url: '/services/clientorder/adjustaccept',
    			method: 'POST',
    			data: {
    				clientOrderId: clientOrderId,
    				warehouse: warehouse
    			}
    		});
    	},
    	
    	removeClientOrder: function(clientOrderId) {
    		return $.ajax({
    			url: '/services/clientorder/remove',
    			method: 'POST',
    			data: {
    				clientOrderId: clientOrderId
    			}
    		});
    	},
    	
    	getWarehouseList: function() {
    		return $.ajax({
    			url: '/services/clientorder/warehouse'
    		});
    	}
	};
});