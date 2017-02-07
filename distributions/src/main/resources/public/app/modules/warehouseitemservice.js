define(['jquery'], function ($) {
	return {
		getWarehouseItemList: function(currentPage, searchKey, warehouse, async) {
			return $.ajax({
				url: '/services/warehouseitem/warehouseitemlist',
				async: async,
				data: {
					pageNumber: currentPage - 1,
					searchKey: searchKey,
					warehouse, warehouse
				}
			});
		},
		
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