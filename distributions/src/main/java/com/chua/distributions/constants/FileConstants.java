package com.chua.distributions.constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 17, 2016
 */
@Component
public class FileConstants {

	private final String fileHome;
	
	private final String priceListHome;
	
	private final String clientRankingHome;
	
	private final String salesHome;

	private final String purchasesHome;

	private final String dispatchHome;
	
	private final String productImageHome;
	
	private final String imageDefaultFileName;
	
	@Autowired
	public FileConstants(@Value("${file.home}") String fileHome,
						@Value("${file.image.defaultFileName}") String imageDefaultFileName) {
		this.fileHome = fileHome;
		this.priceListHome = fileHome + "files/price_list/";
		this.clientRankingHome = fileHome + "files/client_ranking/";
		this.salesHome = fileHome + "files/sales_report/";
		this.purchasesHome = fileHome + "files/purchase_order/";
		this.dispatchHome = fileHome + "files/dispatch/";
		this.productImageHome = fileHome + "program_data/product_image/";
		this.imageDefaultFileName = imageDefaultFileName;
	}

	public String getFileHome() {
		return fileHome;
	}

	public String getPriceListHome() {
		return priceListHome;
	}

	public String getClientRankingHome() {
		return clientRankingHome;
	}

	public String getSalesHome() {
		return salesHome;
	}

	public String getPurchasesHome() {
		return purchasesHome;
	}

	public String getDispatchHome() {
		return dispatchHome;
	}

	public String getProductImageHome() {
		return productImageHome;
	}

	public String getImageDefaultFileName() {
		return imageDefaultFileName;
	}
}
