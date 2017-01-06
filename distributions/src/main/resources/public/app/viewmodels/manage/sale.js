define(['plugins/router', 'durandal/app', 'knockout', 'modules/clientorderservice', 'viewmodels/manage/salereport', 'viewmodels/clientorder/saleview'],
		function (router, app, ko, clientOrderService, SaleReport, SaleView) {
    var Sale = function() {
    	this.clientOrderList = ko.observable();
    	this.warehouseList = ko.observable();
    	
    	this.warehouse = ko.observable();
    	
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
		
		self.refreshClientOrderList();
    };
    
    Sale.prototype.refreshClientOrderList = function() {
    	var self = this;
    	
    	clientOrderService.getPaidClientOrderList(self.currentPage(), self.warehouse()).done(function(data) {
    		self.clientOrderList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    Sale.prototype.generateReport = function() {
    	var self = this;
    	
    	//SaleReport.show()
    	alert('user triggered report is not yet working')
    };
    
    Sale.prototype.view = function(clientOrderId) {
    	var self = this;
    	
    	clientOrderService.getClientOrder(clientOrderId).done(function(clientOrder) {
    		SaleView.show(clientOrder)
    	});
    };
    
    return Sale;
});