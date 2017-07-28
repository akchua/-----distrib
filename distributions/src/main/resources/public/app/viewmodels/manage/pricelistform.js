define(['plugins/router', 'plugins/dialog', 'durandal/app', 'knockout', 'modules/productservice', 'modules/companyservice', 'modules/fileservice'],
		function (router, dialog, app, ko, productService, companyService, fileService) {
    var PriceListForm = function() {
    	this.companyList = ko.observable();
    	this.companyId = ko.observable();
    	
    	this.sendEmail = ko.observable(false);
    	this.downloadFile = ko.observable(true);
    	
    	this.enableGenerateButton = ko.observable(true);
    };
    
    PriceListForm.prototype.activate = function() {
    	var self = this;
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    	});
    };
    
    PriceListForm.show = function() {
    	return dialog.show(new PriceListForm());
    };
    
    PriceListForm.prototype.generate = function() {
    	var self = this;

    	self.enableGenerateButton(false);
    	productService.generatePriceList(self.companyId(), self.sendEmail()).done(function(result) {
    		if(result.success) {
    			if(self.downloadFile()) {
    				fileService.downloadPriceListByFileName(result.extras.fileName);
    			}
    			dialog.close(self);
    		}
    		
    		self.enableGenerateButton(true);
        	app.showMessage(result.message);
    	});
    };
    
    PriceListForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return PriceListForm;
});