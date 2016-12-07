define(['plugins/router', 'durandal/app', 'knockout', 'modules/securityservice', 'viewmodels/userform', 'viewmodels/passwordform', 'viewmodels/forgotpassword', 'viewmodels/settings'], 
		function (router, app, ko, securityService, UserForm, PasswordForm, ForgotPasswordForm, Settings) {
	var homeroute = [
	    { route: ['', 'home'], moduleId: 'viewmodels/home', title: 'Home', nav: true }
	];
	
	var userroute = [
 	    { route: 'user', moduleId: 'viewmodels/user/user', title: 'Users', nav: true, hash: '#user' }
 	];
	
	var manageroute = [
   	  	{ route: 'manage', moduleRootId: 'viewmodels/manage', title: '', nav: true, hash: '#manage',
   	  		childRoutes: [
   	  		    { route: 'category', moduleId: 'category', title: 'Categories', nav: true, hash: 'category' },
   	  		    { route: 'company', moduleId: 'company', title: 'Companies', nav: true, hash: 'company' },
   	  		    { route: 'product', moduleId: 'product', title: 'Products', nav: true, hash: 'product' }
   	  		]
   	  	}
   	];
	
	var purchaseorderroute = [
	    { route: 'purchaseorder', moduleId: 'viewmodels/purchaseorder/purchaseorder', title: 'Purchase Order', nav: true, hash: '#purchaseorder' }
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
	    			self.routes = self.routes.concat(purchaseorderroute);
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
    	
        self.router.map(self.routes).buildNavigationModel();
        
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
		
		UserForm.show(app.user, 'My Profile').done(function() {
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
		var self = this;
		
		UserForm.show(new Object(), 'Registration');
	};
	
	return Shell;
});