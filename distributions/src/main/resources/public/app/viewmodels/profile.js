define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/userservice'], function (dialog, app, ko, userService) {
    var Profile = function(user) {
    	this.user = user;
    	
    	this.userFormModel = {
    		id: ko.observable(),
    		
    		firstName: ko.observable(),
	    	lastName: ko.observable(),
	    	contactNumber: ko.observable(),
	    	
	    	businessName: ko.observable(),
    		businessAddress: ko.observable(),
    		businessContactNumber: ko.observable(),
    		businessArea: ko.observable(),
    		
	    	username: ko.observable(),
	    	emailAddress: ko.observable(),
	    	password: ko.observable(),
	    	confirmPassword: ko.observable(),
	    	itemsPerPage: ko.observable(),
	    	userType: ko.observable()
	    };
    };
    
    Profile.prototype.activate = function() {
    	var self = this;
    	
    	self.userFormModel.id(self.user.id);
    	self.userFormModel.firstName(self.user.firstName);
    	self.userFormModel.lastName(self.user.lastName);
    	self.userFormModel.contactNumber(self.user.contactNumber);
    	self.userFormModel.businessName(self.user.businessName);
    	self.userFormModel.businessAddress(self.user.businessAddress);
    	self.userFormModel.businessContactNumber(self.user.businessContactNumber);
    	if(self.user.businessArea) self.userFormModel.businessArea(self.user.businessArea.name);
    	self.userFormModel.username(self.user.username);
    	self.userFormModel.emailAddress(self.user.emailAddress);
    	self.userFormModel.itemsPerPage(self.user.itemsPerPage);
    	if(self.user.userType) self.userFormModel.userType(self.user.userType.name);
    };
    
    Profile.show = function(user) {
    	return dialog.show(new Profile(user));
    };
    
    Profile.prototype.save = function() {
    	var self = this;
    	
        userService.saveUser(ko.toJSON(self.userFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    Profile.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return Profile;
});