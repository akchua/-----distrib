define(['plugins/router', 'durandal/app', 'knockout', 'modules/purchaseorderservice', 'modules/warehouseservice', 'modules/companyservice', 'viewmodels/report/timestampview', 'viewmodels/purchaseorder/purchaseview'],
		function (router, app, ko, purchaseOrderService, warehouseService, companyService, TimestampView, PurchaseView) {
    var Purchase = function() {
    	this.purchaseOrderList = ko.observable();
    	this.companyList = ko.observable();
    	this.warehouseList = ko.observable();
    	
    	this.purchaseReportQuery = {
    		from: ko.observable(),
    		to: ko.observable(),
    		warehouseId: ko.observable(),
    		companyId: ko.observable(),
    		includePaid: ko.observable(true),
    		includeDelivered: ko.observable(true),
    		includeToFollow: ko.observable(false),
    		includeAccepted: ko.observable(false),
    		includeSubmitted: ko.observable(false),
    		includeCreating: ko.observable(false),
    		showNetTrail: ko.observable()
	    };
    	
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
		
		warehouseService.getWarehouseListByName().done(function(warehouseList) {
			self.warehouseList(warehouseList);
		});
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    	});
		
		self.refreshPurchaseOrderList();
    };
    
    Purchase.prototype.refreshPurchaseOrderList = function() {
    	var self = this;
    	
    	purchaseOrderService.getPurchaseOrderListByReportQuery(self.currentPage(), ko.toJSON(self.purchaseReportQuery)).done(function(data) {
    		self.purchaseOrderList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    Purchase.prototype.generateReport = function() {
    	var self = this;
    	
    	alert('not yet functional');
    };
    
    Purchase.prototype.timestamp = function(purchaseOrderId) {
    	var self = this;
    	
    	purchaseOrderService.getPurchaseOrder(purchaseOrderId).done(function(purchaseOrder) {
    		TimestampView.show('Purchase Timestamps', purchaseOrder);
    	});
    };
    
    Purchase.prototype.view = function(purchaseOrderId) {
    	var self = this;
    	
    	purchaseOrderService.getPurchaseOrder(purchaseOrderId).done(function(purchaseOrder) {
    		PurchaseView.show(purchaseOrder)
    	});
    };
    
    return Purchase;
});