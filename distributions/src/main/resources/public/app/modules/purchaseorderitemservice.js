define(['jquery'], function ($) {
	return {
		getPurchaseOrderItemList: function(currentPage, purchaseOrderId, async) {
			return $.ajax({
				url: '/services/purchaseorderitem/list',
				async: async,
				data: {
					pageNumber: currentPage - 1,
					purchaseOrderId: purchaseOrderId
				}
			});
		},
		
		getPurchaseOrderItemByProductList: function(currentPage, productId, async) {
			return $.ajax({
				url: '/services/purchaseorderitem/listbyproduct',
				async: async,
				data: {
					pageNumber: currentPage - 1,
					productId: productId
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
		},
		
		transferPiece: function(purchaseOrderItemId, destinationOrderId) {
			return $.ajax({
				url: '/services/purchaseorderitem/transferpiece',
				method: 'POST',
				data: {
					purchaseOrderItemId: purchaseOrderItemId,
					destinationOrderId: destinationOrderId
				}
			});
		},
		
		transferPackage: function(purchaseOrderItemId, destinationOrderId) {
			return $.ajax({
				url: '/services/purchaseorderitem/transferpackage',
				method: 'POST',
				data: {
					purchaseOrderItemId: purchaseOrderItemId,
					destinationOrderId: destinationOrderId
				}
			});
		},
		
		transferAll: function(purchaseOrderItemId, destinationOrderId) {
			return $.ajax({
				url: '/services/purchaseorderitem/transferall',
				method: 'POST',
				data: {
					purchaseOrderItemId: purchaseOrderItemId,
					destinationOrderId: destinationOrderId
				}
			});
		}
	};
});