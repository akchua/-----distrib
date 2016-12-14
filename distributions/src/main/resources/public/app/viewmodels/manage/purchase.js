define(['plugins/router', 'durandal/app', 'knockout', 'modules/purchaseorderservice', 'modules/companyservice', 'viewmodels/purchaseorder/purchaseview'],
		function (router, app, ko, purchaseOrderService, companyService, PurchaseView) {
    var Purchase = function() {
    	this.purchaseOrderList = ko.observable();
    	this.companyList = ko.observable();
    	this.warehouseList = ko.observable();
    	
    	this.companyId = ko.observable();
    	this.warehouse = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    Purchase.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshPurchaseOrderList();
		});
		
		purchaseOrderService.getWarehouseList().done(function(warehouseList) {
			self.warehouseList(warehouseList);
		});
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    	});
		
		self.refreshPurchaseOrderList();
    };
    
    Purchase.prototype.refreshPurchaseOrderList = function() {
    	var self = this;
    	
    	purchaseOrderService.getPaidPurchaseOrderList(self.currentPage(), self.companyId(), self.warehouse()).done(function(data) {
    		self.purchaseOrderList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    Purchase.prototype.generateReport = function() {
    	var self = this;
    	
    	alert('not yet functional');
    };
    
    Purchase.prototype.view = function(purchaseOrderId) {
    	var self = this;
    	
    	purchaseOrderService.getPurchaseOrder(purchaseOrderId).done(function(purchaseOrder) {
    		PurchaseView.show(purchaseOrder)
    	});
    };
    
    return Purchase;
});