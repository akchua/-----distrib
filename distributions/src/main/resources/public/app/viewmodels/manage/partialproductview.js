define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/productservice'], 
		function (dialog, app, ko, productService) {
    var PartialProductView = function(partialProduct) {
    	this.partialProduct = partialProduct;
    	
    	this.partialProductImageList = ko.observable();
    	
    	this.partialProductViewModel = {
    		id: ko.observable(),
    		
    		displayName: ko.observable(),
    		image: ko.observable(),
    		
    		companyName: ko.observable(),
    		categoryName: ko.observable(),
    		productCode: ko.observable(),
    		description: ko.observable(),
    		formattedPackageSellingDiscount: ko.observable(),
    		formattedPackageNetSellingPrice: ko.observable()
	    };
    };
    
    PartialProductView.prototype.activate = function() {
    	var self = this;
    	
    	self.partialProductViewModel.id(self.partialProduct.id);
    	
    	self.partialProductViewModel.productCode(self.partialProduct.productCode);
    	self.partialProductViewModel.displayName(self.partialProduct.displayName);
    	self.partialProductViewModel.image(productService.getProductImageByFileName(self.partialProduct.image));
    	self.partialProductViewModel.description(self.partialProduct.description);
    	
    	self.partialProductViewModel.formattedPackageSellingDiscount(self.partialProduct.formattedPackageSellingDiscount);
    	self.partialProductViewModel.formattedPackageNetSellingPrice(self.partialProduct.formattedPackageNetSellingPrice);
    	
    	self.partialProductViewModel.companyName(self.partialProduct.companyName);
    	self.partialProductViewModel.categoryName(self.partialProduct.categoryName);
    	
    	productService.getPartialProductImageList(self.partialProduct.id).done(function(partialProductImageList) {
    		for(i = 0; i < partialProductImageList.length; i++) {
    			partialProductImageList[i].filePath = productService.getProductImageByFileName(partialProductImageList[i].fileName);
    		}
    		self.partialProductImageList(partialProductImageList);
    	});
    };
    
    PartialProductView.show = function(partialProduct) {
    	return dialog.show(new PartialProductView(partialProduct));
    };
    
    PartialProductView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return PartialProductView;
});