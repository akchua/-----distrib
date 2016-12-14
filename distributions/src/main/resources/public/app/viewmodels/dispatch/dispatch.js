define(['plugins/router', 'durandal/app', 'knockout', 'modules/dispatchservice', 'viewmodels/dispatch/dispatchform'],
		function (router, app, ko, dispatchService, DispatchForm) {
    var Dispatch = function() {
    	this.dispatchList = ko.observable();
    	
    	this.showReceived = ko.observable(false);
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    Dispatch.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshDispatchList();
		});
		
		self.refreshDispatchList();
    };
    
    Dispatch.prototype.refreshDispatchList = function() {
    	var self = this;
    	
    	dispatchService.getDispatchList(self.currentPage(), self.showReceived()).done(function(data) {
    		self.dispatchList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    Dispatch.prototype.add = function() {
    	var self = this;
    	
    	DispatchForm.show().done(function() {
    		self.refreshDispatchList();
    	});
    };
    
    Dispatch.prototype.complete = function(dispatchId) {
    	var self = this;
    	
    	app.showMessage('<p>Please make sure that all of the orders within Dispatch of <span class="text-primary">ID #' + dispatchId + '</span> are all received by their designated client.</p>',
				'<p class="text-warning">Complete Dispatch</p>',
				[{ text: 'Continue', value: true }, { text: 'Cancel', value: false }])
		.then(function(confirm) {
			if(confirm) {
				dispatchService.completeDispatch(dispatchId).done(function(result) {
					self.refreshDispatchList();
					app.showMessage(result.message);
				});
			}
		})
    };
    
    Dispatch.prototype.open = function(dispatchId) {
    	router.navigate('#dispatchpage/' + dispatchId);
    };
    
    Dispatch.prototype.remove = function(dispatchId) {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to remove Dispatch of <span class="text-primary">ID #' + dispatchId + '</span>?</p>',
				'<p class="text-danger">Confirm Remove</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				dispatchService.removeDispatch(dispatchId).done(function(result) {
					self.refreshDispatchList();
					app.showMessage(result.message);
				});
			}
		})
    };
    
    return Dispatch;
});