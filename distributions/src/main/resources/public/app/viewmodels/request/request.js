define(['durandal/app', 'knockout', 'modules/clientorderservice', 'modules/userservice', 'viewmodels/clientorder/saleview', 'viewmodels/user/userview', 'viewmodels/clientorder/transfer'],
		function (app, ko, clientOrderService, userService, SaleView, UserView, Transfer) {
    var Request = function() {
    	this.clientOrderList = ko.observable();
    	
    	this.showAccepted = ko.observable(false);
    	this.enableAccept = ko.observable(true);
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    Request.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshClientOrderList();
		});
		
		self.refreshClientOrderList();
    };
    
    Request.prototype.refreshClientOrderList = function() {
    	var self = this;
    	
    	clientOrderService.getClientOrderRequestList(self.currentPage(), self.showAccepted()).done(function(data) {
    		self.clientOrderList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    Request.prototype.viewClient = function(clientId) {
    	var self = this;
    	
    	userService.getUser(clientId).done(function(client) {
    		UserView.show(client)
    	});
    };
    
    Request.prototype.accept = function(clientOrderId) {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to accept Order of <span class="text-primary">ID #' + clientOrderId + '</span>?</p>',
				'<p class="text-primary">Confirm Accept</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				self.enableAccept(false);
        		clientOrderService.acceptClientOrder(clientOrderId).done(function(result) {
        			self.refreshClientOrderList();
                	app.showMessage(result.message);
                	self.enableAccept(true);
        		});
			}
		})
    };
    
    Request.prototype.open = function(clientOrderId) {
    	router.navigate('#clientorderpage/' + clientOrderId);
    };
    
    Request.prototype.transfer = function(clientOrderId) {
    	var self = this;
    	
    	Transfer.show(clientOrderId).done(function() {
    		self.refreshClientOrderList();
    	});
    };
    
    /*Request.prototype.changeWarehouse = function(clientOrderId) {
    	var self = this;
    	
    	clientOrderService.getClientOrder(clientOrderId).done(function(clientOrder) {
    		Accept.show(clientOrder, 'Change Warehouse').done(function() {
    			self.refreshClientOrderList();
    		});
    	});
    };*/
    
    Request.prototype.view = function(clientOrderId) {
    	var self = this;
    	
    	clientOrderService.getPartialClientOrder(clientOrderId).done(function(partialClientOrder) {
    		SaleView.show(partialClientOrder)
    	});
    };
    
    Request.prototype.remove = function(clientOrderId) {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to remove Order of <span class="text-primary">ID #' + clientOrderId + '</span>?</p>',
				'<p class="text-danger">Confirm Remove</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				clientOrderService.removeClientOrder(clientOrderId).done(function(result) {
					self.refreshClientOrderList();
					app.showMessage(result.message);
				});
			}
		})
    };
    
    return Request;
});