define(['plugins/router', 'durandal/app', 'knockout', 'modules/clientorderservice', 'viewmodels/clientorder/saleview'],
		function (router, app, ko, clientOrderService, SaleView) {
    var Payment = function() {
    	this.clientOrderList = ko.observable();
    	this.warehouseList = ko.observable();
    	
    	this.warehouse = ko.observable();
    	this.formattedCollectible = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    Payment.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshClientOrderList();
		});
		
		clientOrderService.getWarehouseList().done(function(warehouseList) {
			self.warehouseList(warehouseList);
		});
		
		self.refreshClientOrderList();
		self.refreshTotalPayable();
    };
    
    Payment.prototype.refreshClientOrderList = function() {
    	var self = this;
    	
    	clientOrderService.getReceivedClientOrderList(self.currentPage(), self.warehouse()).done(function(data) {
    		self.clientOrderList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    Payment.prototype.refreshTotalPayable = function() {
    	var self = this;
    	
    	clientOrderService.getFormattedTotalCollectible().done(function(formattedCollectible) {
    		self.formattedCollectible(formattedCollectible.content);
    	});
    };
    
    Payment.prototype.pay = function(clientOrderId, formattedNetTotal) {
    	var self = this;
    	
    	app.showMessage('<p>Finalize and receive payment of Sales Order <span class="text-primary">ID #' + clientOrderId + '</span> with total amount of <span class="text-success">' + formattedNetTotal + '</span>?</p>',
				'<p>Confirm and Receive Payment</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				clientOrderService.payClientOrder(clientOrderId).done(function(result) {
					self.refreshClientOrderList();
					self.refreshTotalPayable();
					app.showMessage(result.message);
				});
			}
		})
    };
    
    Payment.prototype.view = function(clientOrderId) {
    	var self = this;
    	
    	clientOrderService.getClientOrder(clientOrderId).done(function(clientOrder) {
    		SaleView.show(clientOrder)
    	});
    };
    
    return Payment;
});