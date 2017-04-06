define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/clientproductpriceservice', 'modules/clientcompanypriceservice', 'modules/userservice', 'modules/productservice'], 
			function (dialog, app, ko, clientProductPriceService, clientCompanyPriceService, userService, productService) {
    var ClientProductPriceForm = function(clientProductPrice, title) {
    	this.clientProductPrice = clientProductPrice;
    	this.title = title;
    	
    	this.clientList = ko.observable();
    	this.productList = ko.observable();
    	
    	this.productFormattedPackageNetSellingPrice = ko.observable('');
    	this.productSellingDiscount = ko.observable(0);
    	this.clientCompanyPriceDiscount = ko.observable(0);
    	this.clientLessVat = ko.observable(0);
    	
    	this.netPackageSellingPrice = ko.observable(0);
    	
    	this.enableSelect = ko.observable(false);
    	
    	this.clientProductPriceFormModel = {
    		id: ko.observable(),
    		
    		clientId: ko.observable(),
    		productId: ko.observable(),
    		packageSellingPrice: ko.observable(0)
	    };
    };
    
    ClientProductPriceForm.prototype.activate = function() {
    	var self = this;
    	
    	if(!self.clientProductPrice.id) {
    		self.enableSelect(true);
    	}
    	
    	self.clientProductPriceFormModel.id(self.clientProductPrice.id);
    	self.clientProductPriceFormModel.packageSellingPrice(self.clientProductPrice.packageSellingPrice);
    	
    	userService.getFullClientList().done(function(clientList) {
			self.clientList(clientList);
			if(self.clientProductPrice.client) {
				self.clientProductPriceFormModel.clientId(self.clientProductPrice.client.id);
			}
		});
    	
    	productService.getProductListByName().done(function(productList) {
    		self.productList(productList);
    		if(self.clientProductPrice.product) {
    			self.clientProductPriceFormModel.productId(self.clientProductPrice.product.id);
    		}
    	});
    };
    
    ClientProductPriceForm.prototype.compositionComplete = function() {
    	var self = this;
    	
    	self.refreshProduct();
    	self.refreshClientCompanyPrice();
		self.refreshClient();
    	
    	self.clientProductPriceFormModel.clientId.subscribe(function(newClientId) {
    		self.refreshClientCompanyPrice();
    		self.refreshClient();
    		self.refreshProduct();
    	});
    	
    	self.clientProductPriceFormModel.productId.subscribe(function(newProductId) {
    		self.refreshProduct();
        	self.refreshClientCompanyPrice();
    	});
    	
    	self.clientProductPriceFormModel.packageSellingPrice.subscribe(function() {
    		self.refreshNetPackageSellingPrice();
    	});
    	
    	self.clientCompanyPriceDiscount.subscribe(function() {
    		self.refreshNetPackageSellingPrice();
    	});
    	
    	self.clientLessVat.subscribe(function() {
    		self.refreshNetPackageSellingPrice();
    	});
    };
    
    ClientProductPriceForm.show = function(clientProductPrice, title) {
    	return dialog.show(new ClientProductPriceForm(clientProductPrice, title));
    };
    
    ClientProductPriceForm.prototype.refreshNetPackageSellingPrice = function() {
    	var self = this;
    	
    	self.netPackageSellingPrice(
			Math.round(
					self.clientProductPriceFormModel.packageSellingPrice() 
					* ((100.0 - self.productSellingDiscount()) / 100)
					* ((100.0 - self.clientCompanyPriceDiscount()) / 100) 
					* ((100.0 - self.clientLessVat()) / 100) 
    	);
    };
    
    ClientProductPriceForm.prototype.refreshProduct = function() {
    	var self = this;
    	
    	if(self.clientProductPriceFormModel.productId()) {
    		productService.getPartialProductFor(self.clientProductPriceFormModel.productId, self.clientProductPriceFormModel.clientId).done(function(partialProduct) {
	    		self.productFormattedPackageNetSellingPrice(partialProduct.formattedPackageNetSellingPrice);
	    		self.productSellingDiscount(partialProduct.sellingDiscount);
	    	});
    	}
    };
    
    ClientProductPriceForm.prototype.refreshClientCompanyPrice = function() {
    	var self = this;
    	
    	if(self.clientProductPriceFormModel.clientId() && self.clientProductPriceFormModel.productId()) {
	    	clientCompanyPriceService.getClientCompanyPriceByClientAndProduct(self.clientProductPriceFormModel.clientId, self.clientProductPriceFormModel.productId).done(function(clientCompanyPrice) {
	    		if(clientCompanyPrice) {
	    			self.clientCompanyPriceDiscount(clientCompanyPrice.discount);
	    		}
	    	});
    	}
    };
    
    ClientProductPriceForm.prototype.refreshClient = function() {
    	var self = this;
    	
    	if(self.clientProductPriceFormModel.clientId()) {
	    	userService.getUser(self.clientProductPriceFormModel.clientId).done(function(client) {
	    		self.clientLessVat(client.vatType.lessVat);
	    	});
    	}
    };
    
    ClientProductPriceForm.prototype.save = function() {
    	var self = this;
    	
    	clientProductPriceService.saveClientProductPrice(ko.toJSON(self.clientProductPriceFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    ClientProductPriceForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return ClientProductPriceForm;
});