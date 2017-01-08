define(['plugins/router', 'durandal/app', 'knockout', 'modules/purchaseorderservice', 'modules/companyservice', 'viewmodels/purchaseorder/purchaseorderform', 'viewmodels/purchaseorder/purchaseview'],
		function (router, app, ko, purchaseOrderService, companyService, PurchaseOrderForm, PurchaseView) {
    var PurchaseOrder = function() {
    	this.purchaseOrderList = ko.observable();
    	this.companyList = ko.observable();
    	this.warehouseList = ko.observable();
    	
    	this.companyId = ko.observable();
    	this.warehouse = ko.observable();
    	
    	this.enableSend = ko.observable(true);
    	
    	this.showPaid = ko.observable(false);
    	
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
		
		purchaseOrderService.getWarehouseList().done(function(warehouseList) {
			self.warehouseList(warehouseList);
		});
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    	});
		
		self.refreshPurchaseOrderList();
    };
    
    PurchaseOrder.prototype.refreshPurchaseOrderList = function() {
    	var self = this;
    	
    	purchaseOrderService.getPurchaseOrderList(self.currentPage(), self.companyId(), self.warehouse(), self.showPaid()).done(function(data) {
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
    
    PurchaseOrder.prototype.send = function(purchaseOrderId, companyName, emailAddress) {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to send Purchase Order of <span class="text-primary">ID #' + purchaseOrderId + '</span> to <span class="text-primary">' + emailAddress + "</span> ?</p>",
				'<p>Send Order Email to <span class="text-primary">' + companyName + '</span></p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				self.enableSend(false);
				purchaseOrderService.sendPurchaseOrder(purchaseOrderId).done(function(result) {
					self.refreshPurchaseOrderList();
					app.showMessage(result.message);
					self.enableSend(true);
				});
			}
		})
    };
    
    PurchaseOrder.prototype.receive = function(purchaseOrderId) {
    	var self = this;
    	
    	app.showMessage('<p>Receive Purchase Order of <span class="text-primary">ID #' + purchaseOrderId + '?</p>'
    			+ '<p>Make sure that all items in this order have been received.</p>',
				'<p>Confirm and Pay Purchase Order</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				purchaseOrderService.receivePurchaseOrder(purchaseOrderId).done(function(result) {
					self.refreshPurchaseOrderList();
					app.showMessage(result.message);
				});
			}
		})
    };
    
    PurchaseOrder.prototype.pay = function(purchaseOrderId, formattedNetTotal) {
    	var self = this;
    	
    	app.showMessage('<p>Mark Purchase Order of <span class="text-primary">ID #' + purchaseOrderId + '</span> with total amount of <span class="text-success">' + formattedNetTotal + '</span> as paid?</p>',
				'<p>Mark Purchase Order as Paid</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				purchaseOrderService.payPurchaseOrder(purchaseOrderId).done(function(result) {
					self.refreshPurchaseOrderList();
					app.showMessage(result.message);
				});
			}
		})
    };
    
    PurchaseOrder.prototype.open = function(purchaseOrderId) {
    	router.navigate('#purchaseorderpage/' + purchaseOrderId);
    };
    
    PurchaseOrder.prototype.edit = function(purchaseOrderId) {
    	var self = this;
    	
    	purchaseOrderService.getPurchaseOrder(purchaseOrderId).done(function(purchaseOrder) {
    		PurchaseOrderForm.show(purchaseOrder, 'Edit Purchase Order').done(function() {
    			self.refreshPurchaseOrderList();
    		});
    	});
    };
    
    PurchaseOrder.prototype.view = function(purchaseOrderId) {
    	var self = this;
    	
    	purchaseOrderService.getPurchaseOrder(purchaseOrderId).done(function(purchaseOrder) {
    		PurchaseView.show(purchaseOrder)
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