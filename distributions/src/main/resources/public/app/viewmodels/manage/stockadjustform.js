define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/stockadjustservice', 'modules/productservice', 'modules/warehouseservice'], 
		function (dialog, app, ko, stockAdjustService, productService, warehouseService) {
    var StockAdjustForm = function(product) {
    	this.product = product;
    	
    	this.warehouseList = ko.observable();
    	this.stockCount = ko.observable('0');
    	
    	this.productPackaging = ko.observable();
    	this.allStock = ko.observable();
    	this.productDisplayName = ko.observable();
    	
    	this.stockAdjustFormModel = {
    		productId: ko.observable(),
    		warehouse: ko.observable(),
    		description: ko.observable(),
    		pieceQuantity: ko.observable(),
    		packageQuantity: ko.observable()
	    };
    };
    
    StockAdjustForm.prototype.activate = function() {
    	var self = this;

    	self.productPackaging(self.product.packaging);
    	self.allStock(self.product.formattedStockCountAll);
    	self.productDisplayName(self.product.displayName);
    	
    	self.stockAdjustFormModel.productId(self.product.id);
    	
    	warehouseService.getWarehouseListByName().done(function(warehouseList) {
    		self.warehouseList(warehouseList);
    	});
    };
    
    StockAdjustForm.prototype.compositionComplete = function() {
    	var self = this;
    	
    	self.stockAdjustFormModel.warehouse.subscribe(function(newWarehouse) {
    		productService.getProduct(self.product.id, newWarehouse).done(function(product) {
    			if(product) {
    				self.stockCount(product.formattedStockCountCurrent);
    			} else {
    				self.stockCount('0');
    			}
    		});
    	});
    };
    
    StockAdjustForm.show = function(product) {
    	return dialog.show(new StockAdjustForm(product));
    };
    
    StockAdjustForm.prototype.adjust = function() {
    	var self = this;
    	
        stockAdjustService.adjustStock(ko.toJSON(self.stockAdjustFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    StockAdjustForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return StockAdjustForm;
});