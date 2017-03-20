define(['plugins/router', 'durandal/app', 'knockout', 'modules/securityservice', 'viewmodels/userform', 'viewmodels/profile', 'viewmodels/passwordform', 'viewmodels/forgotpassword', 'viewmodels/settings'], 
		function (router, app, ko, securityService, UserForm, Profile, PasswordForm, ForgotPasswordForm, Settings) {
	var homeroute = [
	    { route: ['', 'home'], moduleId: 'viewmodels/home', title: 'Home', nav: false }
	];
	
	var ourproductsroute = [
	    { route: 'ourproducts', moduleId: 'viewmodels/ourproducts/ourproducts', title: 'Our Products', nav: true, hash: '#ourproducts' }                    
	];
	
	var userroute = [
 	    { route: 'user', moduleId: 'viewmodels/user/user', title: 'Users', nav: true, hash: '#user' }
 	];
	
	var manageroute = [
   	  	{ route: 'manage', moduleRootId: 'viewmodels/manage', title: '', nav: true, hash: '#manage',
   	  		childRoutes: [
   	  		    { route: 'category', moduleId: 'category', title: 'Categories', nav: true, hash: 'category' },
   	  		    { route: 'client', moduleId: 'client', title: 'Clients', nav: true, hash: 'client' },
   	  		    { route: 'company', moduleId: 'company', title: 'Companies', nav: true, hash: 'company' },
   	  		    { route: 'inventory', moduleId: 'inventory', title: 'Inventory', nav: true, hash: 'inventory' },
   	  		    { route: 'product', moduleId: 'product', title: 'Products', nav: true, hash: 'product' }
   	  		]
   	  	},
   	  	{ route: 'pricesettings', moduleId: 'viewmodels/manage/pricesettings', title: '', nav: false, hash: '#pricesettings' }
   	];
	
	var reportroute = [
		{ route: 'report', moduleRootId: 'viewmodels/report', title: 'Reports', nav: true, hash: '#report',
			childRoutes: [
				{ route: 'purchase', moduleId: 'purchase', title: 'Purchases', nav: true, hash: 'purchase' },
   	  		    { route: 'sale', moduleId: 'sale', title: 'Sales', nav: true, hash: 'sale' },
   	  		    { route: 'warehouse', moduleId: 'warehouse', title: 'Warehouse', nav: true, hash: 'warehouse' }
			]
		}
	];
	
	var paymentroute = [
  	    { route: 'payment', moduleId: 'viewmodels/payment/payment', title: 'Payments', nav: true, hash: '#payment' }
  	];
	
	var purchaseorderroute = [
	    { route: 'purchaseorder', moduleId: 'viewmodels/purchaseorder/purchaseorder', title: 'Purchase Order', nav: true, hash: '#purchaseorder' },
	    { route: 'purchaseorderpage/:id', moduleId: 'viewmodels/purchaseorder/purchaseorderpage', title: 'Purchase Order', nav: false, hash: '#purchaseorderpage' }
	];
	
	var requestroute = [
		{ route: 'request', moduleRootId: 'viewmodels/request', title: 'Requests', nav: true, hash: '#request',
			childRoutes: [
				{ route: 'accept', moduleId: 'request', title: 'Accept', nav: true, hash: 'accept' },
   	  		    { route: 'create', moduleId: 'create', title: 'Create', nav: true, hash: 'create' }
			]
		},
		{ route: 'clientorderpage/:id', moduleId: 'viewmodels/clientorder/clientorderpage', title: 'Order', nav: false, hash: '#clientorderpage' }
	];
	
	var dispatchroute = [
	    { route: 'dispatch', moduleId: 'viewmodels/dispatch/dispatch', title: 'Dispatch', nav: true, hash: '#dispatch' },
	    { route: 'dispatchpage/:id', moduleId: 'viewmodels/dispatch/dispatchpage', title: 'Dispatch', nav: false, hash: '#dispatchpage' }
	]; 
	
	var clientorderroute = [
	    { route: 'clientorder', moduleId: 'viewmodels/clientorder/clientorder', title: 'Order', nav: true, hash: '#clientorder' },
	    { route: 'clientorderpage/:id', moduleId: 'viewmodels/clientorder/clientorderpage', title: 'Order', nav: false, hash: '#clientorderpage' }
	];
	
	var Shell = function() {
		this.router = router;
		
		this.routes = homeroute;
		this.errorMessage = ko.observable();
		
		this.userDetails = {
			id: ko.observable(),
			fullName: ko.observable(),
			userType: ko.observable(),
			itemsPerPage: ko.observable()
		};
		
		this.loginForm = {
			username: ko.observable(),
			password: ko.observable()
		};
	};
	
	Shell.prototype.activate = function() {
		var self = this;
		
		if(app.user) {
			self.userDetails.id(app.user.id);
    		self.userDetails.fullName(app.user.fullName);
    		self.userDetails.userType(app.user.userType);
    		self.userDetails.itemsPerPage(app.user.itemsPerPage);
    		
    		switch(app.user.userType.name) {
	    		case 'ADMINISTRATOR':
	    			self.routes = self.routes.concat(userroute);
	    		case 'MANAGER':
	    			self.routes = self.routes.concat(manageroute);
	    			self.routes = self.routes.concat(reportroute);
	    			self.routes = self.routes.concat(paymentroute);
	    		case 'SECRETARY':
	    			self.routes = self.routes.concat(purchaseorderroute);
	    			self.routes = self.routes.concat(requestroute);
	    			self.routes = self.routes.concat(dispatchroute);
	    			if(app.user.userType.name == 'SECRETARY') self.routes = self.routes.concat(ourproductsroute);
	    			break;
	    		case 'SUPERVISOR':
	    			self.routes = self.routes.concat(reportroute);
	    			self.routes = self.routes.concat(ourproductsroute);
	    			break;
	    		case 'CLIENT':
	    			self.routes = self.routes.concat(clientorderroute);
	    			self.routes = self.routes.concat(ourproductsroute);
	    			break;
    		}
		}
    		
    	$.each(self.routes, function(index, route) {
            if (route.childRoutes === undefined)
                return
            $.each(route.childRoutes, function(index, childRoute) {
                childRoute.route = route.route + '/' + childRoute.route;
                childRoute.moduleId = route.moduleRootId + '/' + childRoute.moduleId;
                childRoute.title = childRoute.title;
                childRoute.hash = route.hash + '/' + childRoute.hash;
                childRoute.parent = route.moduleRootId;
            });
            self.routes = self.routes.concat(route.childRoutes);
        });
    	
        self.router.map(self.routes)
        	.buildNavigationModel()
        	.mapUnknownRoutes('viewmodels/home');
        
        return router.activate();
	};
	
	Shell.prototype.refreshUser = function() {
		var self = this;
		
		securityService.getUser().done(function(user) {
    		app.user = user;
    		self.userDetails.id(user.id);
    		self.userDetails.fullName(user.fullName);
    		self.userDetails.userType(user.userType);
    		self.userDetails.itemsPerPage(user.itemsPerPage);
        });
	};
	
	Shell.prototype.login = function() {
		var self = this;
		
		self.errorMessage('');
		securityService.login(self.loginForm.username(), self.loginForm.password()).done(function(data) {
			if(data == 'SUCCESS') {
				location.href = '/';
			} else { // FAILURE
				self.errorMessage('Invalid Username / Password!');
				self.loginForm.password('');
			}
		});
	};
	
	Shell.prototype.profile = function() {
		var self = this;
		
		Profile.show(app.user).done(function() {
			self.refreshUser();
		});
	};
	
	Shell.prototype.changePassword = function() {
		var self = this;
		
		PasswordForm.show();
	};
	
	Shell.prototype.forgotPassword = function() {
		var self = this;
		
		ForgotPasswordForm.show();
	};
	
	Shell.prototype.changeSettings = function() {
		var self = this;
		
		Settings.show().done(function() {
			self.refreshUser();
		});
	};
	
	Shell.prototype.logout = function() {
		securityService.logout().done(function() {
    		location.href = '/';
    	});
	};
	
	Shell.prototype.register = function() {
		/*UserForm.show(new Object(), 'Registration');*/
		app.showMessage('<p>To register please email us at <span class="text-primary">primepad.pampanga@gmail.com</span> or Call/Text <span class="text-primary">+63 926 615 9373</span>.'
				+ ' Make sure to include <span class="text-primary">Contact Person, Contact Number, Business Name and Business Address</span> in your message.</p>'
				+ '<p/>'
				+ '<p>To ensure the quality of our service, we make sure to meet up with new clients before accepting any order requests.</p>');
	};
	
	return Shell;
});