define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/productservice', 'modules/purchaseorderitemservice', 'viewmodels/manage/productview'],
		function (dialog, app, ko, productService, purchaseOrderItemService, ProductView) {
    var AddItem = function(purchaseOrderId, companyId, warehouseId) {
    	this.purchaseOrderId = purchaseOrderId;
    	this.companyId = companyId;
    	this.warehouseId = warehouseId;
    	this.productList = ko.observable();
    	
    	this.searchKey = ko.observable();
    	this.log = ko.observable('<p class="text-info">Click the cart beside the product to add ONE PACKAGE of that product.</p>');
    	this.enableAdd = ko.observable(true);
    	this.searchFocus = ko.observable(true);
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    AddItem.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshProductList();
		});
		
		self.searchKey.subscribe(function(searchKey) {
			if(searchKey.length >= 3) {
				self.search();
			}
		});
		
		self.refreshProductList();
    };
    
    AddItem.show = function(purchaseOrderId, companyId, warehouseId) {
    	return dialog.show(new AddItem(purchaseOrderId, companyId, warehouseId));
    };
    
    AddItem.prototype.refreshProductList = function() {
    	var self = this;
    	
    	productService.getProductList(self.currentPage(), self.searchKey(), self.companyId, null, self.warehouseId, false).done(function(data) {
    		self.productList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    AddItem.prototype.addItem = function(productId) {
    	var self = this;
    	
    	self.enableAdd(false);
    	purchaseOrderItemService.addItem(productId, self.purchaseOrderId).done(function(result) {
    		if(result.success) {
    			self.log(result.message);
    			self.searchKey('');
				self.searchFocus(true);
        	} else {
        		app.showMessage(result.message);
        	}
    		self.enableAdd(true);
    	});
    };
    
    AddItem.prototype.view = function(productId) {
    	var self = this;
    	
    	productService.getProduct(productId, self.warehouseId).done(function(product) {
    		ProductView.show(product, true)
    	});
    };
    
    AddItem.prototype.search = function() {
    	var self = this;
    	
    	self.currentPage(1);
    	self.refreshProductList();
    };
    
    AddItem.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return AddItem;
});