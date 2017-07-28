define(['durandal/app', 'knockout', 'modules/productservice', 'modules/companyservice', 'modules/categoryservice', 'viewmodels/manage/productform', 'viewmodels/manage/productview', 'viewmodels/manage/productgallery'],
		function (app, ko, productService, companyService, categoryService, ProductForm, ProductView, ProductGallery) {
    var Product = function() {
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
    
    Product.prototype.activate = function() {
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
    
    Product.prototype.refreshProductList = function() {
    	var self = this;
    	
    	productService.getProductList(self.currentPage(), self.searchKey(), self.companyId(), self.categoryId(), null, true).done(function(data) {
    		self.productList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    Product.prototype.add = function() {
    	var self = this;
    	
    	ProductForm.show(new Object(), 'Create Product').done(function() {
    		self.refreshProductList();
    	});
    };
    
    Product.prototype.view = function(productId) {
    	var self = this;
    	
    	productService.getProduct(productId, null).done(function(product) {
    		ProductView.show(product, true)
    	});
    };
    
    Product.prototype.gallery = function(productId) {
		ProductGallery.show(productId)
    };
    
    Product.prototype.edit = function(productId) {
    	var self = this;
    	
    	productService.getProduct(productId, null).done(function(product) {
    		ProductForm.show(product, 'Edit Product').done(function() {
    			self.refreshProductList();
    		});
    	});
    };
    
    Product.prototype.remove = function(productId, productDisplayName) {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to remove Product <span class="text-primary">' + productDisplayName + '</span>?</p>',
				'<p class="text-danger">Confirm Remove</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				productService.removeProduct(productId).done(function(result) {
					self.refreshProductList();
					app.showMessage(result.message);
				});
			}
		})
    };
    
    Product.prototype.search = function() {
    	var self = this;
    	
    	self.currentPage(1);
    	self.refreshProductList();
    };
    
    return Product;
});