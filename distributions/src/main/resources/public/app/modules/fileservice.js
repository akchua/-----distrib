define(['jquery', 'fileDownload'], function ($, fd) {
	return {
		downloadSalesReportByFileName: function(fileName) {
			fd.fileDownload('/services/file/salesreport/' + fileName)
			return false;
    	},
    	
    	downloadPriceListByFileName: function(fileName) {
			fd.fileDownload('/services/file/pricelist/' + fileName)
			return false;
    	}
	};
});
