define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/clientproductpriceservice', 'modules/clientcompanypriceservice', 'modules/userservice', 'modules/productservice'], 
			function (dialog, app, ko, clientPromoService, clientCompanyPriceService, userService, productService) {
    var ClientPromoForm = function(clientPromo, title) {
    	this.clientPromo = clientPromo;
    	this.title = title;
    	
    	this.clientList = ko.observable();
    	this.productList = ko.observable();
    	
    	this.productFormattedPackageNetSellingPrice = ko.observable('');
    	this.productPackageNetSellingPrice = ko.observable(0);
    	this.productSellingDiscount = ko.observable(0);
    	
    	this.discountedPackageSellingPrice = ko.observable(0);
    	
    	this.enableSelect = ko.observable(false);
    	
    	this.clientPromoFormModel = {
    		id: ko.observable(),
    		
    		clientId: ko.observable(),
    		productId: ko.observable(),
    		discount: ko.observable(0)
	    };
    };
    
    ClientPromoForm.prototype.activate = function() {
    	var self = this;
    	
    	if(!self.clientPromo.id) {
    		self.enableSelect(true);
    	}
    	
    	self.clientPromoFormModel.id(self.clientPromo.id);
    	self.clientPromoFormModel.discount(self.clientPromo.discount);
    	
    	userService.getFullClientList().done(function(clientList) {
			self.clientList(clientList);
			if(self.clientPromo.client) {
				self.clientPromoFormModel.clientId(self.clientPromo.client.id);
			}
		});
    	
    	productService.getProductListByName().done(function(productList) {
    		self.productList(productList);
    		if(self.clientPromo.product) {
    			self.clientPromoFormModel.productId(self.clientPromo.product.id);
    		}
    	});
    };
    
    ClientPromoForm.prototype.compositionComplete = function() {
    	var self = this;
    	
    	self.refreshProduct();
		self.refreshClient();
    	
    	self.clientPromoFormModel.clientId.subscribe(function(newClientId) {
    		self.refreshProduct();
    	});
    	
    	self.clientPromoFormModel.productId.subscribe(function(newProductId) {
    		self.refreshProduct();
    	});
    	
    	self.productPackageNetSellingPrice.subscribe(function() {
    		self.refreshDiscountedPackageSellingPrice();
    	});
    	
    	self.clientPromoFormModel.discount.subscribe(function() {
    		self.refreshDiscountedPackageSellingPrice();
    	});
    };
    
    ClientPromoForm.show = function(clientPromo, title) {
    	return dialog.show(new ClientPromoForm(clientPromo, title));
    };
    
    ClientPromoForm.prototype.refreshDiscountedPackageSellingPrice = function() {
    	var self = this;
    	
    	self.discountedPackageSellingPrice(
			Math.round(
					self.productPackageNetSellingPrice() 
					* ((100.0 - self.clientPromoFormModel.discount()) / 100)
    	);
    };
    
    ClientPromoForm.prototype.refreshProduct = function() {
    	var self = this;
    	
    	if(self.clientPromoFormModel.productId()) {
    		productService.getPartialProductFor(self.clientPromoFormModel.productId, self.clientPromoFormModel.clientId).done(function(partialProduct) {
	    		self.productPackageNetSellingPrice(partialProduct.packageNetSellingPrice);
    			self.productFormattedPackageNetSellingPrice(partialProduct.formattedPackageNetSellingPrice);
	    		self.productSellingDiscount(partialProduct.sellingDiscount);
	    	});
    	}
    };
    
    ClientPromoForm.prototype.save = function() {
    	var self = this;
    	
    	clientPromoService.saveClientPromo(ko.toJSON(self.clientPromoFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    ClientPromoForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return ClientPromoForm;
});