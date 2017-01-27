define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/purchaseorderitemservice', 'modules/clientorderitemservice', 'modules/stockadjustservice'], 
		function (dialog, app, ko, purchaseOrderItemService, clientOrderItemService, stockAdjustService) {
    var ProductHistory = function(product) {
    	this.product = product;
    	this.productDisplayName = ko.observable();
    	
    	this.purchaseOrderItemList = ko.observable();
    	this.clientOrderItemList = ko.observable();
    	this.stockAdjustList = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		
    	// PURCHASES
    	this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
		
		// SALES
		this.totalItems2 = ko.observable();
		this.currentPage2 = ko.observable(1);
		this.currentPageSubscription2 = null;
		
		// STOCK ADJUST
		this.totalItems3 = ko.observable();
		this.currentPage3 = ko.observable();
		this.currentPageSubscription3 = null;
    };
    
    ProductHistory.prototype.activate = function() {
    	var self = this;
    	
    	self.productDisplayName(self.product.displayName);
    	
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
		
		self.currentPage3(1);
		self.currentPageSubscription3 = self.currentPage3.subscribe(function() {
			self.refreshStockAdjustList();
		});
		
		self.refreshStockAdjustList();
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
    
    ProductHistory.prototype.refreshStockAdjustList = function() {
    	var self = this;
    	
    	stockAdjustService.getStockAdjustByProductList(self.currentPage3(), self.product.id, false).done(function(data) {
    		self.stockAdjustList(data.list);
    		self.totalItems3(data.total);
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