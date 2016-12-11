define(['plugins/dialog', 'durandal/app', 'knockout'], 
		function (dialog, app, ko) {
    var ProductView = function(product) {
    	this.product = product;
    	
    	this.productViewModel = {
    		id: ko.observable(),
    		
    		displayName: ko.observable(),
    		
    		companyName: ko.observable(),
    		categoryName: ko.observable(),
    		productCode: ko.observable(),
    		description: ko.observable(),
    		stockCountCurrent: ko.observable(),
    		stockCountAll: ko.observable(),
    		
    		formattedPackageSellingPrice: ko.observable()
	    };
    };
    
    ProductView.prototype.activate = function() {
    	var self = this;
    	
    	self.productViewModel.id(self.product.id);
    	
    	self.productViewModel.productCode(self.product.productCode);
    	self.productViewModel.displayName(self.product.displayName);
    	self.productViewModel.description(self.product.description);
    	self.productViewModel.stockCountCurrent(self.product.formattedStockCountCurrent);
    	self.productViewModel.stockCountAll(self.product.formattedStockCountAll);
    	
    	self.productViewModel.formattedPackageSellingPrice(self.product.formattedPackageSellingPrice);
    	
    	self.productViewModel.companyName(self.product.company.name);
    	self.productViewModel.categoryName(self.product.category.name);
    };
    
    ProductView.show = function(product) {
    	return dialog.show(new ProductView(product));
    };
    
    ProductView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return ProductView;
});