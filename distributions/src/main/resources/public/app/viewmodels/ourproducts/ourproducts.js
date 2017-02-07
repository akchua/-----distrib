define(['durandal/app', 'knockout', 'modules/productservice', 'modules/companyservice', 'modules/categoryservice', 'viewmodels/manage/partialproductview'],
		function (app, ko, productService, companyService, categoryService, PartialProductView) {
    var OurProducts = function() {
    	this.partialProductList = ko.observable();
    	this.partialCompanyList = ko.observable();
    	this.categoryList = ko.observable();
    	
    	this.companyId = ko.observable();
    	this.categoryId = ko.observable();
    	
    	this.searchKey = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    OurProducts.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshProductList();
		});
		
		categoryService.getCategoryListByName().done(function(categoryList) {
    		self.categoryList(categoryList);
    	});
    	
    	companyService.getPartialCompanyListByName().done(function(partialCompanyList) {
    		self.partialCompanyList(partialCompanyList);
    	});
		
		self.refreshProductList();
    };
    
    OurProducts.prototype.refreshProductList = function() {
    	var self = this;
    	
    	productService.getPartialProductList(self.currentPage(), self.searchKey(), self.companyId(), self.categoryId(), null, true).done(function(data) {
    		self.partialProductList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    OurProducts.prototype.view = function(productId) {
    	var self = this;
    	
    	productService.getPartialProduct(productId, null).done(function(partialProduct) {
    		PartialProductView.show(partialProduct)
    	});
    };
    
    OurProducts.prototype.search = function() {
    	var self = this;
    	
    	self.currentPage(1);
    	self.refreshProductList();
    };
    
    return OurProducts;
});