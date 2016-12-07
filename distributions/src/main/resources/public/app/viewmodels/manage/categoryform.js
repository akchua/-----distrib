define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/categoryservice'], function (dialog, app, ko, categoryService) {
    var CategoryForm = function(category, title) {
    	this.category = category;
    	this.title = title;
    	
    	this.categoryFormModel = {
    		id: ko.observable(),
    		
    		name: ko.observable()
	    };
    };
    
    CategoryForm.prototype.activate = function() {
    	var self = this;
    	
    	self.categoryFormModel.id(self.category.id);
    	self.categoryFormModel.name(self.category.name);
    };
    
    CategoryForm.show = function(category, title) {
    	return dialog.show(new CategoryForm(category, title));
    };
    
    CategoryForm.prototype.save = function() {
    	var self = this;
    	
        categoryService.saveCategory(ko.toJSON(self.categoryFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    CategoryForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return CategoryForm;
});