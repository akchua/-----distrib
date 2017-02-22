define(['plugins/router', 'durandal/app', 'knockout', 'modules/clientorderservice', 'modules/userservice', 'viewmodels/report/salereport', 'viewmodels/report/timestampview', 'viewmodels/clientorder/saleview', 'viewmodels/user/userview'],
		function (router, app, ko, clientOrderService, userService, SaleReport, TimestampView, SaleView, UserView) {
    var Sale = function() {
    	this.clientOrderList = ko.observable();
    	this.warehouseList = ko.observable();
    	this.clientList = ko.observable();
    	
    	this.salesReportQuery = {
    		includePaid: ko.observable(true),
    		includeDelivered: ko.observable(true),
    		includeDispatched: ko.observable(false),
    		includeToFollow: ko.observable(false),
    		includeAccepted: ko.observable(false),
    		includeSubmitted: ko.observable(false),
    		includeCreating: ko.observable(false)
	    };
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    Sale.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshClientOrderList();
		});
		
		clientOrderService.getWarehouseList().done(function(warehouseList) {
			self.warehouseList(warehouseList);
		});
		
		userService.getFullClientList().done(function(clientList) {
			self.clientList(clientList);
		});
		
		self.refreshClientOrderList();
    };
    
    Sale.prototype.refreshClientOrderList = function() {
    	var self = this;
    	
    	clientOrderService.getClientOrderListByReportQuery(self.currentPage(), ko.toJSON(self.salesReportQuery)).done(function(data) {
    		self.clientOrderList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    Sale.prototype.generateReport = function() {
    	var self = this;
    	
    	SaleReport.show()
    };
    
    Sale.prototype.timestamp = function(clientOrderId) {
    	var self = this;
    	
    	clientOrderService.getClientOrder(clientOrderId).done(function(clientOrder) {
    		TimestampView.show('Sale Timestamps', clientOrder);
    	});
    };
    
    Sale.prototype.view = function(clientOrderId) {
    	var self = this;
    	
    	clientOrderService.getClientOrder(clientOrderId).done(function(clientOrder) {
    		SaleView.show(clientOrder)
    	});
    };
    
    Sale.prototype.viewClient = function(clientId) {
    	var self = this;
    	
    	userService.getUser(clientId).done(function(client) {
    		UserView.show(client);
    	});
    };
    
    return Sale;
});