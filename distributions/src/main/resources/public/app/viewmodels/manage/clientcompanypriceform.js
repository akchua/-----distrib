define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/clientcompanypriceservice', 'modules/userservice', 'modules/companyservice'], 
			function (dialog, app, ko, clientCompanyPriceService, userService, companyService) {
    var ClientCompanyPriceForm = function(clientCompanyPrice, title) {
    	this.clientCompanyPrice = clientCompanyPrice;
    	this.title = title;
    	
    	this.clientList = ko.observable();
    	this.companyList = ko.observable();
    	
    	this.clientCompanyPriceFormModel = {
    		id: ko.observable(),
    		
    		clientId: ko.observable(),
    		companyId: ko.observable(),
    		discount: ko.observable(),
    		markup: ko.observable()
	    };
    };
    
    ClientCompanyPriceForm.prototype.activate = function() {
    	var self = this;
    	
    	self.clientCompanyPriceFormModel.id(self.clientCompanyPrice.id);
    	self.clientCompanyPriceFormModel.discount(self.clientCompanyPrice.discount);
    	self.clientCompanyPriceFormModel.markup(self.clientCompanyPrice.markup);
    	
    	userService.getFullClientList().done(function(clientList) {
			self.clientList(clientList);
			if(self.clientCompanyPrice.client) {
				self.clientCompanyPriceFormModel.clientId(self.clientCompanyPrice.client.id);
			}
		});
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    		if(self.clientCompanyPrice.company) {
    			self.clientCompanyPriceFormModel.companyId(self.clientCompanyPrice.company.id);
    		}
    	});
    };
    
    ClientCompanyPriceForm.show = function(clientCompanyPrice, title) {
    	return dialog.show(new ClientCompanyPriceForm(clientCompanyPrice, title));
    };
    
    ClientCompanyPriceForm.prototype.save = function() {
    	var self = this;
    	
    	clientCompanyPriceService.saveClientCompanyPrice(ko.toJSON(self.clientCompanyPriceFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    ClientCompanyPriceForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return ClientCompanyPriceForm;
});