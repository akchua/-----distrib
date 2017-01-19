define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/purchaseorderservice', 'modules/purchaseorderitemservice'],
		function (dialog, app, ko, purchaseOrderService, purchaseOrderItemService) {
	
	var Transfer = function(sourceId) {
    	this.sourceId = sourceId;
    	this.sourceOrderItemList = ko.observable();
    	this.destinationOrderItemList = ko.observable();
    	
    	this.log = ko.observable('<p class="text-info">Click any button beside the product to transfer that product.</p>');
    	
    	this.sourceOrder = {
			id: ko.observable(),
			company: ko.observable(),
			warehouse: ko.observable(),
			status: ko.observable(),
			netTotal: ko.observable()
    	};
    	
    	this.destinationOrder = {
    		id: ko.observable(),
    		status: ko.observable()
    	};
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
    	
		this.sourceTotalItems = ko.observable();
		this.sourceCurrentPage = ko.observable(1);
		this.sourceCurrentPageSubscription = null;
		
		this.destinationTotalItems = ko.observable();
		this.destinationCurrentPage = ko.observable(1);
		this.destinationCurrentPageSubscription = null;
    };
    
    Transfer.prototype.activate = function() {
    	var self = this;
    	
    	self.sourceCurrentPage(1);
		self.sourceCurrentPageSubscription = self.sourceCurrentPage.subscribe(function() {
			self.refreshSourceOrderList();
		});
		
		self.refreshSourceOrderList();
		
		self.destinationCurrentPage(1);
		self.destinationCurrentPageSubscription = self.destinationCurrentPage.subscribe(function() {
			self.refreshDestinationOrderList();
		});
		
		purchaseOrderService.getTransferInstance(self.sourceId).done(function(transferInstance) {
			self.destinationOrder.id(transferInstance.id);
			self.destinationOrder.status(transferInstance.status.displayName);
		});
    };
    
    Transfer.show = function(sourceId) {
    	return dialog.show(new Transfer(sourceId));
    };
    
    Transfer.prototype.refreshSourceOrderList = function() {
    	var self = this;
    	
    	purchaseOrderService.getPurchaseOrder(self.sourceId).done(function(sourceOrder) {
    		self.sourceOrder.id(sourceOrder.id);
    		self.sourceOrder.company(sourceOrder.company.name);
    		self.sourceOrder.warehouse(sourceOrder.warehouse.displayName);
    		self.sourceOrder.status(sourceOrder.status.displayName);
    		self.sourceOrder.netTotal(sourceOrder.netTotal);
    	});
    	
    	purchaseOrderItemService.getPurchaseOrderItemList(self.sourceCurrentPage(), self.sourceId, false).done(function(data) {
    		self.sourceOrderItemList(data.list);
    		self.sourceTotalItems(data.total);
    	});
    };
    
    Transfer.prototype.refreshDestinationOrderList = function() {
    	var self = this;
    	
    	purchaseOrderService.getPurchaseOrder(self.destinationOrder.id()).done(function(destinationOrder) {
    		self.destinationOrder.id(destinationOrder.id);
    		self.destinationOrder.status(destinationOrder.status.displayName);
    	});
    	
    	purchaseOrderItemService.getPurchaseOrderItemList(self.destinationCurrentPage(), self.destinationOrder.id(), false).done(function(data) {
    		self.destinationOrderItemList(data.list);
    		self.destinationTotalItems(data.total);
    	});
    };
    
    Transfer.prototype.sourceTransferPiece = function(purchaseOrderItemId) {
    	var self = this;
    	
    	purchaseOrderItemService.transferPiece(purchaseOrderItemId, self.destinationOrder.id()).done(function(result) {
    		if(result.success) {
    			self.log(result.message);
    		} else {
    			app.showMessage(result.message);
    		}
    		self.refreshSourceOrderList();
    		self.refreshDestinationOrderList();
    	});
    };

    Transfer.prototype.sourceTransferPackage = function(purchaseOrderItemId) {
    	var self = this;
    	
    	purchaseOrderItemService.transferPackage(purchaseOrderItemId, self.destinationOrder.id()).done(function(result) {
    		if(result.success) {
    			self.log(result.message);
    		} else {
    			app.showMessage(result.message);
    		}
    		self.refreshSourceOrderList();
    		self.refreshDestinationOrderList();
    	});
    };
    
    Transfer.prototype.sourceTransferAll = function(purchaseOrderItemId) {
    	var self = this;
    	
    	purchaseOrderItemService.transferAll(purchaseOrderItemId, self.destinationOrder.id()).done(function(result) {
    		if(result.success) {
    			self.log(result.message);
    		} else {
    			app.showMessage(result.message);
    		}
    		self.refreshSourceOrderList();
    		self.refreshDestinationOrderList();
    	});
    };
    
    Transfer.prototype.destinationTransferPiece = function(purchaseOrderItemId) {
    	var self = this;
    	
    	purchaseOrderItemService.transferPiece(purchaseOrderItemId, self.sourceOrder.id()).done(function(result) {
    		if(result.success) {
    			self.log(result.message);
    		} else {
    			app.showMessage(result.message);
    		}
    		self.refreshSourceOrderList();
    		self.refreshDestinationOrderList();
    	});
    };
    
    Transfer.prototype.destinationTransferPackage = function(purchaseOrderItemId) {
    	var self = this;
    	
    	purchaseOrderItemService.transferPackage(purchaseOrderItemId, self.sourceOrder.id()).done(function(result) {
    		if(result.success) {
    			self.log(result.message);
    		} else {
    			app.showMessage(result.message);
    		}
    		self.refreshSourceOrderList();
    		self.refreshDestinationOrderList();
    	});
    };
    
    Transfer.prototype.destinationTransferAll = function(purchaseOrderItemId) {
    	var self = this;
    	
    	purchaseOrderItemService.transferAll(purchaseOrderItemId, self.sourceOrder.id()).done(function(result) {
    		if(result.success) {
    			self.log(result.message);
    		} else {
    			app.showMessage(result.message);
    		}
    		self.refreshSourceOrderList();
    		self.refreshDestinationOrderList();
    	});
    };
    
    Transfer.prototype.cancel = function() {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to exit?</p>' + 
    				'<p class="text-warning">Once closed you will not be able to transfer to the same order.</p>',
				'<p class="text-danger">Confirm Exit</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				if(self.sourceOrder.netTotal() == 0 && self.sourceOrder.id() != self.destinationOrder.id()) {
					purchaseOrderService.removePurchaseOrder(self.sourceOrder.id());
					dialog.close(self);
				} else {
					dialog.close(self);
				}
			}
		})
    };
    
    return Transfer;
});