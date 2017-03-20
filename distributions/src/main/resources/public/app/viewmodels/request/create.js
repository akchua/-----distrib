define(['plugins/router', 'durandal/app', 'knockout', 'modules/clientorderservice', 'modules/userservice', 'viewmodels/request/addorder', 'viewmodels/clientorder/saleview', 'viewmodels/user/userview'],
		function (router, app, ko, clientOrderService, userService, AddOrder, SaleView, UserView) {
    var Create = function() {
    	this.clientOrderList = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    Create.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshClientOrderList();
		});
		
		self.refreshClientOrderList();
    };
    
    Create.prototype.refreshClientOrderList = function() {
    	var self = this;
    	
    	clientOrderService.getClientOrderRequestByCurrentUserList(self.currentPage()).done(function(data) {
    		self.clientOrderList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    Create.prototype.add = function() {
    	var self = this;
    	
    	AddOrder.show().done(function() {
    		self.refreshClientOrderList();
    	});
    };
    
    Create.prototype.viewClient = function(clientId) {
    	var self = this;
    	
    	userService.getUser(clientId).done(function(client) {
    		UserView.show(client)
    	});
    };
    
    Create.prototype.open = function(clientOrderId) {
    	router.navigate('#clientorderpage/' + clientOrderId);
    };
    
    Create.prototype.view = function(clientOrderId) {
    	var self = this;
    	
    	clientOrderService.getPartialClientOrder(clientOrderId).done(function(partialClientOrder) {
    		SaleView.show(partialClientOrder)
    	});
    };
    
    Create.prototype.remove = function(clientOrderId) {
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
    
    return Create;
});