define(['durandal/app', 'knockout', 'modules/clientcompanypriceservice', 'modules/userservice', 'viewmodels/manage/clientcompanypriceform', 'viewmodels/user/userview'], 
			function (app, ko, clientCompanyPriceService, userService, ClientCompanyPriceForm, UserView) {
    var ClientCompanyPrice = function() {
    	this.clientCompanyPriceList = ko.observable();
    	
    	this.clientId = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    ClientCompanyPrice.prototype.activate = function(activationData) {
    	var self = this;
    	
    	self.clientId = activationData.clientId;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshClientCompanyPriceList();
		});
    	
    	self.refreshClientCompanyPriceList();
    };
    
    ClientCompanyPrice.prototype.compositionComplete = function() {
    	var self = this;
    	
    	self.clientId.subscribe(function(newClientId) {
    		self.clientId(newClientId);
    		self.refreshClientCompanyPriceList();
    	});
    };
    
    ClientCompanyPrice.prototype.refreshClientCompanyPriceList = function() {
    	var self = this;
    	
    	clientCompanyPriceService.getClientCompanyPriceList(self.currentPage(), self.clientId()).done(function(data) {
    		self.clientCompanyPriceList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    ClientCompanyPrice.prototype.add = function() {
    	var self = this;
    	
    	ClientCompanyPriceForm.show(new Object, 'Create Client-Company Price').done(function() {
    		self.refreshClientCompanyPriceList();
    	});
    };
    
    ClientCompanyPrice.prototype.edit = function(clientCompanyPriceId) {
    	var self = this;
    	
    	clientCompanyPriceService.getClientCompanyPrice(clientCompanyPriceId).done(function(clientCompanyPrice) {
    		ClientCompanyPriceForm.show(clientCompanyPrice, 'Edit Client-Company Price').done(function() {
        		self.refreshClientCompanyPriceList();
        	});
    	});
    };
    
    ClientCompanyPrice.prototype.remove = function(clientCompanyPriceId) {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to remove Client-Company Price with <span class="text-primary">ID #' + clientCompanyPriceId + '</span>?</p>',
				'<p class="text-danger">Confirm Remove</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				clientCompanyPriceService.removeClientCompanyPrice(clientCompanyPriceId).done(function(result) {
		    		self.refreshClientCompanyPriceList();
		    		app.showMessage(result.message);
		    	});
			}
		})
    };
    
    ClientCompanyPrice.prototype.viewClient = function(clientId) {
    	var self = this;
    	
    	userService.getUser(clientId).done(function(client) {
    		UserView.show(client);
    	});
    };
    
    return ClientCompanyPrice;
});