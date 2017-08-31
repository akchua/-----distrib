define(['jquery'], function ($) {
	return {
		getBusinessOfficialEmail: function() {
			return $.ajax({
				url: '/services/publicconstants/businessofficialemail'
			});
		},
		
		getBusinessPrimaryContactNumber: function() {
			return $.ajax({
				url: '/services/publicconstants/businessprimarycontact'
			});
		},
		
		getSystemVersion: function() {
			return $.ajax({
				url: '/services/publicconstants/systemversion'
			});
		}
	};
});