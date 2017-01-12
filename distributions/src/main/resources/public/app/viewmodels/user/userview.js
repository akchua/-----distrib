define(['plugins/dialog', 'durandal/app', 'knockout'], 
		function (dialog, app, ko) {
    var UserView = function(user) {
    	this.user = user;
    	
    	this.showBusinessInfo = ko.observable(false);
    	
    	this.userViewModel = {
    		id: ko.observable(),
    		
    		name: ko.observable(),
    		
    		contactNumber: ko.observable(),
    		username: ko.observable(),
    		email: ko.observable(),
    		userType: ko.observable(),
    		
    		businessName: ko.observable(),
    		businessArea: ko.observable(),
    		businessAddress: ko.observable(),
    		businessContactNumber: ko.observable(),
    		discount: ko.observable(),
    		markup: ko.observable(),
    		vatType: ko.observable()
	    };
    };
    
    UserView.prototype.activate = function() {
    	var self = this;
    	
    	self.userViewModel.id(self.user.id);
    	
    	self.userViewModel.name(self.user.formattedName);
    	
    	self.userViewModel.username(self.user.username);
    	self.userViewModel.contactNumber(self.user.contactNumber);
    	self.userViewModel.email(self.user.emailAddress);
    	self.userViewModel.userType(self.user.userType.displayName);
    	
    	self.userViewModel.businessName(self.user.businessName);
    	if(self.user.businessArea != null) self.userViewModel.businessArea(self.user.businessArea.displayName);
    	else self.userViewModel.businessArea('n/a');
    	self.userViewModel.businessAddress(self.user.businessAddress);
    	self.userViewModel.businessContactNumber(self.user.businessContactNumber);
    	self.userViewModel.discount(self.user.formattedDiscount);
    	self.userViewModel.markup(self.user.formattedMarkup);
    	if(self.user.vatType != null) self.userViewModel.vatType(self.user.vatType.displayName);
    	else self.userViewModel.vatType('n/a');
    	
    	if(self.user.userType.name == 'CLIENT') self.showBusinessInfo(true);
    	else self.showBusinessInfo(false);
    };
    
    UserView.show = function(user) {
    	return dialog.show(new UserView(user));
    };
    
    UserView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return UserView;
});