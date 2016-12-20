define(['jquery'], function ($) {
	return {
		getUser: function(userId) {
			return $.ajax({
				url: '/services/user/get',
				data: {
					userId: userId
				}
			});
		},
		
		getUserList: function(currentPage, searchKey) {
			return $.ajax({
				url: '/services/user/list',
				data: {
					pageNumber: currentPage - 1,
					searchKey: searchKey
				}
			});
		},
		
		getClientList: function(currentPage, searchKey) {
			return $.ajax({
				url: '/services/user/clientlist',
				data: {
					pageNumber: currentPage - 1,
					searchKey: searchKey
				}
			});
		},
		
		retrieveUser: function(retrievalKey) {
			return $.ajax({
				url: '/services/user/userretrieval',
				data: {
					retrievalKey: retrievalKey
				}
			});
		},
		
		saveUser: function(userFormData) {
    		return $.ajax({
    			url: '/services/user/save',
    			method: 'POST',
    			data: {
    				userFormData: userFormData
    			} 
    		});
    	},
    	
    	removeUser: function(userId) {
    		return $.ajax({
    			url: '/services/user/remove',
    			method: 'POST',
    			data: {
    				userId: userId
    			}
    		});
    	},
    	
    	changePassword: function(passwordFormData) {
    		return $.ajax({
    			url: '/services/user/changepassword',
    			method: 'POST',
    			data: {
    				passwordFormData: passwordFormData
    			} 
    		});
    	},
    	
    	resetPassword: function(userId) {
    		return $.ajax({
    			url: '/services/user/resetpassword',
    			method: 'POST',
    			data: {
    				userId: userId
    			}
    		});
    	},
    	
    	changeSettings: function(settingsFormData) {
    		return $.ajax({
    			url: '/services/user/changesettings',
    			method: 'POST',
    			data: {
    				settingsFormData: settingsFormData
    			} 
    		});
    	},
    	
    	changeClientSettings: function(clientSettingsFormData) {
    		return $.ajax({
    			url: '/services/user/changeclientsettings',
    			method: 'POST',
    			data: {
    				clientSettingsFormData: clientSettingsFormData
    			} 
    		});
    	},
    	
    	getUserTypeList: function() {
    		return $.ajax({
    			url: '/services/user/usertype'
    		});
    	},
    	
    	getAreaList: function() {
    		return $.ajax({
    			url: '/services/user/area'
    		});
    	},
    	
    	getVatTypeList: function() {
    		return $.ajax({
    			url: '/services/user/vattype'
    		});
    	}
	};
});