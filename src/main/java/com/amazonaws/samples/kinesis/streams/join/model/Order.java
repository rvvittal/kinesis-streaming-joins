package com.amazonaws.samples.kinesis.streams.join.model;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Order  {

	private final static ObjectMapper JSON = new ObjectMapper();
	static {
		JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	private static String ORDER_REC_TYPE = "Order";
	
	private static DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
	

	
	private long orderId;
	private double orderAmount;
	private String orderStatus;
	private LocalDateTime orderDateTime;
	private String shipToName;
	private String shipToAddress;
	private String shipToCity;
	private String shipToState;
	private String shipToZip;
	private String recordType;
	
	
	public Order() {
		this.recordType = ORDER_REC_TYPE;
		
	}
	
		
	
	public long getOrderId() {
		return orderId;
	}


	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}


	public double getOrderAmount() {
		return orderAmount;
	}


	public void setOrderAmount(double orderAmount) {
		this.orderAmount = orderAmount;
	}


	public String getOrderStatus() {
		return orderStatus;
	}


	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}


	public String getShipToName() {
		return shipToName;
	}


	public void setShipToName(String shipToName) {
		this.shipToName = shipToName;
	}


	public String getShipToAddress() {
		return shipToAddress;
	}


	public void setShipToAddress(String shipToAddress) {
		this.shipToAddress = shipToAddress;
	}


	public String getShipToCity() {
		return shipToCity;
	}


	public void setShipToCity(String shipToCity) {
		this.shipToCity = shipToCity;
	}


	public String getShipToZip() {
		return shipToZip;
	}


	public void setShipToZip(String shipToZip) {
		this.shipToZip = shipToZip;
	}


	
		

	public String toJsonAsString() {
		try {
			return JSON.writeValueAsString(this);
		} catch (IOException e) {
			return null;
		}
	}



	public String getOrderDateTime() {
		
		return orderDateTime.format(inputFormatter);
	}



	public void setOrderDateTime(LocalDateTime orderDateTime) {
		this.orderDateTime = orderDateTime;
	}



	public String getShipToState() {
		return shipToState;
	}



	public void setShipToState(String shipToState) {
		this.shipToState = shipToState;
	}



	public String getRecordType() {
		return recordType;
	}



	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	

}