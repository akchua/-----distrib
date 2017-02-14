define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/clientorderitemservice'], 
		function (dialog, app, ko, clientOrderItemService) {
    var SaleView = function(clientOrder) {
    	this.clientOrder = clientOrder;
    	
    	this.clientOrderItemList = ko.observable();
    	
    	this.saleViewModel = {
			id: ko.observable(),
			
			discount: ko.observable(),
			client: ko.observable(),
			formattedGrossTotal: ko.observable(),
			formattedDiscountTotal: ko.observable(),
			lessVat: ko.observable(),
			formattedLessVatAmount: ko.observable(),
			formattedAdditionalDiscount: ko.observable(),
			formattedNetTotal: ko.observable(),
			status: ko.observable()
    	};
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    SaleView.prototype.activate = function() {
    	var self = this;
    	
    	self.saleViewModel.id(self.clientOrder.id);
    	self.saleViewModel.discount(self.clientOrder.additionalDiscount);
		self.saleViewModel.client(self.clientOrder.client.businessName);
		self.saleViewModel.formattedGrossTotal(self.clientOrder.formattedGrossTotal);
		self.saleViewModel.formattedDiscountTotal(self.clientOrder.formattedDiscountTotal);
		self.saleViewModel.lessVat(self.clientOrder.lessVat);
		self.saleViewModel.formattedLessVatAmount(self.clientOrder.formattedLessVatAmount);
		self.saleViewModel.formattedAdditionalDiscount(self.clientOrder.formattedAdditionalDiscountAmount);
		self.saleViewModel.formattedNetTotal(self.clientOrder.formattedNetTotal);
		self.saleViewModel.status(self.clientOrder.status.displayName);
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshClientOrderItemList();
		});
		
		self.refreshClientOrderItemList();
    };
    
    SaleView.prototype.refreshClientOrderItemList = function() {
    	var self = this;
    	
    	clientOrderItemService.getClientOrderItemList(self.currentPage(), self.saleViewModel.id(), false).done(function(data) {
    		self.clientOrderItemList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    SaleView.show = function(clientOrder) {
    	return dialog.show(new SaleView(clientOrder));
    };
    
    SaleView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return SaleView;
});