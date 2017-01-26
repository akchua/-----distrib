define(['durandal/app', 'knockout', 'modules/productservice', 'modules/companyservice', 'modules/categoryservice', 'viewmodels/manage/productform', 'viewmodels/manage/productview', 'viewmodels/manage/producthistory'],
		function (app, ko, productService, companyService, categoryService, ProductView, ProductHistory) {
    var Inventory = function() {
    	this.productList = ko.observable();
    	this.companyList = ko.observable();
    	this.categoryList = ko.observable();
    	
    	this.companyId = ko.observable();
    	this.categoryId = ko.observable();
    	
    	this.searchKey = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    Inventory.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshProductList();
		});
		
		categoryService.getCategoryListByName().done(function(categoryList) {
    		self.categoryList(categoryList);
    	});
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    	});
		
		self.refreshProductList();
    };
    
    Inventory.prototype.refreshProductList = function() {
    	var self = this;
    	
    	productService.getProductList(self.currentPage(), self.searchKey(), self.companyId(), self.categoryId(), null, true).done(function(data) {
    		self.productList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    Inventory.prototype.history = function(productId) {
    	var self = this;
    	
    	productService.getProduct(productId, null).done(function(product) {
    		ProductHistory.show(product)
    	});
    };
    
    Inventory.prototype.view = function(productId) {
    	var self = this;
    	
    	productService.getProduct(productId, null).done(function(product) {
    		ProductView.show(product, true)
    	});
    };
    
    Inventory.prototype.search = function() {
    	var self = this;
    	
    	self.currentPage(1);
    	self.refreshProductList();
    };
    
    return Inventory;
});