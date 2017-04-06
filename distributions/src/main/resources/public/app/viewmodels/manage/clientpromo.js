define(['durandal/app', 'knockout', 'modules/clientproductpriceservice', 'modules/userservice', 'viewmodels/manage/clientproductpriceform', 'viewmodels/user/userview'], 
			function (app, ko, clientPromoService, userService, ClientPromoForm, UserView) {
    var ClientPromo = function() {
    	this.clientPromoList = ko.observable();
    	
    	this.clientId = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    ClientPromo.prototype.activate = function(activationData) {
    	var self = this;
    	
    	self.clientId = activationData.clientId;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshClientPromoList();
		});
    	
    	self.refreshClientPromoList();
    };
    
    ClientPromo.prototype.compositionComplete = function() {
    	var self = this;
    	
    	self.clientId.subscribe(function(newClientId) {
    		self.clientId(newClientId);
    		self.refreshClientPromoList();
    	});
    };
    
    ClientPromo.prototype.refreshClientPromoList = function() {
    	var self = this;
    	
    	clientPromoService.getClientPromoList(self.currentPage(), self.clientId()).done(function(data) {
    		self.clientPromoList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    ClientPromo.prototype.add = function() {
    	var self = this;
    	
    	ClientPromoForm.show(new Object, 'Create Client-Product Price').done(function() {
    		self.refreshClientPromoList();
    	});
    };
    
    ClientPromo.prototype.edit = function(clientPromoId) {
    	var self = this;
    	
    	clientPromoService.getClientPromo(clientPromoId).done(function(clientPromo) {
    		ClientPromoForm.show(clientPromo, 'Edit Client-Product Price').done(function() {
        		self.refreshClientPromoList();
        	});
    	});
    };
    
    ClientPromo.prototype.remove = function(clientPromoId) {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to remove Client-Promo Discount with <span class="text-primary">ID #' + clientPromoId + '</span>?</p>',
				'<p class="text-danger">Confirm Remove</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				clientPromoService.removeClientPromo(clientPromoId).done(function(result) {
		    		self.refreshClientPromoList();
		    		app.showMessage(result.message);
		    	});
			}
		})
    };
    
    ClientPromo.prototype.viewClient = function(clientId) {
    	var self = this;
    	
    	userService.getUser(clientId).done(function(client) {
    		UserView.show(client);
    	});
    };
    
    return ClientPromo;
});