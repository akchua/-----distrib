define(['plugins/dialog', 'durandal/app', 'knockout'], 
		function (dialog, app, ko) {
    var ProductView = function(product, showStock) {
    	this.product = product;
    	this.showStock = showStock;
    	this.showHere = false;
    	
    	this.productViewModel = {
    		id: ko.observable(),
    		
    		displayName: ko.observable(),
    		
    		companyName: ko.observable(),
    		categoryName: ko.observable(),
    		productCode: ko.observable(),
    		description: ko.observable(),
    		stockCountCurrent: ko.observable(),
    		stockCountAll: ko.observable(),
    		
    		formattedPackageNetSellingPrice: ko.observable()
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
    	
    	self.productViewModel.formattedPackageNetSellingPrice(self.product.formattedPackageNetSellingPrice);
    	
    	self.productViewModel.companyName(self.product.company.name);
    	self.productViewModel.categoryName(self.product.category.name);
    	
    	if(self.product.formattedStockCountCurrent != 0) self.showHere = true;
    };
    
    ProductView.show = function(product, showStock) {
    	return dialog.show(new ProductView(product, showStock));
    };
    
    ProductView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return ProductView;
});