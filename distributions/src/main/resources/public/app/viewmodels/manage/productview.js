define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/productservice'], 
		function (dialog, app, ko, productService) {
    var ProductView = function(product, showStock) {
    	this.product = product;
    	this.productImageList = ko.observable();
    	this.showStock = showStock;
    	this.showHere = false;
    	
    	this.productViewModel = {
    		id: ko.observable(),
    		
    		displayName: ko.observable(),
    		image: ko.observable(),
    		
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
    	self.productViewModel.image(productService.getProductImageByFileName(self.product.image));
    	self.productViewModel.description(self.product.description);
    	self.productViewModel.stockCountCurrent(self.product.formattedStockCountCurrent);
    	self.productViewModel.stockCountAll(self.product.formattedStockCountAll);
    	
    	self.productViewModel.formattedPackageNetSellingPrice(self.product.formattedPackageNetSellingPrice);
    	
    	self.productViewModel.companyName(self.product.company.name);
    	self.productViewModel.categoryName(self.product.category.name);
    	
    	if(self.product.formattedStockCountCurrent != 0) self.showHere = true;
    	
    	productService.getProductImageList(self.product.id).done(function(productImageList) {
    		for(i = 0; i < productImageList.length; i++) {
    			productImageList[i].filePath = productService.getProductImageByFileName(productImageList[i].fileName);
    		}
    		self.productImageList(productImageList);
    	});
    };
    
    ProductView.show = function(product, showStock) {
    	return dialog.show(new ProductView(product, showStock));
    };
    
    ProductView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return ProductView;
});