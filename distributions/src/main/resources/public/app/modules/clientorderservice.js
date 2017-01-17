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
		
		getTransferInstance: function(sourceId) {
			return $.ajax({
				url: '/services/clientorder/gettransfer',
				data: {
					sourceId: sourceId
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
		
		getReceivedClientOrderList: function(currentPage, warehouse) {
			return $.ajax({
				url: '/services/clientorder/receivedlist',
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
		
		getFormattedTotalPayable: function() {
			return $.ajax({
				url: '/services/clientorder/formattedpayable'
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
    	
    	/*testAcceptClientOrder: function(clientOrderId, warehouse) {
    		return $.ajax({
    			url: '/services/clientorder/testaccept',
    			method: 'POST',
    			data: {
    				clientOrderId: clientOrderId,
    				warehouse: warehouse
    			}
    		});
    	},*/
    	
    	acceptClientOrder: function(clientOrderId) {
    		return $.ajax({
    			url: '/services/clientorder/accept',
    			method: 'POST',
    			data: {
    				clientOrderId: clientOrderId
    			}
    		});
    	},
    	
    	/*adjustAndAcceptClientOrder: function(clientOrderId, warehouse) {
    		return $.ajax({
    			url: '/services/clientorder/adjustaccept',
    			method: 'POST',
    			data: {
    				clientOrderId: clientOrderId,
    				warehouse: warehouse
    			}
    		});
    	},*/
    	
    	payClientOrder: function(clientOrderId) {
    		return $.ajax({
    			url: '/services/clientorder/pay',
    			method: 'POST',
    			data: {
    				clientOrderId: clientOrderId
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
    	
    	generateReport: function(salesReportQueryData) {
    		return $.ajax({
    			url: '/services/clientorder/generatereport',
    			method: 'POST',
    			data: {
    				salesReportQueryData: salesReportQueryData
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