define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/clientorderservice', 'modules/userservice', 'bootstrap-datetimepicker'],
			function (dialog, app, ko, clientOrderService, userService, dateTimePicker) {
    var SalesReport = function() {
    	this.warehouseList = ko.observable();
    	this.clientList = ko.observable();
    	
    	this.salesReportQuery = {
    		from: ko.observable(new Date()),
    		to: ko.observable(new Date()),
    		warehouse: ko.observable(),
    		clientId: ko.observable(),
    		includePaid: ko.observable(true),
    		includeDelivered: ko.observable(true),
    		includeDispatched: ko.observable(false),
    		includeAccepted: ko.observable(false),
    		includeSubmitted: ko.observable(false),
    		includeCreating: ko.observable(false),
    		showNetTrail: ko.observable()
	    };
    };
    
    SalesReport.prototype.activate = function() {
    	var self = this;
    	
    	clientOrderService.getWarehouseList().done(function(warehouseList) {
			self.warehouseList(warehouseList);
		});
    	
    	userService.getFullClientList().done(function(clientList) {
    		self.clientList(clientList);
    	});
    };
    
    SalesReport.show = function() {
    	return dialog.show(new SalesReport());
    };
    
    SalesReport.prototype.generateReport = function() {
    	var self = this;
    	
    	clientOrderService.generateReport(ko.toJSON(self.salesReportQuery)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    SalesReport.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return SalesReport;
});