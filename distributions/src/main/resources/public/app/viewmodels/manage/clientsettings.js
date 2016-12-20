define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/userservice'], function (dialog, app, ko, userService) {
    var ClientSettings = function(user) {
    	this.user = user;
    	
    	this.vatTypeList = ko.observable();
    	
    	this.clientSettingsFormModel = {
    		id: ko.observable(),
    		
    		discount: ko.observable(),
    		vatType: ko.observable()
	    };
    };
    
    ClientSettings.prototype.activate = function() {
    	var self = this;
    	
    	self.clientSettingsFormModel.id(self.user.id);
    	self.clientSettingsFormModel.discount(self.user.discount);
    	if(self.user.vatType) self.clientSettingsFormModel.vatType(self.user.vatType.name);
    	
    	userService.getVatTypeList().done(function(vatTypeList) {
    		self.vatTypeList(vatTypeList);
    		if(self.user.vatType) self.clientSettingsFormModel.vatType(self.user.vatType.name);
    	});
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