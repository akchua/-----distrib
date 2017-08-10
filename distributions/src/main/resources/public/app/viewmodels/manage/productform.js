define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/productservice', 'modules/companyservice', 'modules/categoryservice'], 
		function (dialog, app, ko, productService, companyService, categoryService) {
    var ProductForm = function(product, title) {
    	this.product = product;
    	this.title = title;
    	
    	this.companyList = ko.observable();
    	this.categoryList = ko.observable();
    	
    	this.discountAmount = ko.observable();
    	this.profitAmount = ko.observable();
    	
    	this.enableAllowRetailCheckBox = ko.observable(true);
    	
    	this.productFormModel = {
    		id: ko.observable(),
    		
    		companyId: ko.observable(),
    		categoryId: ko.observable(),
    		productCode: ko.observable(),
    		name: ko.observable(),
    		size: ko.observable(),
    		packaging: ko.observable(),
    		allowRetail: ko.observable(true),
    		description: ko.observable(),
    		
    		packageGrossPrice: ko.observable(),
    		discount: ko.observable(),
    		packageNetPrice: ko.observable(),
    		packageSellingPrice: ko.observable(),
    		percentProfit: ko.observable()
	    };
    };
    
    ProductForm.prototype.activate = function() {
    	var self = this;
    	
    	self.productFormModel.id(self.product.id);
    	
    	self.productFormModel.productCode(self.product.productCode);
    	self.productFormModel.name(self.product.name);
    	self.productFormModel.size(self.product.size);
    	self.productFormModel.packaging(self.product.packaging);
    	if(self.product.allowRetail) {
    		self.productFormModel.allowRetail(self.product.allowRetail);
    	}
    	self.productFormModel.description(self.product.description);
    	
    	self.productFormModel.packageGrossPrice(self.product.packageGrossPrice);
    	self.productFormModel.discount(self.product.discount);
    	self.productFormModel.packageNetPrice(self.product.packageNetPrice);
    	self.productFormModel.packageSellingPrice(self.product.packageSellingPrice);
    	self.productFormModel.percentProfit(self.product.percentProfit);
    	
    	self.discountAmount(self.product.packageDiscountAmount);
    	self.profitAmount(self.product.packageProfitAmount);
    	
    	if(self.productFormModel.packaging() <= 1) {
    		self.enableAllowRetailCheckBox(false);
    	}
    	
    	categoryService.getCategoryListByName().done(function(categoryList) {
    		self.categoryList(categoryList);
    		self.productFormModel.categoryId(self.product.category.id);
    	});
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    		self.productFormModel.companyId(self.product.company.id);
    	});
    };
    
    ProductForm.prototype.compositionComplete = function() {
    	var self = this;
    	
    	self.productFormModel.packaging.subscribe(function(newPackaging) {
    		if(newPackaging <= 1) {
    			self.productFormModel.allowRetail(false);
    			self.enableAllowRetailCheckBox(false);
    		} else {
    			self.enableAllowRetailCheckBox(true);
    		}
    	});
    	
    	self.productFormModel.packageGrossPrice.subscribe(function(newPackageGrossPrice) {
    		self.productFormModel.packageNetPrice((newPackageGrossPrice * (100 - self.productFormModel.discount()) / 100).toFixed(2));
    	});
    	
    	self.productFormModel.discount.subscribe(function(newDiscount) {
    		self.productFormModel.packageNetPrice((self.productFormModel.packageGrossPrice() * (100 - newDiscount) / 100).toFixed(2));
    	});
    	
    	self.productFormModel.packageNetPrice.subscribe(function(newPackageNetPrice) {
    		self.discountAmount((self.productFormModel.packageGrossPrice() - newPackageNetPrice).toFixed(2));
    		self.profitAmount((self.productFormModel.packageSellingPrice() - newPackageNetPrice).toFixed(2));
    	});
    	
    	self.productFormModel.packageSellingPrice.subscribe(function(newPackageSellingPrice) {
    		self.profitAmount((newPackageSellingPrice - self.productFormModel.packageNetPrice()).toFixed(2));
    	});
    	
    	self.profitAmount.subscribe(function(newProfitAmount) {
    		self.productFormModel.percentProfit((newProfitAmount / self.productFormModel.packageNetPrice() * 100).toFixed(2));
    	});
    };
    
    ProductForm.show = function(product, title) {
    	return dialog.show(new ProductForm(product, title));
    };
    
    ProductForm.prototype.save = function() {
    	var self = this;
    	
        productService.saveProduct(ko.toJSON(self.productFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    ProductForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return ProductForm;
});