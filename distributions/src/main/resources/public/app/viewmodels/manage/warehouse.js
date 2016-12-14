define(['plugins/router', 'durandal/app', 'knockout', 'modules/clientorderservice', 'viewmodels/manage/warehouseview'],
		function (router, app, ko, clientOrderService, WarehouseView) {
    var Warehouse = function() {
    	this.warehouseList = ko.observable();
    };
    
    Warehouse.prototype.activate = function() {
    	var self = this;
		
		clientOrderService.getWarehouseList().done(function(warehouseList) {
			self.warehouseList(warehouseList);
		});
    };
    
    Warehouse.prototype.generateReport = function() {
    	var self = this;
    	
    	alert('not yet functional');
    };
    
    Warehouse.prototype.view = function(warehouse) {
    	var self = this;
    	
    	WarehouseView.show(warehouse)
    };
    
    return Warehouse;
});