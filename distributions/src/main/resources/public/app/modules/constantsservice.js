define(['jquery'], function ($) {
	return {
		getBusinessOfficialEmail: function() {
			return $.ajax({
				url: '/services/constants/businessofficialemail'
			});
		},
		
		getBusinessPrimaryContactNumber: function() {
			return $.ajax({
				url: '/services/constants/businessprimarycontact'
			});
		}
	};
});