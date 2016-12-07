define(['durandal/app', 'knockout', 'modules/purchaseorderservice', 'modules/companyservice', 'viewmodels/purchaseorder/purchaseorderform'],
		function (app, ko, purchaseOrderService, companyService, PurchaseOrderForm) {
    var PurchaseOrder = function() {
    	this.purchaseOrderList = ko.observable();
    	this.companyList = ko.observable();
    	this.areaList = ko.observable();
    	
    	this.companyId = ko.observable();
    	this.area = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    PurchaseOrder.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshPurchaseOrderList();
		});
		
		purchaseOrderService.getAreaList().done(function(areaList) {
			self.areaList(areaList);
		});
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    	});
		
		self.refreshPurchaseOrderList();
    };
    
    PurchaseOrder.prototype.refreshPurchaseOrderList = function() {
    	var self = this;
    	
    	purchaseOrderService.getPurchaseOrderList(self.currentPage(), self.companyId(), self.area()).done(function(data) {
    		self.purchaseOrderList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    PurchaseOrder.prototype.add = function() {
    	var self = this;
    	
    	PurchaseOrderForm.show(new Object(), 'Create Purchase Order').done(function() {
    		self.refreshPurchaseOrderList();
    	});
    };
    
    PurchaseOrder.prototype.edit = function(purchaseOrderId) {
    	var self = this;
    	
    	purchaseOrderService.getPurchaseOrder(purchaseOrderId).done(function(purchaseOrder) {
    		PurchaseOrderForm.show(purchaseOrder, 'Edit Purchase Order').done(function() {
    			self.refreshPurchaseOrderList();
    		});
    	});
    };
    
    PurchaseOrder.prototype.remove = function(purchaseOrderId) {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to remove Purchase Order of <span class="text-primary">ID #' + purchaseOrderId + '</span>?</p>',
				'<p class="text-danger">Confirm Remove</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				purchaseOrderService.removePurchaseOrder(purchaseOrderId).done(function(result) {
					self.refreshPurchaseOrderList();
					app.showMessage(result.message);
				});
			}
		})
    };
    
    return PurchaseOrder;
});