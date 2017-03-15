define(['durandal/app', 'durandal/system', 'knockout', 'modules/clientorderservice', 'modules/clientorderitemservice', 'viewmodels/clientorder/additem'],
		function (app, system, ko, clientOrderService, clientOrderItemService, AddItem) {
    var ClientOrderPage = function() {
    	this.partialClientOrderItemList = ko.observable();
    	this.companyId = null;
    	
    	this.showCheckButton = ko.observable(false);
    	this.showClientDiscount = ko.observable(true);
    	
    	this.clientOrder = {
			id: ko.observable(),
			
			creatorName: ko.observable(),
			clientName: ko.observable(),
			companyName: ko.observable(),
			formattedGrossTotal: ko.observable(),
			formattedDiscountTotal: ko.observable(),
			lessVat: ko.observable(),
			formattedLessVatAmount: ko.observable(),
			additionalDiscount: ko.observable(),
			formattedAdditionalDiscount: ko.observable(),
			status: ko.observable(),
			formattedNetTotal: ko.observable()
    	};
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    ClientOrderPage.prototype.canActivate = function(clientOrderId) {
    	return system.defer(function (dfd) {
    		clientOrderService.getPartialClientOrder(clientOrderId).done(function(partialClientOrder) {
    			dfd.resolve(partialClientOrder.clientId == app.user.id || partialClientOrder.creatorId == app.user.id);
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
    	
    	clientOrderService.getPartialClientOrder(self.clientOrder.id()).done(function(partialClientOrder) {
    		self.companyId = partialClientOrder.companyId;
    		
    		self.clientOrder.creatorName(partialClientOrder.creatorName);
    		self.clientOrder.clientName(partialClientOrder.clientName);
    		self.clientOrder.companyName(partialClientOrder.companyName);
    		self.clientOrder.formattedGrossTotal(partialClientOrder.formattedGrossTotal);
    		self.clientOrder.formattedDiscountTotal(partialClientOrder.formattedDiscountTotal);
    		self.clientOrder.lessVat(partialClientOrder.lessVat);
    		self.clientOrder.formattedLessVatAmount(partialClientOrder.formattedLessVatAmount);
    		self.clientOrder.additionalDiscount(partialClientOrder.additionalDiscount);
    		self.clientOrder.formattedAdditionalDiscount(partialClientOrder.formattedAdditionalDiscount);
    		self.clientOrder.status(partialClientOrder.status.displayName);
    		self.clientOrder.formattedNetTotal(partialClientOrder.formattedNetTotal);
    		
    		self.showCheckButton(partialClientOrder.status.name == 'CREATING');
    		self.showClientDiscount(partialClientOrder.additionalDiscount != 0);
    	});
    	
    	clientOrderItemService.getPartialClientOrderItemList(self.currentPage(), self.clientOrder.id(), true).done(function(data) {
    		self.partialClientOrderItemList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    ClientOrderPage.prototype.add = function() {
    	var self = this;
    	
    	AddItem.show(self.clientOrder.id(), self.companyId).done(function() {
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