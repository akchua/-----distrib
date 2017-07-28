define(['plugins/dialog', 'durandal/app', 'knockout', 'modules/productservice'], 
		function (dialog, app, ko, productService) {
    var ProductGallery = function(productId) {
    	this.productId = productId;
    	this.productImageList = ko.observableArray();
    	
    	this.productModel = {
    		displayName: ko.observable(),
    		image: ko.observable(),
    		
    		companyName: ko.observable(),
    		categoryName: ko.observable()
	    };
    };
    
    ProductGallery.prototype.activate = function() {
    	var self = this;
    	
    	self.refreshProduct();
    	self.refreshProductImageList();
    };
    
    ProductGallery.show = function(product) {
    	return dialog.show(new ProductGallery(product));
    };
    
    ProductGallery.prototype.refreshProduct = function() {
    	var self = this;
    	
    	productService.getProduct(self.productId, null).done(function(product) {
        	self.productModel.displayName(product.displayName);
        	self.productModel.image(productService.getProductImageByFileName(product.image));
        	
        	self.productModel.companyName(product.company.name);
        	self.productModel.categoryName(product.category.name);
    	});
    };
    
    ProductGallery.prototype.refreshProductImageList = function() {
    	var self = this;
    	
    	productService.getProductImageList(self.productId).done(function(productImageList) {
    		for(i = 0; i < productImageList.length; i++) {
    			productImageList[i].filePath = productService.getProductImageByFileName(productImageList[i].fileName);
    		}
    		self.productImageList(productImageList);
    	});
    };
    
    ProductGallery.prototype.uploadProductImage = function(imageFile) {
    	var self = this;
    	
    	productService.uploadProductImage(self.productId, imageFile).done(function(result) {
    		if(result.success) {
    			self.refreshProductImageList();
    		}
    		app.showMessage(result.message);
    	});
    };
    
    ProductGallery.prototype.setProductImageAsThumbnail = function(productImageId) {
    	var self = this;
    	
    	productService.setProductImageAsThumbnail(productImageId).done(function(result) {
    		if(result.success) {
    			self.refreshProduct();
    		} else {
    			app.showMessage(result.message);
    		}
    	});
    };
    
    ProductGallery.prototype.removeProductImage = function(productImageId) {
    	var self = this;
    	
    	app.showMessage('<p>Are you sure you want to remove this image?</p>',
				'<p class="text-danger">Confirm Remove</p>',
				[{ text: 'Yes', value: true }, { text: 'No', value: false }])
		.then(function(confirm) {
			if(confirm) {
				productService.removeProductImage(productImageId).done(function(result) {
					self.refreshProduct();
					self.refreshProductImageList();
					app.showMessage(result.message);
				});
			}
		})
    };
    
    ProductGallery.prototype.cancel = function() {
        dialog.close(this);
    };
    
    return ProductGallery;
});