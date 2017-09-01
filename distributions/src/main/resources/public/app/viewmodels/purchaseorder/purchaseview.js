define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/purchaseorderitemservice'], 
		function (dialog, app, ko, purchaseOrderItemService) {
    var PurchaseView = function(purchaseOrder) {
    	this.purchaseOrder = purchaseOrder;
    	
    	this.purchaseOrderItemList = ko.observable();
    	
    	this.purchaseViewModel = {
			id: ko.observable(),
			
			companyId: ko.observable(),
			companyName: ko.observable(),
			warehouseName: ko.observable(),
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
    
    PurchaseView.prototype.activate = function() {
    	var self = this;
    	
    	self.purchaseViewModel.id(self.purchaseOrder.id);
    	self.purchaseViewModel.companyId(self.purchaseOrder.company.id);
		self.purchaseViewModel.companyName(self.purchaseOrder.company.name);
		self.purchaseViewModel.warehouseName(self.purchaseOrder.warehouse.name + ' - ' + self.purchaseOrder.warehouse.address);
		self.purchaseViewModel.creatorName(self.purchaseOrder.creator.formattedName);
		self.purchaseViewModel.formattedGrossTotal(self.purchaseOrder.formattedGrossTotal);
		self.purchaseViewModel.formattedDiscountTotal(self.purchaseOrder.formattedDiscountTotal);
		self.purchaseViewModel.formattedNetTotal(self.purchaseOrder.formattedNetTotal);
		self.purchaseViewModel.status(self.purchaseOrder.status.displayName);
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshPurchaseOrderItemList();
		});
		
		self.refreshPurchaseOrderItemList();
    };
    
    PurchaseView.prototype.refreshPurchaseOrderItemList = function() {
    	var self = this;
    	
    	purchaseOrderItemService.getPurchaseOrderItemList(self.currentPage(), self.purchaseViewModel.id(), false).done(function(data) {
    		self.purchaseOrderItemList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    PurchaseView.show = function(purchaseOrder) {
    	return dialog.show(new PurchaseView(purchaseOrder));
    };
    
    PurchaseView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return PurchaseView;
});