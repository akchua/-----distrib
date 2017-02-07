define(['plugins/dialog', 'durandal/app', 'knockout'], 
		function (dialog, app, ko) {
    var PartialProductView = function(partialProduct) {
    	this.partialProduct = partialProduct;
    	
    	this.partialProductViewModel = {
    		id: ko.observable(),
    		
    		displayName: ko.observable(),
    		
    		companyName: ko.observable(),
    		categoryName: ko.observable(),
    		productCode: ko.observable(),
    		description: ko.observable(),
    		formattedPackageNetSellingPrice: ko.observable()
	    };
    };
    
    PartialProductView.prototype.activate = function() {
    	var self = this;
    	
    	self.partialProductViewModel.id(self.partialProduct.id);
    	
    	self.partialProductViewModel.productCode(self.partialProduct.productCode);
    	self.partialProductViewModel.displayName(self.partialProduct.displayName);
    	self.partialProductViewModel.description(self.partialProduct.description);
    	self.partialProductViewModel.formattedPackageNetSellingPrice(self.partialProduct.formattedPackageNetSellingPrice);
    	
    	self.partialProductViewModel.companyName(self.partialProduct.companyName);
    	self.partialProductViewModel.categoryName(self.partialProduct.categoryName);
    };
    
    PartialProductView.show = function(partialProduct) {
    	return dialog.show(new PartialProductView(partialProduct));
    };
    
    PartialProductView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return PartialProductView;
});