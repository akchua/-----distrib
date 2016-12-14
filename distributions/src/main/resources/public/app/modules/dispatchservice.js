define(['jquery'], function ($) {
	return {
		getDispatch: function(dispatchId) {
			return $.ajax({
				url: '/services/dispatch/get',
				data: {
					dispatchId: dispatchId
				}
			});
		},
		
		getDispatchList: function(currentPage, showReceived) {
			return $.ajax({
				url: '/services/dispatch/list',
				data: {
					pageNumber: currentPage - 1,
					showReceived: showReceived
				}
			});
		},
		
		getDispatchItemList: function(currentPage, dispatchId) {
			return $.ajax({
				url: '/services/dispatch/itemlist',
				data: {
					pageNumber: currentPage - 1,
					dispatchId: dispatchId
				}
			});
		},
		
		saveDispatch: function(dispatchFormData) {
    		return $.ajax({
    			url: '/services/dispatch/save',
    			method: 'POST',
    			data: {
    				dispatchFormData: dispatchFormData
    			} 
    		});
    	},
    	
    	dispatch: function(dispatchId) {
    		return $.ajax({
    			url: '/services/dispatch/dispatch',
    			method: 'POST',
    			data: {
    				dispatchId: dispatchId
    			}
    		});
    	},
    	
    	completeDispatch: function(dispatchId) {
    		return $.ajax({
    			url: '/services/dispatch/complete',
    			method: 'POST',
    			data: {
    				dispatchId: dispatchId
    			}
    		});
    	},
    	
    	removeDispatch: function(dispatchId) {
    		return $.ajax({
    			url: '/services/dispatch/remove',
    			method: 'POST',
    			data: {
    				dispatchId: dispatchId
    			}
    		});
    	},
    	
    	addItem: function(clientOrderId, dispatchId) {
    		return $.ajax({
    			url: '/services/dispatch/additem',
    			method: 'POST',
    			data: {
    				clientOrderId: clientOrderId,
    				dispatchId: dispatchId
    			}
    		});
    	},
    	
    	removeItem: function(dispatchItemId) {
    		return $.ajax({
    			url: '/services/dispatch/removeitem',
    			method: 'POST',
    			data: {
    				dispatchItemId: dispatchItemId
    			}
    		});
    	},
    	
    	getWarehouseList: function() {
    		return $.ajax({
    			url: '/services/dispatch/warehouse'
    		});
    	}
	};
});