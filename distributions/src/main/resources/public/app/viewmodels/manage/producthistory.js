define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/purchaseorderitemservice', 'modules/clientorderitemservice'], 
		function (dialog, app, ko, purchaseOrderItemService, clientOrderItemService) {
    var ProductHistory = function(product) {
    	this.product = product;
    	
    	this.purchaseOrderItemList = ko.observable();
    	this.clientOrderItemList = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		
    	this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		this.totalItems2 = ko.observable();
		this.currentPage2 = ko.observable(1);
		this.currentPageSubscription2 = null;
    };
    
    ProductHistory.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshPurchaseOrderItemList();
		});
		
		self.refreshPurchaseOrderItemList();
		
		self.currentPage2(1);
		self.currentPageSubscription2 = self.currentPage2.subscribe(function() {
			self.refreshClientOrderItemList();
		});
		
		self.refreshClientOrderItemList();
    };
    
    ProductHistory.prototype.refreshPurchaseOrderItemList = function() {
    	var self = this;
    	
    	purchaseOrderItemService.getPurchaseOrderItemByProductList(self.currentPage(), self.product.id, false).done(function(data) {
    		self.purchaseOrderItemList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    ProductHistory.prototype.refreshClientOrderItemList = function() {
    	var self = this;
    	
    	clientOrderItemService.getClientOrderItemByProductList(self.currentPage2(), self.product.id, false).done(function(data) {
    		self.clientOrderItemList(data.list);
    		self.totalItems2(data.total);
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