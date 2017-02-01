define(['jquery'], function ($) {
	return {
		getFormattedPurchaseValue: function(warehouse) {
			return $.ajax({
				url: '/services/warehouseitem/formattedpurchasevalue',
				data: {
					warehouse: warehouse
				}
			});
		}
	};
});