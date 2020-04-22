package com.amazonaws.samples.kinesis.streams.join.model;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Product {
	
	private final static ObjectMapper JSON = new ObjectMapper();
	static {
		JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	
	private long productId;
	private String productName;
	private double productPrice;
	
	public Product(long productId, String productName, String productPrice) {
		this.productId = productId;
		this.productName = productName;
		this.productPrice = Double.parseDouble(productPrice);
	}
	
	public Product() {
		
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
	
	public String toJsonAsString() {
		try {
			return JSON.writeValueAsString(this);
		} catch (IOException e) {
			return null;
		}
	}
	
	
	
	/*
	public String getProductAsCsv() {
		
		return this.productId +"," + this.productName +"," +this.productPrice;
		
	}
	*/
}
