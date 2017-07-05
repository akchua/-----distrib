define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/clientorderservice', 'modules/userservice', 'modules/companyservice', 'modules/fileservice'],
			function (dialog, app, ko, clientOrderService, userService, companyService, fileService) {
    var SalesReport = function() {
    	this.warehouseList = ko.observable();
    	this.clientList = ko.observable();
    	this.companyList = ko.observable();
    	this.clientSalesReportTypes = ko.observable();
    	
    	this.showInclude = ko.observable(true);
    	
    	this.enableGenerateButton = ko.observable(true);
    	
    	this.salesReportQuery = {
    		from: ko.observable(),
    		to: ko.observable(),
    		
    		warehouse: ko.observable(),
    		clientId: ko.observable(),
    		companyId: ko.observable(),
    		
    		clientSalesReportType: ko.observable('STATUS_BASED'),  //HARDCODE FOR NOW
    		
    		includePaid: ko.observable(true),
    		includeDelivered: ko.observable(true),
    		includeDispatched: ko.observable(false),
    		includeToFollow: ko.observable(false),
    		includeAccepted: ko.observable(false),
    		includeSubmitted: ko.observable(false),
    		includeCreating: ko.observable(false),
    		
    		sendMail: ko.observable(true),
    		downloadFile: ko.observable(true)
	    };
    };
    
    SalesReport.prototype.activate = function() {
    	var self = this;
    	
    	clientOrderService.getWarehouseList().done(function(warehouseList) {
			self.warehouseList(warehouseList);
		});
    	
    	clientOrderService.getClientSalesReportType().done(function(clientSalesReportTypes) {
			self.clientSalesReportTypes(clientSalesReportTypes);
		});
    	
    	userService.getFullClientList().done(function(clientList) {
    		self.clientList(clientList);
    	});
    	
    	companyService.getCompanyListByName().done(function(companyList) {
    		self.companyList(companyList);
    	});
    	
    	self.salesReportQuery.clientSalesReportType.subscribe(function(salesReportType) {
    		self.showInclude(salesReportType == 'STATUS_BASED');
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