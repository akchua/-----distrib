define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/userservice'], function (dialog, app, ko, userService) {
    var ClientSettings = function(user) {
    	this.user = user;
    	
    	this.clientSettingsFormModel = {
    		id: ko.observable(),
    		
    		discount: ko.observable()
	    };
    };
    
    ClientSettings.prototype.activate = function() {
    	var self = this;
    	
    	self.clientSettingsFormModel.id(self.user.id);
    	self.clientSettingsFormModel.discount(self.user.discount);
    };
    
    ClientSettings.show = function(user) {
    	return dialog.show(new ClientSettings(user));
    };
    
    ClientSettings.prototype.save = function() {
    	var self = this;
    	
        userService.changeClientSettings(ko.toJSON(self.clientSettingsFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    ClientSettings.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return ClientSettings;
});