define(['plugins/dialog', 'durandal/app', 'knockout'], 
		function (dialog, app, ko) {
    var TimestampView = function(title, order) {
    	this.title = title;
    	this.order = order;
    	
    	this.timestampModel = {
    		id: ko.observable(),
    		
    		formattedCreatedOn: ko.observable(),
    		formattedRequestedOn: ko.observable(),
    		formattedDeliveredOn: ko.observable(),
    		formattedPaidOn: ko.observable()
	    };
    };
    
    TimestampView.prototype.activate = function() {
    	var self = this;
    	
    	self.timestampModel.id(self.order.id);
    	self.timestampModel.formattedCreatedOn(self.order.formattedCreatedOn);
    	self.timestampModel.formattedRequestedOn(self.order.formattedRequestedOn);
    	self.timestampModel.formattedDeliveredOn(self.order.formattedDeliveredOn);
    	self.timestampModel.formattedPaidOn(self.order.formattedPaidOn);
    };
    
    TimestampView.show = function(title, order) {
    	return dialog.show(new TimestampView(title, order));
    };
    
    TimestampView.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return TimestampView;
});