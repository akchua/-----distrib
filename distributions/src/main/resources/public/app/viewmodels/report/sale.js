define(['plugins/router', 'durandal/app', 'knockout', 'modules/clientorderservice', 'modules/warehouseservice', 'modules/userservice', 'modules/companyservice', 'viewmodels/report/salereport', 'viewmodels/report/clientrankingquery', 'viewmodels/report/timestampview', 'viewmodels/clientorder/saleview', 'viewmodels/user/userview'],
		function (router, app, ko, clientOrderService, warehouseService, userService, companyService, SaleReport, ClientRankingQuery, TimestampView, SaleView, UserView) {
    var Sale = function() {
    	this.clientOrderList = ko.observable();
    	this.clientList = ko.observable();
    	this.companyList = ko.observable();
    	this.warehouseList = ko.observable();
    	
    	this.salesReportQuery = {
    		clientId: ko.observable(),
    		companyId: ko.observable(),
    		warehouseId: ko.observable(),
    		
    		clientSalesReportType: ko.observable("STATUS_BASED"),
    		
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
		
		userService.getFullClientList().done(function(clientList) {
			self.clientList(clientList);
		});
		
		companyService.getCompanyListByName().done(function(companyList) {
			self.companyList(companyList);
		});
		
		warehouseService.getWarehouseListByName().done(function(warehouseList) {
			self.warehouseList(warehouseList);
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
    
    Sale.prototype.ranking = function() {
    	ClientRankingQuery.show()
    };
    
    Sale.prototype.timestamp = function(clientOrderId) {
    	var self = this;
    	
    	clientOrderService.getClientOrder(clientOrderId).done(function(clientOrder) {
    		TimestampView.show('Sale Timestamps', clientOrder);
    	});
    };
    
    Sale.prototype.view = function(clientOrderId) {
    	var self = this;
    	
    	clientOrderService.getPartialClientOrder(clientOrderId).done(function(partialClientOrder) {
    		SaleView.show(partialClientOrder)
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