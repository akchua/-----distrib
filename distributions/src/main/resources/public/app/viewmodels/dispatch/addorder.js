define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/dispatchservice', 'modules/clientorderservice', 'modules/userservice', 'viewmodels/clientorder/saleview', 'viewmodels/user/userview'],
		function (dialog, app, ko, dispatchService, clientOrderService, userService, SaleView, UserView) {
    var AddOrder = function(dispatchId, warehouse) {
    	this.dispatchId = dispatchId;
    	this.warehouse = warehouse;
    	
    	this.clientOrderList = ko.observable();
    	
    	this.log = ko.observable('<p class="text-info">Click the plus beside the order to add the order to this dispatch.</p>');
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    AddOrder.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshClientOrderList();
		});
		
		self.refreshClientOrderList();
    };
    
    AddOrder.show = function(dispatchId, warehouse) {
    	return dialog.show(new AddOrder(dispatchId, warehouse));
    };
    
    AddOrder.prototype.refreshClientOrderList = function() {
    	var self = this;
    	
    	clientOrderService.getAcceptedClientOrderList(self.currentPage(), self.warehouse, false).done(function(data) {
    		self.clientOrderList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    AddOrder.prototype.addItem = function(clientOrderId) {
    	var self = this;
    	
    	dispatchService.addItem(clientOrderId, self.dispatchId).done(function(result) {
    		if(result.success) {
    			self.log(result.message);
        	} else {
        		app.showMessage(result.message);
        	}
    		self.refreshClientOrderList();
    	});
    };
    
    AddOrder.prototype.view = function(clientOrderId) {
    	var self = this;
    	
    	clientOrderService.getClientOrder(clientOrderId).done(function(clientOrder) {
    		SaleView.show(clientOrder)
    	});
    };
    
    AddOrder.prototype.viewClient = function(clientId) {
    	var self = this;
    	
    	userService.getUser(clientId).done(function(client) {
    		UserView.show(client);
    	});
    };
    
    AddOrder.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return AddOrder;
});