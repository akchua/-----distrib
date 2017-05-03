define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/companyservice'], function (dialog, app, ko, companyService) {
    var CompanyForm = function(company, title) {
    	this.company = company;
    	this.title = title;
    	
    	this.companyFormModel = {
    		id: ko.observable(),
    		
    		name: ko.observable(),
    		
    		contactPerson: ko.observable(),
    		contactNumber: ko.observable(),
    		emailAddress: ko.observable(),
    		reportReceiver: ko.observable()
	    };
    };
    
    CompanyForm.prototype.activate = function() {
    	var self = this;
    	
    	self.companyFormModel.id(self.company.id);
    	self.companyFormModel.name(self.company.name);
    	self.companyFormModel.contactPerson(self.company.contactPerson);
    	self.companyFormModel.contactNumber(self.company.contactNumber);
    	self.companyFormModel.emailAddress(self.company.emailAddress);
    	self.companyFormModel.reportReceiver(self.company.reportReceiver);
    };
    
    CompanyForm.show = function(company, title) {
    	return dialog.show(new CompanyForm(company, title));
    };
    
    CompanyForm.prototype.save = function() {
    	var self = this;
    	
        companyService.saveCompany(ko.toJSON(self.companyFormModel)).done(function(result) {
        	if(result.success) {
        		dialog.close(self);	
        	} 
        	app.showMessage(result.message);
        });
    };
    
    CompanyForm.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return CompanyForm;
});