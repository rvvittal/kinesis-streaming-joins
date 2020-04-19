package com.amazonaws.samples.kinesis.streams.join.model;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OrderItem  {

	private final static ObjectMapper JSON = new ObjectMapper();
	static {
		JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	private static String ORDER_ITEM_REC_TYPE = "OrderItem";
	
	private static DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
	

	
	private long orderId;
	private long itemId;
	private long itemQuantity;
	private double itemAmount;
	private String itemStatus;
	LocalDateTime orderDateTime;
	private String recordType;
	
	
	
	public OrderItem() {
		this.recordType = ORDER_ITEM_REC_TYPE;
		
	}



	public long getOrderId() {
		return orderId;
	}



	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}



	public long getItemId() {
		return itemId;
	}



	public void setItemId(long itemId) {
		this.itemId = itemId;
	}






	public long getItemQuantity() {
		return itemQuantity;
	}



	public void setItemQuantity(long itemQuantity) {
		this.itemQuantity = itemQuantity;
	}



	public double getItemAmount() {
		return itemAmount;
	}



	public void setItemAmount(double itemAmount) {
		this.itemAmount = itemAmount;
	}



	public String getRecordType() {
		return recordType;
	}



	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}



	public String getItemStatus() {
		return itemStatus;
	}



	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}



	public String getOrderDateTime() {
		return orderDateTime.format(inputFormatter);
		
	}



	public void setOrderDateTime(LocalDateTime orderDateTime) {
		this.orderDateTime = orderDateTime;
	}
	
		
	public String toJsonAsString() {
		try {
			return JSON.writeValueAsString(this);
		} catch (IOException e) {
			return null;
		}
	}
	
	

}