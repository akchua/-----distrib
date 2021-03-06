define(['durandal/app', 'knockout', 'modules/clientorderservice', 'modules/dispatchservice', 'modules/userservice', 'viewmodels/clientorder/saleview', 'viewmodels/dispatch/addorder', 'viewmodels/user/userview'],
		function (app, ko, clientOrderService, dispatchService, userService, SaleView, AddOrder, UserView) {
    var DispatchPage = function() {
    	this.dispatchItemList = ko.observable();
    	
    	this.showDispatchButton = ko.observable(false);
    	this.enableDispatch = ko.observable(true);
    	
    	this.dispatch = {
			id: ko.observable(),
			
			warehouseId: ko.observable(),
			warehouseName: ko.observable(),
			orderCount: ko.observable(),
			totalAmount: ko.observable(),
			status: ko.observable()
    	};
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    DispatchPage.prototype.activate = function(dispatchId) {
    	var self = this;
    	
    	self.dispatch.id(dispatchId);
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshDispatchItemList();
		});
		
		self.refreshDispatchItemList();
    };
    
    DispatchPage.prototype.refreshDispatchItemList = function() {
    	var self = this;
    	
    	dispatchService.getDispatch(self.dispatch.id()).done(function(dispatch) {
    		self.dispatch.warehouseId(dispatch.warehouse.id);
    		self.dispatch.warehouseName(dispatch.warehouse.name + ' - ' + dispatch.warehouse.address);
    		self.dispatch.orderCount(dispatch.orderCount);
    		self.dispatch.totalAmount(dispatch.formattedTotalAmount);
    		self.dispatch.status(dispatch.status.displayName);
    		
    		if(dispatch.status.name == 'CREATING') self.showDispatchButton(true);
    		else self.showDispatchButton(false);
    	});
    	
    	dispatchService.getDispatchItemList(self.currentPage(), self.dispatch.id()).done(function(data) {
    		self.dispatchItemList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    DispatchPage.prototype.add = function() {
    	var self = this;
    	
    	AddOrder.show(self.dispatch.id(), self.dispatch.warehouseId()).done(function() {
    		self.refreshDispatchItemList();
    	});
    };
    
    DispatchPage.prototype.dispatchNow = function() {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to send dispatch of <span class="text-primary">ID #' + self.dispatch.id() + '</span>?</p>'
    			+ '<p class="text-danger">Once dispatched you cannot add any more orders in this dispatch.</p>',
				'<p class="text-primary">Confirm Dispatch</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				self.enableDispatch(false);
				dispatchService.dispatch(self.dispatch.id()).done(function(result) {
					self.refreshDispatchItemList();
					app.showMessage(result.message);
					self.enableDispatch(true);
				});
			}
		})
    };
    
    DispatchPage.prototype.view = function(clientOrderId) {
    	var self = this;
    	
    	clientOrderService.getPartialClientOrder(clientOrderId).done(function(partialClientOrder) {
    		SaleView.show(partialClientOrder)
    	});
    };
    
    DispatchPage.prototype.viewClient = function(clientId) {
    	var self = this;
    	
    	userService.getUser(clientId).done(function(client) {
    		UserView.show(client);
    	});
    };
    
    DispatchPage.prototype.remove = function(clientOrderId, dispatchItemId) {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to remove Order of <span class="text-primary">ID #' + clientOrderId + '</span> from this dispatch?</p>',
				'<p class="text-danger">Confirm Remove</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				dispatchService.removeItem(dispatchItemId).done(function(result) {
					self.refreshDispatchItemList();
					app.showMessage(result.message);
				});
			}
		})
    };
    
    return DispatchPage;
});