define(['durandal/app', 'knockout', 'modules/purchaseorderservice', 'modules/purchaseorderitemservice', 'viewmodels/purchaseorder/additem'],
		function (app, ko, purchaseOrderService, purchaseOrderItemService, AddItem) {
    var PurchaseOrderPage = function() {
    	this.purchaseOrderItemList = ko.observable();
    	
    	this.warehouseEntity = null;
    	this.showCheckButton = ko.observable(false);
    	
    	this.purchaseOrder = {
			id: ko.observable(),
			
			companyId: ko.observable(),
			companyName: ko.observable(),
			warehouse: ko.observable(),
			creatorName: ko.observable(),
			formattedGrossTotal: ko.observable(),
			formattedDiscountTotal: ko.observable(),
			formattedNetTotal: ko.observable(),
			status: ko.observable()
    	};
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    PurchaseOrderPage.prototype.activate = function(purchaseOrderId) {
    	var self = this;
    	
    	self.purchaseOrder.id(purchaseOrderId);
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshPurchaseOrderItemList();
		});
		
		self.refreshPurchaseOrderItemList();
    };
    
    PurchaseOrderPage.prototype.refreshPurchaseOrderItemList = function() {
    	var self = this;
    	
    	purchaseOrderService.getPurchaseOrder(self.purchaseOrder.id()).done(function(purchaseOrder) {
    		self.purchaseOrder.companyId(purchaseOrder.company.id);
    		self.purchaseOrder.companyName(purchaseOrder.company.name);
    		self.purchaseOrder.warehouse(purchaseOrder.warehouse.displayName + ' - ' + purchaseOrder.warehouse.address);
    		self.purchaseOrder.creatorName(purchaseOrder.creator.formattedName);
    		self.purchaseOrder.formattedGrossTotal(purchaseOrder.formattedGrossTotal);
    		self.purchaseOrder.formattedDiscountTotal(purchaseOrder.formattedDiscountTotal);
    		self.purchaseOrder.formattedNetTotal(purchaseOrder.formattedNetTotal);
    		self.purchaseOrder.status(purchaseOrder.status.displayName);
    		
    		if(purchaseOrder.status.name == 'CREATING') self.showCheckButton(true);
    		else self.showCheckButton(false);
    		self.warehouseEntity = purchaseOrder.warehouse.name;
    	});
    	
    	purchaseOrderItemService.getPurchaseOrderItemList(self.currentPage(), self.purchaseOrder.id(), true).done(function(data) {
    		self.purchaseOrderItemList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    PurchaseOrderPage.prototype.add = function() {
    	var self = this;
    	
    	AddItem.show(self.purchaseOrder.id(), self.purchaseOrder.companyId(), self.warehouseEntity).done(function() {
    		self.refreshPurchaseOrderItemList();
    	});
    };
    
    PurchaseOrderPage.prototype.submit = function() {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to submit Purchase Order of <span class="text-primary">ID #' + self.purchaseOrder.id + '</span>?</p>'
    			+ '<p class="text-danger">Once submitted only the manager can edit the order.</p>',
				'<p class="text-primary">Submit for Evaluation</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				purchaseOrderService.submitPurchaseOrder(self.purchaseOrder.id).done(function(result) {
					self.refreshPurchaseOrderItemList();
					app.showMessage(result.message);
				});
			}
		})
    };
    
    PurchaseOrderPage.prototype.remove = function(purchaseOrderItemId, displayName) {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to remove Item <span class="text-primary">' + displayName + '</span>?</p>',
				'<p class="text-danger">Confirm Remove</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				purchaseOrderItemService.removeItem(purchaseOrderItemId).done(function(result) {
					self.refreshPurchaseOrderItemList();
					app.showMessage(result.message);
				});
			}
		})
    };
    
    PurchaseOrderPage.prototype.changePackageQuantity = function(purchaseOrderItemId, packageQuantity) {
    	var self = this;
    	
    	purchaseOrderItemService.changePackageQuantity(purchaseOrderItemId, packageQuantity).done(function(result) {
    		self.refreshPurchaseOrderItemList();
    		if(!result.success) {
    			app.showMessage(result.message);
    		}
    	});
    };
    
    PurchaseOrderPage.prototype.changePieceQuantity = function(purchaseOrderItemId, pieceQuantity) {
    	var self = this;
    	
    	purchaseOrderItemService.changePieceQuantity(purchaseOrderItemId, pieceQuantity).done(function(result) {
    		self.refreshPurchaseOrderItemList();
    		if(!result.success) {
    			app.showMessage(result.message);
    		}
    	});
    };
    
    return PurchaseOrderPage;
});