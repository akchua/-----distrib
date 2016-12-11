define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/purchaseorderservice', 'modules/companyservice'], 
		function (dialog, app, ko, purchaseOrderService, companyService) {
    var PurchaseOrderForm = function(purchaseOrder, title) {
    	this.purchaseOrder = purchaseOrder;
    	this.title = title;
    	
    	this.companyList = ko.observable();
    	this.warehouseList = ko.observable();
    	
    	this.purchaseOrderFormModel = {
    		id: ko.observable(),
    		
    		companyId: ko.observable(),
    		warehouse: ko.observable()
	    };
    };
    
    PurchaseOrderForm.prototype.activate = function() {
    	var self = this;
    	
    	self.purchaseOrderFormModel.id(self.purchaseOrder.id);
    	self.purchaseOrderFormModel.warehouse(self.purchaseOrder.warehouse);
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    		self.purchaseOrderFormModel.companyId(self.purchaseOrder.company.id);
    	});
    	
    	purchaseOrderService.getWarehouseList().done(function(warehouseList) {
    		self.warehouseList(warehouseList);
    		self.purchaseOrderFormModel.warehouse(self.purchaseOrder.warehouse);
    	});
    };
    
    PurchaseOrderForm.show = function(purchaseOrder, title) {
    	return dialog.show(new PurchaseOrderForm(purchaseOrder, title));
    };
    
    PurchaseOrderForm.prototype.save = function() {
    	var self = this;
    	
        purchaseOrderService.savePurchaseOrder(ko.toJSON(self.purchaseOrderFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    PurchaseOrderForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return PurchaseOrderForm;
});