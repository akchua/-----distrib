define(['jquery'], function ($) {
	return {
		getCompany: function(companyId) {
			return $.ajax({
				url: '/services/company/get',
				data: {
					companyId: companyId
				}
			});
		},
		
		getCompanyList: function(currentPage, searchKey) {
			return $.ajax({
				url: '/services/company/list',
				data: {
					pageNumber: currentPage - 1,
					searchKey: searchKey
				}
			});
		},
		
		getCompanyListByName: function() {
			return $.ajax({
				url: '/services/company/listbyname'
			});
		},
		
		saveCompany: function(companyFormData) {
    		return $.ajax({
    			url: '/services/company/save',
    			method: 'POST',
    			data: {
    				companyFormData: companyFormData
    			} 
    		});
    	},
    	
    	removeCompany: function(companyId) {
    		return $.ajax({
    			url: '/services/company/remove',
    			method: 'POST',
    			data: {
    				companyId: companyId
    			}
    		});
    	}
	};
});