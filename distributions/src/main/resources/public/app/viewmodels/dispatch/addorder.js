define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/dispatchservice', 'modules/clientorderservice', 'modules/userservice', 'viewmodels/clientorder/saleview', 'viewmodels/user/userview', 'viewmodels/clientorder/transfer'],
		function (dialog, app, ko, dispatchService, clientOrderService, userService, SaleView, UserView, Transfer) {
    var AddOrder = function(dispatchId, warehouseId) {
    	this.dispatchId = dispatchId;
    	this.warehouseId = warehouseId;
    	
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
    
    AddOrder.show = function(dispatchId, warehouseId) {
    	return dialog.show(new AddOrder(dispatchId, warehouseId));
    };
    
    AddOrder.prototype.refreshClientOrderList = function() {
    	var self = this;
    	
    	clientOrderService.getAcceptedClientOrderList(self.currentPage(), null, false).done(function(data) {
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
    	
    	clientOrderService.getPartialClientOrder(clientOrderId).done(function(partialClientOrder) {
    		SaleView.show(partialClientOrder)
    	});
    };
    
    AddOrder.prototype.viewClient = function(clientId) {
    	var self = this;
    	
    	userService.getUser(clientId).done(function(client) {
    		UserView.show(client);
    	});
    };
    
    AddOrder.prototype.transfer = function(clientOrderId) {
    	var self = this;
    	
    	Transfer.show(clientOrderId).done(function() {
    		self.refreshClientOrderList();
    	});
    };
    
    AddOrder.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return AddOrder;
});