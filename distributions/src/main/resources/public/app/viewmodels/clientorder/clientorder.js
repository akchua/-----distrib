define(['plugins/router', 'durandal/app', 'knockout', 'modules/clientorderservice', 'viewmodels/clientorder/addorder', 'viewmodels/clientorder/saleview', 'viewmodels/report/timestampview'],
		function (router, app, ko, clientOrderService, AddOrder, SaleView, TimestampView) {
    var ClientOrder = function() {
    	this.partialClientOrderList = ko.observable();

    	this.showPaid = ko.observable(false);
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    ClientOrder.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshClientOrderList();
		});
		
		self.refreshClientOrderList();
    };
    
    ClientOrder.prototype.refreshClientOrderList = function() {
    	var self = this;
    	
    	clientOrderService.getPartialClientOrderList(self.currentPage(), self.showPaid()).done(function(data) {
    		self.partialClientOrderList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    ClientOrder.prototype.add = function() {
    	var self = this;
    	
    	AddOrder.show().done(function() {
    		self.refreshClientOrderList();
    	});
    };
    
    ClientOrder.prototype.open = function(clientOrderId) {
    	router.navigate('#clientorderpage/' + clientOrderId);
    };
    
    ClientOrder.prototype.timestamp = function(clientOrderId) {
    	var self = this;
    	
    	clientOrderService.getPartialClientOrder(clientOrderId).done(function(clientOrder) {
    		TimestampView.show('Sale Timestamps', clientOrder);
    	});
    };
    
    ClientOrder.prototype.view = function(clientOrderId) {
    	clientOrderService.getPartialClientOrder(clientOrderId).done(function(clientOrder) {
    		SaleView.show(clientOrder)
    	});
    };
    
    ClientOrder.prototype.remove = function(clientOrderId) {
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
    
    return ClientOrder;
});