define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/productservice', 'modules/companyservice'], 
		function (dialog, app, ko, productService, companyService) {
    var MassPriceChangeForm = function() {
    	this.title = 'Mass Price Change Form';
    	
    	this.companyList = ko.observable();
    	
    	this.massPriceChangeModel = {
    		companyId : ko.observable(),
    		includeString : ko.observable(),
    		excludeString : ko.observable(),
    		
    		percentGrossIncrease : ko.observable(),
    		percentSellingIncrease : ko.observable()
	    };
    };
    
    MassPriceChangeForm.prototype.activate = function() {
    	var self = this;
    	
    	self.massPriceChangeModel.includeString('');
    	self.massPriceChangeModel.excludeString('');
    	self.massPriceChangeModel.percentGrossIncrease(0);
    	self.massPriceChangeModel.percentSellingIncrease(0);
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    		self.productFormModel.companyId(self.product.company.id);
    	});
    };
    
    MassPriceChangeForm.show = function() {
    	return dialog.show(new MassPriceChangeForm());
    };
    
    MassPriceChangeForm.prototype.apply = function() {
    	var self = this;
    	
        productService.massPriceChange(ko.toJSON(self.massPriceChangeModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    MassPriceChangeForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return MassPriceChangeForm;
});