define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/clientorderservice', 'modules/userservice', 'modules/fileservice'],
			function (dialog, app, ko, clientOrderService, userService, fileService) {
    var SalesReport = function() {
    	this.warehouseList = ko.observable();
    	this.clientList = ko.observable();
    	
    	this.enableGenerateButton = ko.observable(true);
    	
    	this.salesReportQuery = {
    		from: ko.observable(),
    		to: ko.observable(),
    		
    		warehouse: ko.observable(),
    		clientId: ko.observable(),
    		
    		includePaid: ko.observable(true),
    		includeDelivered: ko.observable(true),
    		includeDispatched: ko.observable(false),
    		includeToFollow: ko.observable(false),
    		includeAccepted: ko.observable(false),
    		includeSubmitted: ko.observable(false),
    		includeCreating: ko.observable(false),
    		
    		showNetTrail: ko.observable(true),
    		
    		sendMail: ko.observable(true),
    		downloadFile: ko.observable(false)
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

    	self.enableGenerateButton(false);
    	clientOrderService.generateReport(ko.toJSON(self.salesReportQuery)).done(function(result) {
        	if(result.success) {
        		if(self.salesReportQuery.downloadFile()) {
        			fileService.downloadSalesReportByFileName(result.extras.fileName);
        		}
        		dialog.close(self);
        	} 
        	self.enableGenerateButton(true);
        	app.showMessage(result.message);
        });
    };
    
    SalesReport.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return SalesReport;
});