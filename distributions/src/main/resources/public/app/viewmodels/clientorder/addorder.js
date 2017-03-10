define(['plugins/router', 'plugins/dialog', 'durandal/app', 'knockout', 'modules/companyservice', 'modules/clientorderservice'],
		function (router, dialog, app, ko, companyService, clientOrderService) {
    var AddOrder = function() {
    	this.companyList = ko.observable();
    	this.companyId = ko.observable();
    	
    	this.enableAdd = ko.observable(true);
    };
    
    AddOrder.prototype.activate = function() {
    	var self = this;
    	
    	companyService.getPartialCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    	});
    };
    
    AddOrder.show = function() {
    	return dialog.show(new AddOrder());
    };
    
    AddOrder.prototype.add = function() {
    	var self = this;

    	self.enableAdd(false);
    	clientOrderService.addClientOrder(self.companyId()).done(function(result) {
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