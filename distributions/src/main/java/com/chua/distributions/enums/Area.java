package com.chua.distributions.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Dec 4, 2016
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Area {
	
	ABU_DHABI("Abu Dhabi", VatType.VAT),
	
	AGUSAN_DEL_NORTE("Agusan Del Norte", VatType.VAT),
	
	AUSTRALIA("Australia", VatType.VAT),
	
	BAGUIO("Baguio (Benguet)", VatType.VAT),
	
	BATAAN("Bataan", VatType.VAT),
	
	BATANGAS("Batangas 1", VatType.VAT),
	
	BATANGAS_2("Batangas 2", VatType.VAT),
	
	BUKIDNON("Bukidnon", VatType.VAT),
	
	BULACAN("Bulacan", VatType.VAT),
	
	CAGAYAN("Cagayan", VatType.VAT),
	
	CAGAYAN_DE_ORO("Cagayan de Oro", VatType.VAT),
	
	CANADA("Group 1 Canada (British Columbia)", VatType.VAT),
	
	CANADA_2("Group 2 Canada (Alberta)", VatType.VAT),
	
	CANADA_3("Canada 3", VatType.VAT),
	
	CAVITE("Cavite", VatType.VAT),
	
	CEBU_CITY("Cebu City", VatType.VAT),
	
	CEBU_NORTH("Cebu North", VatType.VAT),
	
	CEBU_SOUTH("Cebu South", VatType.VAT),
	
	DAVAO_DEL_NORTE("Davao Del Norte", VatType.VAT),
	
	DAVAO_DEL_SUR("Davao Del Sur", VatType.VAT),
	
	DUBAI("Dubai", VatType.VAT),
	
	HAWAII("Hawaii", VatType.VAT),
	
	ILOCOS_NORTE("Ilocos Norte", VatType.VAT),
	
	ILOCOS_SUR("Ilocos Sur", VatType.VAT),
	
	ILOILO("Iloilo", VatType.VAT),
	
	ISABELA("Isabela", VatType.VAT),
	
	ITALY("Italy", VatType.VAT),
	
	LA_UNION("La Union", VatType.VAT),
	
	LAGUNA_NORTH("Laguna North", VatType.VAT),
	
	LANAO_DEL_NORTE("Lanao Del Norte", VatType.VAT),
	
	LEYTE("Leyte", VatType.VAT),
	
	MAGUINDANAO("Maguindanao", VatType.VAT),
	
	MAKATI("Makati", VatType.VAT),
	
	METRO_MANILA("MM GP1 Manila", VatType.VAT),
	
	METRO_MANILA_2("MM GP2 Manda, San Juan, Pasig, Marikina", VatType.VAT),
	
	METRO_MANILA_3("MM GP3 Camanava", VatType.VAT),
	
	METRO_MANILA_4("MM GP4 Las Pinas, Munti, Pasay, Paranaque, Pateros, Taguig", VatType.VAT),
	
	METRO_MANILA_5("MM GP5 Quezon City", VatType.VAT),
	
	NEGROS_OCCIDENTAL("Negros Occidental", VatType.VAT),
	
	NEW_ZEALAND("New Zealand", VatType.VAT),
	
	NUEVA_ECIJA("Nueva Ecija", VatType.VAT),
	
	PAMPANGA("Pampanga", VatType.VAT),
	
	PANGASINAN("Pangasinan", VatType.VAT),
	
	QATAR("Qatar", VatType.VAT),
	
	QUEZON_PROVINCE("Quezon Province", VatType.VAT),
	
	RIZAL_PROVINCE("Rizal Province", VatType.VAT),
	
	SAUDI_ARABIA("Saudi Arabia (Riyadh, Makkah, Madina", VatType.VAT),
	
	SAUDI_ARABIA_2("Saudi Arabia (Jeddah & East RGN", VatType.VAT),
	
	SOUTH_COTABATO("South Cotabato", VatType.VAT),
	
	SURIGAO_DEL_SUR("Surigao Del Sur", VatType.VAT),
	
	TARLAC("Tarlac", VatType.VAT),
	
	USA("USA", VatType.VAT),
	
	USA_SOUTHERN_CALIFORNIA("USA (Southern California)", VatType.VAT),
	
	ZAMBALES_SUBIC_OLONGAPO("Zambales Subic Olongapo", VatType.VAT),
	
	ZAMBOANGA_CITY("Zamboanga City", VatType.VAT),
	
	ZAMBOANGA_DEL_NORTE("Zamboanga Del Norte", VatType.VAT),
	
	ZAMBOANGA_DEL_SUR("Zamboanga Del Sur", VatType.VAT);
	
	private final String displayName;
	
	private final VatType vatType;
	
	private Area(final String displayName, final VatType vatType) {
		this.displayName = displayName;
		this.vatType = vatType;
	}
	
	public String getName() {
		return toString();
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public VatType getVatType() {
		return vatType;
	}
}
