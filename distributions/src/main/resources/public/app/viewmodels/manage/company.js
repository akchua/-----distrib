define(['durandal/app', 'knockout', 'modules/companyservice', 'viewmodels/manage/companyform'],
		function (app, ko, companyService, CompanyForm) {
    var Company = function() {
    	this.companyList = ko.observable();
    	
    	this.searchKey = ko.observable();
    	
    	this.itemsPerPage = ko.observable(app.user.itemsPerPage);
		this.totalItems = ko.observable();
		this.currentPage = ko.observable(1);
		this.currentPageSubscription = null;
    };
    
    Company.prototype.activate = function() {
    	var self = this;
    	
    	self.currentPage(1);
		self.currentPageSubscription = self.currentPage.subscribe(function() {
			self.refreshCompanyList();
		});
		
		self.refreshCompanyList();
    };
    
    Company.prototype.refreshCompanyList = function() {
    	var self = this;
    	
    	companyService.getCompanyList(self.currentPage(), self.searchKey()).done(function(data) {
    		self.companyList(data.list);
    		self.totalItems(data.total);
    	});
    };
    
    Company.prototype.add = function() {
    	var self = this;
    	
    	CompanyForm.show(new Object(), 'Create Company').done(function() {
    		self.refreshCompanyList();
    	});
    };
    
    Company.prototype.edit = function(companyId) {
    	var self = this;
    	
    	companyService.getCompany(companyId).done(function(company) {
    		CompanyForm.show(company, 'Edit Company').done(function() {
    			self.refreshCompanyList();
    		});
    	});
    };
    
    Company.prototype.remove = function(companyId, companyName) {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to remove Company <span class="text-primary">' + companyName + '</span>?</p>',
				'<p class="text-danger">Confirm Remove</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				companyService.removeCompany(companyId).done(function(result) {
					self.refreshCompanyList();
					app.showMessage(result.message);
				});
			}
		})
    };
    
    Company.prototype.search = function() {
    	var self = this;
    	
    	self.currentPage(1);
    	self.refreshCompanyList();
    };
    
    return Company;
});