define(['durandal/app', 'knockout', 'modules/productservice', 'viewmodels/manage/productview'],
		function (app, ko, productService, ProductView) {
    var Home = function() {
    	this.user = app.user;
    	this.productList = ko.observable();
    	
    	this.searchKey = ko.observable();
    	
    	if(this.user) this.itemsPerPage = ko.observable(this.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    Home.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshProductList();
		});
		
		self.refreshProductList();
    };
    
    Home.prototype.refreshProductList = function() {
    	var self = this;
    	
    	productService.getProductList(self.currentPage(), self.searchKey(), null, null, null, true).done(function(data) {
    		self.productList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    Home.prototype.view = function(productId) {
    	var self = this;
    	
    	productService.getProduct(productId, null).done(function(product) {
    		ProductView.show(product, false)
    	});
    };
    
    Home.prototype.search = function() {
    	var self = this;
    	
    	self.currentPage(1);
    	self.refreshProductList();
    };
    
    return Home;
});