define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/clientorderservice', 'modules/fileservice'],
			function (dialog, app, ko, clientOrderService, fileService) {
    var ClientRankingQuery = function() {
    	this.areaList = ko.observable();
    	this.clientRankTypeList = ko.observable();
    	
    	this.enableGenerateButton = ko.observable(true);
    	
    	this.clientRankQuery = {
    		monthFrom: ko.observable(),
    		monthTo: ko.observable(),
    		
    		area: ko.observable(),
    		clientRankType: ko.observable(),
    		
    		sendMail: ko.observable(false),
    		downloadFile: ko.observable(true)
	    };
    };
    
    ClientRankingQuery.prototype.activate = function() {
    	var self = this;
    	
    	clientOrderService.getAreaList().done(function(areaList) {
    		self.areaList(areaList);
    	});
    	
    	clientOrderService.getClientRankTypeList().done(function(clientRankTypeList) {
    		self.clientRankTypeList(clientRankTypeList);
    	});
    };
    
    ClientRankingQuery.show = function() {
    	return dialog.show(new ClientRankingQuery());
    };
    
    ClientRankingQuery.prototype.generateClientRanking = function() {
    	var self = this;

    	self.enableGenerateButton(false);
    	clientOrderService.generateClientRanking(ko.toJSON(self.clientRankQuery)).done(function(result) {
        	if(result.success) {
        		if(self.clientRankQuery.downloadFile()) {
        			fileService.downloadClientRankingByFileName(result.extras.fileName);
        		}
        		dialog.close(self);
        	} 
        	self.enableGenerateButton(true);
        	app.showMessage(result.message);
        });
    };
    
    ClientRankingQuery.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return ClientRankingQuery;
});