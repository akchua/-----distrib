define(['plugins/router', 'durandal/app', 'knockout', 'modules/clientorderservice', 'modules/purchaseorderservice', 'modules/warehouseitemservice'],
		function (router, app, ko, clientOrderService, purchaseOrderService, warehouseItemService) {
    var AdminHome = function() {
    	this.formattedCollectible = ko.observable();
    	this.onGoingSalesCount = ko.observable();
    	this.onGoingPurchasesCount = ko.observable();
    	
    	this.warehouseValueList = ko.observable();
    };
    
    AdminHome.prototype.activate = function() {
    	var self = this;
    	
    	clientOrderService.getFormattedTotalCollectible().done(function(formattedCollectible) {
    		self.formattedCollectible(formattedCollectible.content);
    	});
    	
    	clientOrderService.getOnGoingSalesCount().done(function(onGoingSalesCount) {
    		self.onGoingSalesCount(onGoingSalesCount);
    	});
    	
    	purchaseOrderService.getOnGoingPurchasesCount().done(function(onGoingPurchasesCount) {
    		self.onGoingPurchasesCount(onGoingPurchasesCount);
    	});
    	
    	warehouseItemService.getWarehouseValueList().done(function(warehouseValueList) {
			self.warehouseValueList(warehouseValueList);
		});
    };
    
    return AdminHome;
});