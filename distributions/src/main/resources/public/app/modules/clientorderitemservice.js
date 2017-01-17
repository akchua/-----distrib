define(['jquery'], function ($) {
	return {
		getClientOrderItemList: function(currentPage, clientOrderId, async) {
			return $.ajax({
				url: '/services/clientorderitem/list',
				async: async,
				data: {
					pageNumber: currentPage - 1,
					clientOrderId: clientOrderId
				}
			});
		},
		
		getClientOrderItemByProductList: function(currentPage, productId, async) {
			return $.ajax({
				url: '/services/clientorderitem/listbyproduct',
				async: async,
				data: {
					pageNumber: currentPage - 1,
					productId: productId
				}
			});
		},
		
		addItem: function(productId, clientOrderId) {
			return $.ajax({
				url: '/services/clientorderitem/additem',
				method: 'POST',
				data: {
					productId: productId,
					clientOrderId: clientOrderId
				}
			});
		},
		
		removeItem: function(clientOrderItemId) {
			return $.ajax({
				url: '/services/clientorderitem/removeitem',
				method: 'POST',
				data: {
					clientOrderItemId: clientOrderItemId
				}
			});
		},
		
		changePieceQuantity: function(clientOrderItemId, pieceQuantity) {
			return $.ajax({
				url: '/services/clientorderitem/changepiece',
				method: 'POST',
				data: {
					clientOrderItemId: clientOrderItemId,
					pieceQuantity: pieceQuantity
				}
			});
		},
		
		changePackageQuantity: function(clientOrderItemId, packageQuantity) {
			return $.ajax({
				url: '/services/clientorderitem/changepackage',
				method: 'POST',
				data: {
					clientOrderItemId: clientOrderItemId,
					packageQuantity: packageQuantity
				}
			});
		},
		
		transferPiece: function(clientOrderItemId, destinationOrderId) {
			return $.ajax({
				url: '/services/clientorderitem/transferpiece',
				method: 'POST',
				data: {
					clientOrderItemId: clientOrderItemId,
					destinationOrderId: destinationOrderId
				}
			});
		},
		
		transferPackage: function(clientOrderItemId, destinationOrderId) {
			return $.ajax({
				url: '/services/clientorderitem/transferpackage',
				method: 'POST',
				data: {
					clientOrderItemId: clientOrderItemId,
					destinationOrderId: destinationOrderId
				}
			});
		},
		
		transferAll: function(clientOrderItemId, destinationOrderId) {
			return $.ajax({
				url: '/services/clientorderitem/transferall',
				method: 'POST',
				data: {
					clientOrderItemId: clientOrderItemId,
					destinationOrderId: destinationOrderId
				}
			});
		}
	};
});