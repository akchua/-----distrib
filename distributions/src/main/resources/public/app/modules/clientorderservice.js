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
		
		getPartialClientOrder: function(clientOrderId) {
			return $.ajax({
				url: '/services/clientorder/getpartial',
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
		
		getPartialClientOrderList: function(currentPage, showPaid) {
			return $.ajax({
				url: '/services/clientorder/listpartial',
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
		
		getClientOrderRequestByCurrentUserList: function(currentPage) {
			return $.ajax({
				url: '/services/clientorder/requestlistbycurrentuser',
				data: {
					pageNumber: currentPage - 1
				}
			});
		},
		
		getAcceptedClientOrderList: function(currentPage, warehouseId, async) {
			return $.ajax({
				url: '/services/clientorder/acceptedlist',
				async: async,
				data: {
					pageNumber: currentPage - 1,
					warehouseId: warehouseId
				}
			});
		},
		
		getReceivedClientOrderList: function(currentPage, warehouseId, clientId) {
			return $.ajax({
				url: '/services/clientorder/receivedlist',
				data: {
					pageNumber: currentPage - 1,
					warehouseId: warehouseId,
					clientId: clientId
				}
			});
		},
		
		getPaidClientOrderList: function(currentPage, warehouseId) {
			return $.ajax({
				url: '/services/clientorder/paidlist',
				data: {
					pageNumber: currentPage - 1,
					warehouseId: warehouseId
				}
			});
		},
		
		getClientOrderListByReportQuery: function(currentPage, salesReportQueryData) {
			return $.ajax({
				url: '/services/clientorder/listbyreportquery',
				data: {
					pageNumber: currentPage - 1,
					salesReportQueryData: salesReportQueryData
				}
			});
		},
		
		getFormattedTotalPayable: function() {
			return $.ajax({
				url: '/services/clientorder/formattedpayable'
			});
		},
		
    	addClientOrder: function(companyId, clientId) {
    		return $.ajax({
    			url: '/services/clientorder/add',
    			method: 'POST',
    			data: {
    				companyId: companyId,
    				clientId: clientId
    			}
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
    	
    	generateClientRanking: function(clientRankQueryData) {
    		return $.ajax({
    			url: '/services/clientorder/generateclientranking',
    			method: 'POST',
    			data: {
    				clientRankQueryData: clientRankQueryData
    			}
    		});
    	},
    	
    	getClientSalesReportType: function() {
    		return $.ajax({
    			url: '/services/clientorder/clientreporttypes'
    		});
    	},
    	
    	getClientRankTypeList: function() {
    		return $.ajax({
    			url: '/services/clientorder/clientranktypes'
    		});
    	},
    	
    	getAreaList: function() {
    		return $.ajax({
    			url: '/services/clientorder/area'
    		});
    	}
	};
});