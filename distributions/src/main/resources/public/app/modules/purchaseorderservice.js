define(['jquery'], function ($) {
	return {
		getPurchaseOrder: function(purchaseOrderId) {
			return $.ajax({
				url: '/services/purchaseorder/get',
				data: {
					purchaseOrderId: purchaseOrderId
				}
			});
		},
		
		getTransferInstance: function(sourceId) {
			return $.ajax({
				url: '/services/purchaseorder/gettransfer',
				data: {
					sourceId: sourceId
				}
			});
		},
		
		getPurchaseOrderList: function(currentPage, companyId, warehouse, showPaid) {
			return $.ajax({
				url: '/services/purchaseorder/list',
				data: {
					pageNumber: currentPage - 1,
					companyId: companyId,
					warehouse: warehouse,
					showPaid: showPaid
				}
			});
		},
		
		getPaidPurchaseOrderList: function(currentPage, companyId, warehouse) {
			return $.ajax({
				url: '/services/purchaseorder/paidlist',
				data: {
					pageNumber: currentPage - 1,
					companyId: companyId,
					warehouse: warehouse
				}
			});
		},
		
		getPurchaseOrderListByReportQuery: function(currentPage, purchaseReportQueryData) {
			return $.ajax({
				url: '/services/purchaseorder/listbyreportquery',
				data: {
					pageNumber: currentPage - 1,
					purchaseReportQueryData: purchaseReportQueryData
				}
			});
		},
		
		savePurchaseOrder: function(purchaseOrderFormData) {
    		return $.ajax({
    			url: '/services/purchaseorder/save',
    			method: 'POST',
    			data: {
    				purchaseOrderFormData: purchaseOrderFormData
    			} 
    		});
    	},
    	
    	submitPurchaseOrder: function(purchaseOrderId) {
    		return $.ajax({
    			url: '/services/purchaseorder/submit',
    			method: 'POST',
    			data: {
    				purchaseOrderId: purchaseOrderId
    			}
    		});
    	},
    	
    	sendPurchaseOrder: function(purchaseOrderId) {
    		return $.ajax({
    			url: '/services/purchaseorder/send',
    			method: 'POST',
    			data: {
    				purchaseOrderId: purchaseOrderId
    			}
    		});
    	},
    	
    	receivePurchaseOrder: function(purchaseOrderId) {
    		return $.ajax({
    			url: '/services/purchaseorder/receive',
    			method: 'POST',
    			data: {
    				purchaseOrderId: purchaseOrderId
    			}
    		});
    	},
    	
    	payPurchaseOrder: function(purchaseOrderId) {
    		return $.ajax({
    			url: '/services/purchaseorder/pay',
    			method: 'POST',
    			data: {
    				purchaseOrderId: purchaseOrderId
    			}
    		});
    	},
    	
    	removePurchaseOrder: function(purchaseOrderId) {
    		return $.ajax({
    			url: '/services/purchaseorder/remove',
    			method: 'POST',
    			data: {
    				purchaseOrderId: purchaseOrderId
    			}
    		});
    	},
    	
    	getWarehouseList: function() {
    		return $.ajax({
    			url: '/services/purchaseorder/warehouse'
    		});
    	}
	};
});