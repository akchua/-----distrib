define(['jquery'], function ($) {
	return {
		getWarehouseItemList: function(currentPage, searchKey, warehouseId, async) {
			return $.ajax({
				url: '/services/warehouseitem/warehouseitemlist',
				async: async,
				data: {
					pageNumber: currentPage - 1,
					searchKey: searchKey,
					warehouseId, warehouseId
				}
			});
		},
		
		getFormattedPurchaseValue: function(warehouseId) {
			return $.ajax({
				url: '/services/warehouseitem/formattedpurchasevalue',
				data: {
					warehouseId: warehouseId
				}
			});
		}
	};
});