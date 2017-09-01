define(['plugins/router', 'plugins/dialog', 'durandal/app', 'knockout', 'modules/purchaseorderservice', 'modules/warehouseservice', 'modules/companyservice'], 
		function (router, dialog, app, ko, purchaseOrderService, warehouseService, companyService) {
    var PurchaseOrderForm = function(purchaseOrder, title) {
    	this.purchaseOrder = purchaseOrder;
    	this.title = title;
    	
    	this.companyList = ko.observable();
    	this.warehouseList = ko.observable();
    	
    	this.enableSave = ko.observable(true);
    	
    	this.purchaseOrderFormModel = {
    		id: ko.observable(),
    		
    		companyId: ko.observable(),
    		warehouseId: ko.observable()
	    };
    };
    
    PurchaseOrderForm.prototype.activate = function() {
    	var self = this;
    	
    	self.purchaseOrderFormModel.id(self.purchaseOrder.id);
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    		self.purchaseOrderFormModel.companyId(self.purchaseOrder.company.id);
    	});
    	
    	warehouseService.getWarehouseListByName().done(function(warehouseList) {
    		self.warehouseList(warehouseList);
    		self.purchaseOrderFormModel.warehouseId(self.purchaseOrder.warehouse.id);
    	});
    };
    
    PurchaseOrderForm.show = function(purchaseOrder, title) {
    	return dialog.show(new PurchaseOrderForm(purchaseOrder, title));
    };
    
    PurchaseOrderForm.prototype.save = function() {
    	var self = this;
    	
    	self.enableSave(false);
        purchaseOrderService.savePurchaseOrder(ko.toJSON(self.purchaseOrderFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);
        		router.navigate('#purchaseorderpage/' + result.extras.purchaseOrderId);
        	} else {
         		self.enableSave(true);
         		app.showMessage(result.message);
        	}
        });
    };
    
    PurchaseOrderForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return PurchaseOrderForm;
});