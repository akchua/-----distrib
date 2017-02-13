define(['durandal/app', 'durandal/system', 'knockout', 'modules/clientorderservice', 'modules/clientorderitemservice', 'viewmodels/clientorder/additem'],
		function (app, system, ko, clientOrderService, clientOrderItemService, AddItem) {
    var ClientOrderPage = function() {
    	this.clientOrderItemList = ko.observable();
    	
    	this.showCheckButton = ko.observable(false);
    	this.showClientDiscount = ko.observable(true);
    	
    	this.clientOrder = {
			id: ko.observable(),
			
			discount: ko.observable(),
			clientName: ko.observable(),
			formattedGrossTotal: ko.observable(),
			formattedDiscountTotal: ko.observable(),
			lessVat: ko.observable(),
			formattedLessVatAmount: ko.observable(),
			formattedAdditionalDiscount: ko.observable(),
			formattedNetTotal: ko.observable(),
			status: ko.observable()
    	};
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    ClientOrderPage.prototype.canActivate = function(clientOrderId) {
    	return system.defer(function (dfd) {
    		clientOrderService.getClientOrder(clientOrderId).done(function(clientOrder) {
    			dfd.resolve(clientOrder.client.id == app.user.id || app.user.userType.authority <= 2);
        	});
        })
        .promise();
    };
    
    ClientOrderPage.prototype.activate = function(clientOrderId) {
    	var self = this;
    	
    	self.clientOrder.id(clientOrderId);
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshClientOrderItemList();
		});
		
		self.refreshClientOrderItemList();
    };
    
    ClientOrderPage.prototype.refreshClientOrderItemList = function() {
    	var self = this;
    	
    	clientOrderService.getClientOrder(self.clientOrder.id()).done(function(clientOrder) {
    		self.clientOrder.discount(clientOrder.additionalDiscount);
    		self.clientOrder.clientName(clientOrder.client.formattedName);
    		self.clientOrder.formattedGrossTotal(clientOrder.formattedGrossTotal);
    		self.clientOrder.formattedDiscountTotal(clientOrder.formattedDiscountTotal);
    		self.clientOrder.lessVat(clientOrder.lessVat);
    		self.clientOrder.formattedLessVatAmount(clientOrder.formattedLessVatAmount);
    		self.clientOrder.formattedAdditionalDiscount(clientOrder.formattedAdditionalDiscountAmount);
    		self.clientOrder.formattedNetTotal(clientOrder.formattedNetTotal);
    		self.clientOrder.status(clientOrder.status.displayName);
    		
    		self.showCheckButton(clientOrder.status.name == 'CREATING');
    		self.showClientDiscount(clientOrder.additionalDiscount != 0);
    	});
    	
    	clientOrderItemService.getClientOrderItemList(self.currentPage(), self.clientOrder.id(), true).done(function(data) {
    		self.clientOrderItemList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    ClientOrderPage.prototype.add = function() {
    	var self = this;
    	
    	AddItem.show(self.clientOrder.id()).done(function() {
    		self.refreshClientOrderItemList();
    	});
    };
    
    ClientOrderPage.prototype.submit = function() {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to submit Order of <span class="text-primary">ID #' + self.clientOrder.id() + '</span>?</p>'
    			+ '<p class="text-danger">Once submitted order can be accepted anytime. Until accepted, order can still be edited.</p>',
				'<p class="text-primary">Submit Order</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				clientOrderService.submitClientOrder(self.clientOrder.id()).done(function(result) {
					self.refreshClientOrderItemList();
					app.showMessage(result.message);
				});
			}
		})
    };
    
    ClientOrderPage.prototype.remove = function(clientOrderItemId, displayName) {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to remove Item <span class="text-primary">' + displayName + '</span>?</p>',
				'<p class="text-danger">Confirm Remove</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				clientOrderItemService.removeItem(clientOrderItemId).done(function(result) {
					self.refreshClientOrderItemList();
					app.showMessage(result.message);
				});
			}
		})
    };
    
    ClientOrderPage.prototype.changePackageQuantity = function(clientOrderItemId, packageQuantity) {
    	var self = this;
    	
    	clientOrderItemService.changePackageQuantity(clientOrderItemId, packageQuantity).done(function(result) {
    		self.refreshClientOrderItemList();
    		if(!result.success) {
    			app.showMessage(result.message);
    		}
    	});
    };
    
    ClientOrderPage.prototype.changePieceQuantity = function(clientOrderItemId, pieceQuantity) {
    	var self = this;
    	
    	clientOrderItemService.changePieceQuantity(clientOrderItemId, pieceQuantity).done(function(result) {
    		self.refreshClientOrderItemList();
    		if(!result.success) {
    			app.showMessage(result.message);
    		}
    	});
    };
    
    return ClientOrderPage;
});