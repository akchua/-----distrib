define(['plugins/router', 'plugins/dialog', 'durandal/app', 'knockout', 'modules/userservice', 'modules/companyservice', 'modules/clientorderservice'],
		function (router, dialog, app, ko, userService, companyService, clientOrderService) {
    var AddOrder = function() {
    	this.clientList = ko.observable();
    	this.companyList = ko.observable();
    	
    	this.clientId = ko.observable();
    	this.companyId = ko.observable();
    	
    	this.enableAdd = ko.observable(true);
    };
    
    AddOrder.prototype.activate = function() {
    	var self = this;
    	
    	userService.getFullClientList().done(function(clientList) {
			self.clientList(clientList);
		});
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    	});
    };
    
    AddOrder.show = function() {
    	return dialog.show(new AddOrder());
    };
    
    AddOrder.prototype.add = function() {
    	var self = this;
    	
    	self.enableAdd(false);
    	clientOrderService.addClientOrder(self.companyId(), self.clientId()).done(function(result) {
    		if(result.success) {
        		dialog.close(self);	
        		router.navigate('#clientorderpage/' + result.extras.clientOrderId);
        	} else {
	    		self.enableAdd(true);
				app.showMessage(result.message);
        	}
    	});
    };
    
    AddOrder.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return AddOrder;
});