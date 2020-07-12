define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/clientorderitemservice'], 
		function (dialog, app, ko, clientOrderItemService) {
    var SaleView = function(partialClientOrder) {
    	this.partialClientOrder = partialClientOrder;
    	
    	this.partialClientOrderItemList = ko.observable();
    	
    	this.showClientDiscount = ko.observable(true);
    	
    	this.saleViewModel = {
			id: ko.observable(),
			
			creatorName: ko.observable(),
			dispatcherName: ko.observable(),
			clientBusinessName: ko.observable(),
			companyName: ko.observable(),
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
    	
    	self.saleViewModel.id(self.partialClientOrder.id);
    	self.saleViewModel.creatorName(self.partialClientOrder.creatorName);
    	self.saleViewModel.dispatcherName(self.partialClientOrder.dispatcherName);
		self.saleViewModel.clientBusinessName(self.partialClientOrder.clientBusinessName);
		self.saleViewModel.companyName(self.partialClientOrder.companyName);
		self.saleViewModel.formattedGrossTotal(self.partialClientOrder.formattedGrossTotal);
		self.saleViewModel.formattedDiscountTotal(self.partialClientOrder.formattedDiscountTotal);
		self.saleViewModel.lessVat(self.partialClientOrder.lessVat);
		self.saleViewModel.formattedLessVatAmount(self.partialClientOrder.formattedLessVatAmount);
		self.saleViewModel.formattedAdditionalDiscount(self.partialClientOrder.formattedAdditionalDiscount);
		self.saleViewModel.formattedNetTotal(self.partialClientOrder.formattedNetTotal);
		self.saleViewModel.status(self.partialClientOrder.status.displayName);
		
		self.showClientDiscount(self.partialClientOrder.additionalDiscount != 0);
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshClientOrderItemList();
		});
		
		self.refreshClientOrderItemList();
    };
    
    SaleView.prototype.refreshClientOrderItemList = function() {
    	var self = this;
    	
    	clientOrderItemService.getPartialClientOrderItemList(self.currentPage(), self.saleViewModel.id(), false).done(function(data) {
    		self.partialClientOrderItemList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    SaleView.show = function(partialClientOrder) {
    	return dialog.show(new SaleView(partialClientOrder));
    };
    
    SaleView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return SaleView;
});