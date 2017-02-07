define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/productservice', 'modules/clientorderitemservice', 'viewmodels/manage/partialproductview'],
		function (dialog, app, ko, productService, clientOrderItemService, PartialProductView) {
    var AddItem = function(clientOrderId) {
    	this.clientOrderId = clientOrderId;
    	this.partialProductList = ko.observable();
    	
    	this.searchKey = ko.observable();
    	this.log = ko.observable('<p class="text-info">Click the cart beside the product to add ONE PACKAGE of that product.</p>');
    	this.enableAdd = ko.observable(true);
    	
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
		
		self.refreshProductList();
    };
    
    AddItem.show = function(clientOrderId) {
    	return dialog.show(new AddItem(clientOrderId));
    };
    
    AddItem.prototype.refreshProductList = function() {
    	var self = this;
    	
    	productService.getPartialProductList(self.currentPage(), self.searchKey(), null, null, null, false).done(function(data) {
    		self.partialProductList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    AddItem.prototype.addItem = function(productId) {
    	var self = this;
    	
    	self.enableAdd(false);
    	clientOrderItemService.addItem(productId, self.clientOrderId).done(function(result) {
    		if(result.success) {
    			self.log(result.message);
        	} else {
        		app.showMessage(result.message);
        	}
    		self.enableAdd(true);
    	});
    };
    
    AddItem.prototype.view = function(productId) {
    	var self = this;
    	
    	productService.getPartialProduct(productId, null).done(function(partialProduct) {
    		PartialProductView.show(partialProduct)
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