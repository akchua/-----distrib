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
		
		getPurchaseOrderList: function(currentPage, companyId, area) {
			return $.ajax({
				url: '/services/purchaseorder/list',
				data: {
					pageNumber: currentPage - 1,
					companyId: companyId,
					area: area
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
    	
    	removePurchaseOrder: function(purchaseOrderId) {
    		return $.ajax({
    			url: '/services/purchaseorder/remove',
    			method: 'POST',
    			data: {
    				purchaseOrderId: purchaseOrderId
    			}
    		});
    	},
    	
    	getAreaList: function() {
    		return $.ajax({
    			url: '/services/purchaseorder/area'
    		});
    	}
	};
});