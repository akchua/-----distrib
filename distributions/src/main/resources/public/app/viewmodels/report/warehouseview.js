define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/productservice', 'modules/warehouseservice', 'modules/warehouseitemservice', 'viewmodels/manage/productview', 'viewmodels/manage/producthistory'],
		function (dialog, app, ko, productService, warehouseService, warehouseItemService, ProductView, ProductHistory) {
    var WarehouseView = function(warehouseId) {
    	this.warehouseId = warehouseId;
    	this.warehouseItemList = ko.observable();
    	
    	this.formattedPurchaseValue = ko.observable();
    	this.searchKey = ko.observable();
    	
    	this.warehouse = {
    		name: ko.observable(),
    		address: ko.observable()
    	}
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    WarehouseView.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshWarehouseItemList();
		});
		
		self.searchKey.subscribe(function(searchKey) {
			if(searchKey.length >= 3) {
				self.search();
			}
		});
		
		warehouseItemService.getFormattedPurchaseValue(self.warehouseId).done(function(formattedPurchaseValue) {
			self.formattedPurchaseValue(formattedPurchaseValue);
		});
		
		warehouseService.getWarehouse(self.warehouseId).done(function(warehouse) {
			self.warehouse.name(warehouse.name);
			self.warehouse.address(warehouse.address);
		});
		
		self.refreshWarehouseItemList();
    };
    
    WarehouseView.show = function(warehouseId) {
    	return dialog.show(new WarehouseView(warehouseId));
    };
    
    WarehouseView.prototype.refreshWarehouseItemList = function() {
    	var self = this;
    	
    	warehouseItemService.getWarehouseItemList(self.currentPage(), self.searchKey(), self.warehouseId, false).done(function(data) {
    		self.warehouseItemList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    WarehouseView.prototype.history = function(productId) {
    	var self = this;
    	
    	productService.getProduct(productId, null).done(function(product) {
    		ProductHistory.show(product, false)
    	});
    };
    
    WarehouseView.prototype.view = function(productId) {
    	var self = this;
    	
    	productService.getProduct(productId, self.warehouseId).done(function(product) {
    		ProductView.show(product, true)
    	});
    };
    
    WarehouseView.prototype.search = function() {
    	var self = this;
    	
    	self.currentPage(1);
    	self.refreshWarehouseItemList();
    };
    
    WarehouseView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return WarehouseView;
});