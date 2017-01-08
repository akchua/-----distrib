define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/clientorderservice'],
		function (dialog, app, ko, clientOrderService) {
    var Accept = function(clientOrder, title) {
    	this.clientOrder = clientOrder;
    	this.title = title;
    	
    	this.warehouseList = ko.observable();
    	
    	this.enableAccept = ko.observable(true);
    	
    	this.acceptFormModel = {
    		id: ko.observable(),
    		
    		warehouse: ko.observable()
	    };
    };
    
    Accept.prototype.activate = function() {
    	var self = this;
    	
    	self.acceptFormModel.id(self.clientOrder.id);
    	if(self.clientOrder.warehouse) self.acceptFormModel.warehouse(self.clientOrder.warehouse.name);
    	
    	clientOrderService.getWarehouseList().done(function(warehouseList) {
			self.warehouseList(warehouseList);
			if(self.clientOrder.warehouse) self.acceptFormModel.warehouse(self.clientOrder.warehouse.name);
		});
    };
    
    Accept.show = function(clientOrder, title) {
    	return dialog.show(new Accept(clientOrder, title));
    };
    
    Accept.prototype.accept = function() {
    	var self = this;
    	
        clientOrderService.testAcceptClientOrder(self.acceptFormModel.id(), self.acceptFormModel.warehouse()).done(function(test) {
        	if(test.success) {
        		self.enableAccept(false);
        		clientOrderService.acceptClientOrder(self.acceptFormModel.id(), self.acceptFormModel.warehouse()).done(function(result) {
        			if(result.success) {
                		dialog.close(self);	
                	} 
                	app.showMessage(result.message);
                	self.enableAccept(true);
        		});
        	} else if(test.message) {
        		app.showMessage(test.message + '<br/><p>Adjust order to available stocks or Continue as it is?</p>',
        				'<p class="text-danger">Choose Action</p>',
        				[{ text: 'Adjust', value: 1 }, { text: 'Continue', value: 2 }, { text: 'Cancel', value: 3 }])
        		.then(function(reply) {
        			switch(reply) {
        			case 1:
        				clientOrderService.adjustAndAcceptClientOrder(self.acceptFormModel.id(), self.acceptFormModel.warehouse()).done(function(result) {
                			if(result.success) {
                        		dialog.close(self);
                        		dialog.close(self);
                        	} 
                        	app.showMessage(result.message);
                		});
        				break;
        			case 2:
        				clientOrderService.acceptClientOrder(self.acceptFormModel.id(), self.acceptFormModel.warehouse()).done(function(result) {
                			if(result.success) {
                        		dialog.close(self);
                        		dialog.close(self);
                        	} 
                        	app.showMessage(result.message);
                		});
        				break;
        			case 3:
        				break;
        			}
        		})
        	} else {
        		app.showMessage("<p class=\"text-danger\">Please select a warehouse.</p>");
        	}
        });
    };
    
    Accept.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return Accept;
});