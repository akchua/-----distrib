define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/clientorderservice', 'modules/clientorderitemservice'],
		function (dialog, app, ko, clientOrderService, clientOrderItemService) {
	
	var Transfer = function(sourceId) {
    	this.sourceId = sourceId;
    	this.sourceOrderItemList = ko.observable();
    	this.destinationOrderItemList = ko.observable();
    	
    	this.log = ko.observable('<p class="text-info">Click any button beside the product to transfer that product.</p>');
    	
    	this.sourceOrder = {
			id: ko.observable(),
			client: ko.observable(),
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
		
		clientOrderService.getTransferInstance(self.sourceId).done(function(transferInstance) {
			self.destinationOrder.id(transferInstance.id);
			self.destinationOrder.status(transferInstance.status.displayName);
		});
    };
    
    Transfer.show = function(sourceId) {
    	return dialog.show(new Transfer(sourceId));
    };
    
    Transfer.prototype.refreshSourceOrderList = function() {
    	var self = this;
    	
    	clientOrderService.getClientOrder(self.sourceId).done(function(sourceOrder) {
    		self.sourceOrder.id(sourceOrder.id);
    		self.sourceOrder.client(sourceOrder.creator.businessName);
    		self.sourceOrder.status(sourceOrder.status.displayName);
    		self.sourceOrder.netTotal(sourceOrder.netTotal);
    	});
    	
    	clientOrderItemService.getClientOrderItemList(self.sourceCurrentPage(), self.sourceId, false).done(function(data) {
    		self.sourceOrderItemList(data.list);
    		self.sourceTotalItems(data.total);
    	});
    };
    
    Transfer.prototype.refreshDestinationOrderList = function() {
    	var self = this;
    	
    	clientOrderService.getClientOrder(self.destinationOrder.id()).done(function(destinationOrder) {
    		self.destinationOrder.id(destinationOrder.id);
    		self.destinationOrder.status(destinationOrder.status.displayName);
    	});
    	
    	clientOrderItemService.getClientOrderItemList(self.destinationCurrentPage(), self.destinationOrder.id(), false).done(function(data) {
    		self.destinationOrderItemList(data.list);
    		self.destinationTotalItems(data.total);
    	});
    };
    
    Transfer.prototype.sourceTransferPiece = function(clientOrderItemId) {
    	var self = this;
    	
    	clientOrderItemService.transferPiece(clientOrderItemId, self.destinationOrder.id()).done(function(result) {
    		if(result.success) {
    			self.log(result.message);
    		} else {
    			app.showMessage(result.message);
    		}
    		self.refreshSourceOrderList();
    		self.refreshDestinationOrderList();
    	});
    };

    Transfer.prototype.sourceTransferPackage = function(clientOrderItemId) {
    	var self = this;
    	
    	clientOrderItemService.transferPackage(clientOrderItemId, self.destinationOrder.id()).done(function(result) {
    		if(result.success) {
    			self.log(result.message);
    		} else {
    			app.showMessage(result.message);
    		}
    		self.refreshSourceOrderList();
    		self.refreshDestinationOrderList();
    	});
    };
    
    Transfer.prototype.sourceTransferAll = function(clientOrderItemId) {
    	var self = this;
    	
    	clientOrderItemService.transferAll(clientOrderItemId, self.destinationOrder.id()).done(function(result) {
    		if(result.success) {
    			self.log(result.message);
    		} else {
    			app.showMessage(result.message);
    		}
    		self.refreshSourceOrderList();
    		self.refreshDestinationOrderList();
    	});
    };
    
    Transfer.prototype.destinationTransferPiece = function(clientOrderItemId) {
    	var self = this;
    	
    	clientOrderItemService.transferPiece(clientOrderItemId, self.sourceOrder.id()).done(function(result) {
    		if(result.success) {
    			self.log(result.message);
    		} else {
    			app.showMessage(result.message);
    		}
    		self.refreshSourceOrderList();
    		self.refreshDestinationOrderList();
    	});
    };
    
    Transfer.prototype.destinationTransferPackage = function(clientOrderItemId) {
    	var self = this;
    	
    	clientOrderItemService.transferPackage(clientOrderItemId, self.sourceOrder.id()).done(function(result) {
    		if(result.success) {
    			self.log(result.message);
    		} else {
    			app.showMessage(result.message);
    		}
    		self.refreshSourceOrderList();
    		self.refreshDestinationOrderList();
    	});
    };
    
    Transfer.prototype.destinationTransferAll = function(clientOrderItemId) {
    	var self = this;
    	
    	clientOrderItemService.transferAll(clientOrderItemId, self.sourceOrder.id()).done(function(result) {
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
					clientOrderService.removeClientOrder(self.sourceOrder.id());
					dialog.close(self);
				} else {
					dialog.close(self);
				}
			}
		})
    };
    
    return Transfer;
});