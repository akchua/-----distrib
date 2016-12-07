define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/userservice'], function (dialog, app, ko, userService) {
    var UserForm = function(user, title) {
    	this.user = user;
    	this.title = title;
    	
    	this.showBusinessInformation = ko.observable(false);
    	this.userTypeList = ko.observable();
    	this.areaList = ko.observable();
    	
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
    
    UserForm.prototype.activate = function() {
    	var self = this;
    	
    	self.userFormModel.id(self.user.id);
    	self.userFormModel.firstName(self.user.firstName);
    	self.userFormModel.lastName(self.user.lastName);
    	self.userFormModel.contactNumber(self.user.contactNumber);
    	self.userFormModel.businessName(self.user.businessName);
    	self.userFormModel.businessAddress(self.user.businessAddress);
    	self.userFormModel.businessContactNumber(self.user.businessContactNumber);
    	self.userFormModel.businessArea(self.user.businessArea);
    	self.userFormModel.username(self.user.username);
    	self.userFormModel.emailAddress(self.user.emailAddress);
    	self.userFormModel.itemsPerPage(self.user.itemsPerPage);
    	self.userFormModel.userType(self.user.userType);
    	
    	userService.getUserTypeList().done(function(userTypeList) {
    		self.userTypeList(userTypeList);
    		self.userFormModel.userType(self.user.userType);
    		
    		if(!self.userFormModel.userType() || self.userFormModel.userType() == 'CLIENT') {
        		self.showBusinessInformation(true);
        	}
    	});
    	
    	userService.getAreaList().done(function(areaList) {
    		self.areaList(areaList);
    		self.userFormModel.businessArea(self.user.businessArea);
    	});
    };
    
    UserForm.prototype.compositionComplete = function() {
    	var self = this;
    	
    	self.userFormModel.userType.subscribe(function(newUserType) {
    		if(!newUserType || newUserType == 'CLIENT') {
        		self.showBusinessInformation(true);
        	} else {
        		self.showBusinessInformation(false);
        	}
    	});
    };
    
    UserForm.show = function(user, title) {
    	return dialog.show(new UserForm(user, title));
    };
    
    UserForm.prototype.save = function() {
    	var self = this;
    	
        userService.saveUser(ko.toJSON(self.userFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    UserForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return UserForm;
});