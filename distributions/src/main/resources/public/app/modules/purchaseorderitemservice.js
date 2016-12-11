define(['jquery'], function ($) {
	return {
		getPurchaseOrderItemList: function(currentPage, purchaseOrderId) {
			return $.ajax({
				url: '/services/purchaseorderitem/list',
				data: {
					pageNumber: currentPage - 1,
					purchaseOrderId: purchaseOrderId
				}
			});
		},
		
		addItem: function(productId, purchaseOrderId) {
			return $.ajax({
				url: '/services/purchaseorderitem/additem',
				method: 'POST',
				data: {
					productId: productId,
					purchaseOrderId: purchaseOrderId
				}
			});
		},
		
		removeItem: function(purchaseOrderItemId) {
			return $.ajax({
				url: '/services/purchaseorderitem/removeitem',
				method: 'POST',
				data: {
					purchaseOrderItemId: purchaseOrderItemId
				}
			});
		},
		
		changePieceQuantity: function(purchaseOrderItemId, pieceQuantity) {
			return $.ajax({
				url: '/services/purchaseorderitem/changepiece',
				method: 'POST',
				data: {
					purchaseOrderItemId: purchaseOrderItemId,
					pieceQuantity: pieceQuantity
				}
			});
		},
		
		changePackageQuantity: function(purchaseOrderItemId, packageQuantity) {
			return $.ajax({
				url: '/services/purchaseorderitem/changepackage',
				method: 'POST',
				data: {
					purchaseOrderItemId: purchaseOrderItemId,
					packageQuantity: packageQuantity
				}
			});
		}
	};
});