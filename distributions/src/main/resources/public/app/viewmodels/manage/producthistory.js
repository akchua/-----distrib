define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/purchaseorderitemservice', 'modules/clientorderitemservice', 'modules/stockadjustservice'], 
		function (dialog, app, ko, purchaseOrderItemService, clientOrderItemService, stockAdjustService) {
    var ProductHistory = function(product, enableEdit) {
    	this.product = product;
    	this.enableEdit = enableEdit;
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
    
    ProductHistory.prototype.removeAdjustment = function(stockAdjustId) {
    	var self = this;
    	
    	if(self.enableEdit) {
	    	app.showMessage('<p>Are you sure you want to remove Adjustment of <span class="text-primary"> ID #' + stockAdjustId + '</span>?</p>',
					'<p class="text-danger">Confirm Remove</p>',
					[{ text: 'Yes', value: true }, { text: 'No', value: false }])
			.then(function(confirm) {
				if(confirm) {
					stockAdjustService.removeAdjustment(stockAdjustId).done(function(result) {
						self.refreshStockAdjustList();
						app.showMessage(result.message);
					});
				}
			})
    	}
    };
    
    ProductHistory.show = function(product, enableEdit) {
    	return dialog.show(new ProductHistory(product, enableEdit));
    };
    
    ProductHistory.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return ProductHistory;
});