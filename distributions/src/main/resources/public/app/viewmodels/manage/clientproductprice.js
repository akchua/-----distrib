define(['durandal/app', 'knockout', 'modules/clientproductpriceservice', 'modules/userservice', 'viewmodels/manage/clientproductpriceform', 'viewmodels/user/userview'], 
			function (app, ko, clientProductPriceService, userService, ClientProductPriceForm, UserView) {
    var ClientProductPrice = function() {
    	this.clientProductPriceList = ko.observable();
    	
    	this.clientId = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    ClientProductPrice.prototype.activate = function(activationData) {
    	var self = this;
    	
    	self.clientId = activationData.clientId;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshClientProductPriceList();
		});
    	
    	self.refreshClientProductPriceList();
    };
    
    ClientProductPrice.prototype.compositionComplete = function() {
    	var self = this;
    	
    	self.clientId.subscribe(function(newClientId) {
    		self.clientId(newClientId);
    		self.refreshClientProductPriceList();
    	});
    };
    
    ClientProductPrice.prototype.refreshClientProductPriceList = function() {
    	var self = this;
    	
    	clientProductPriceService.getClientProductPriceList(self.currentPage(), self.clientId()).done(function(data) {
    		self.clientProductPriceList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    ClientProductPrice.prototype.add = function() {
    	var self = this;
    	
    	ClientProductPriceForm.show(new Object, 'Create Client-Product Price').done(function() {
    		self.refreshClientProductPriceList();
    	});
    };
    
    ClientProductPrice.prototype.edit = function(clientProductPriceId) {
    	var self = this;
    	
    	clientProductPriceService.getClientProductPrice(clientProductPriceId).done(function(clientProductPrice) {
    		ClientProductPriceForm.show(clientProductPrice, 'Edit Client-Product Price').done(function() {
        		self.refreshClientProductPriceList();
        	});
    	});
    };
    
    ClientProductPrice.prototype.remove = function(clientProductPriceId) {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to remove Client-Product Price with <span class="text-primary">ID #' + clientProductPriceId + '</span>?</p>',
				'<p class="text-danger">Confirm Remove</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				clientProductPriceService.removeClientProductPrice(clientProductPriceId).done(function(result) {
		    		self.refreshClientProductPriceList();
		    		app.showMessage(result.message);
		    	});
			}
		})
    };
    
    ClientProductPrice.prototype.viewClient = function(clientId) {
    	var self = this;
    	
    	userService.getUser(clientId).done(function(client) {
    		UserView.show(client);
    	});
    };
    
    return ClientProductPrice;
});