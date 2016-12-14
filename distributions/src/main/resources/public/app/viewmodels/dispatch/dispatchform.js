define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/dispatchservice'], function (dialog, app, ko, dispatchService) {
    var DispatchForm = function() {
    	this.warehouseList = ko.observable();
    	
    	this.dispatchFormModel = {
    		id: ko.observable(),
    		
    		warehouse: ko.observable()
	    };
    };
    
    DispatchForm.prototype.activate = function() {
    	var self = this;
    	
    	dispatchService.getWarehouseList().done(function(warehouseList) {
			self.warehouseList(warehouseList);
		});
    };
    
    DispatchForm.show = function() {
    	return dialog.show(new DispatchForm());
    };
    
    DispatchForm.prototype.save = function() {
    	var self = this;
    	
        dispatchService.saveDispatch(ko.toJSON(self.dispatchFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    DispatchForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return DispatchForm;
});