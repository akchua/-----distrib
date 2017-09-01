define(['plugins/router', 'durandal/app', 'knockout', 'modules/warehouseservice', 'viewmodels/report/warehouseview'],
		function (router, app, ko, warehouseService, WarehouseView) {
    var Warehouse = function() {
    	this.warehouseList = ko.observable();
    };
    
    Warehouse.prototype.activate = function() {
    	var self = this;
		
		warehouseService.getWarehouseListByName().done(function(warehouseList) {
			self.warehouseList(warehouseList);
		});
    };
    
    Warehouse.prototype.generateReport = function() {
    	var self = this;
    	
    	alert('not yet functional');
    };
    
    Warehouse.prototype.view = function(warehouseId) {
    	var self = this;
    	
    	WarehouseView.show(warehouseId)
    };
    
    return Warehouse;
});