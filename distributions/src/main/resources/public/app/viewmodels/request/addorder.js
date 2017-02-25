define(['plugins/router', 'plugins/dialog', 'durandal/app', 'knockout', 'modules/userservice', 'modules/clientorderservice'],
		function (router, dialog, app, ko, userService, clientOrderService) {
    var AddOrder = function() {
    	this.clientList = ko.observable();
    	this.clientId = ko.observable();
    	
    	this.enableAdd = ko.observable(true);
    };
    
    AddOrder.prototype.activate = function() {
    	var self = this;
    	
    	userService.getFullClientList().done(function(clientList) {
			self.clientList(clientList);
		});
    };
    
    AddOrder.show = function() {
    	return dialog.show(new AddOrder());
    };
    
    AddOrder.prototype.add = function() {
    	var self = this;
    	
    	clientOrderService.addClientOrderFor(self.clientId()).done(function(result) {
    		self.enableAdd(false);
    		if(result.success) {
        		dialog.close(self);	
        	}
    		self.enableAdd(true);
			app.showMessage(result.message);
    	});
    };
    
    AddOrder.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return AddOrder;
});