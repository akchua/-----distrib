define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/productservice', 'viewmodels/manage/productview', 'viewmodels/manage/producthistory'],
		function (dialog, app, ko, productService, ProductView, ProductHistory) {
    var WarehouseView = function(warehouse) {
    	this.warehouse = warehouse;
    	this.warehouseItemList = ko.observable();
    	
    	this.searchKey = ko.observable();
    	
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
		
		self.refreshWarehouseItemList();
    };
    
    WarehouseView.show = function(warehouse) {
    	return dialog.show(new WarehouseView(warehouse));
    };
    
    WarehouseView.prototype.refreshWarehouseItemList = function() {
    	var self = this;
    	
    	productService.getWarehouseItemList(self.currentPage(), self.searchKey(), self.warehouse, false).done(function(data) {
    		self.warehouseItemList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    WarehouseView.prototype.history = function(productId) {
    	var self = this;
    	
    	productService.getProduct(productId, null).done(function(product) {
    		ProductHistory.show(product)
    	});
    };
    
    WarehouseView.prototype.view = function(productId) {
    	var self = this;
    	
    	productService.getProduct(productId, self.warehouse).done(function(product) {
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