define(['durandal/app', 'knockout', 'modules/userservice', 'viewmodels/userform', 'viewmodels/manage/clientsettings', 'viewmodels/user/userview'],
		function (app, ko, userService, UserForm, ClientSettings, UserView) {
    var Client = function() {
    	this.clientList = ko.observable();
    	
    	this.searchKey = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    Client.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshClientList();
		});
		
		self.refreshClientList();
    };
    
    Client.prototype.refreshClientList = function() {
    	var self = this;
    	
    	userService.getClientList(self.currentPage(), self.searchKey()).done(function(data) {
    		self.clientList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    Client.prototype.add = function() {
    	UserForm.show(new Object(), 'Add Client')
    };
    
    Client.prototype.view = function(clientId) {
    	var self = this;
    	
    	userService.getUser(clientId).done(function(client) {
    		UserView.show(client);
    	});
    };
    
    Client.prototype.settings = function(userId) {
    	var self = this;
    	
    	userService.getUser(userId).done(function(user) {
    		ClientSettings.show(user).done(function() {
    			self.refreshClientList();
    		});
    	});
    };
    
    Client.prototype.search = function() {
    	var self = this;
    	
    	self.currentPage(1);
    	self.refreshClientList();
    };
    
    return Client;
});