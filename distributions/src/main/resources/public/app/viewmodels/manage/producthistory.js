define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/purchaseorderitemservice'], 
		function (dialog, app, ko, purchaseOrderItemService) {
    var ProductHistory = function(product) {
    	this.product = product;
    	
    	this.purchaseOrderItemList = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    ProductHistory.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshPurchaseOrderItemList();
		});
		
		self.refreshPurchaseOrderItemList();
    };
    
    ProductHistory.prototype.refreshPurchaseOrderItemList = function() {
    	var self = this;
    	
    	purchaseOrderItemService.getPurchaseOrderItemByProductList(self.currentPage(), self.product.id, false).done(function(data) {
    		self.purchaseOrderItemList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    ProductHistory.show = function(product) {
    	return dialog.show(new ProductHistory(product));
    };
    
    ProductHistory.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return ProductHistory;
});