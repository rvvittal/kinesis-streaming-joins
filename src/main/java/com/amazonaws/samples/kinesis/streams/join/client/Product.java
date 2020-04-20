package com.amazonaws.samples.kinesis.streams.join.client;

public class Product {
	private long productId;
	private String productName;
	private double productPrice;
	
	Product(long productId, String productName, String productPrice) {
		this.productId = productId;
		this.productName = productName;
		this.productPrice = Double.parseDouble(productPrice);
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public double getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(double productPrice) {
		this.productPrice = productPrice;
	}
	
	public String getProductAsCsv() {
		
		return this.productId +"," + this.productName +"," +this.productPrice;
		
	}
}
