define(['knockout', 'modules/userservice'], 
			function (ko, userService) {
    var PriceSettings = function() {
    	this.clientList = ko.observable();
    	
    	this.clientId = ko.observable();
    };
    
    PriceSettings.prototype.activate = function() {
    	var self = this;
    	
    	userService.getFullClientList().done(function(clientList) {
    		self.clientList(clientList);
    	});
    };
    
    return PriceSettings;
});