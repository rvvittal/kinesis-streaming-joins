package com.amazonaws.samples.kinesis.streams.join.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.cli.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.samples.kinesis.streams.join.model.Order;
import com.amazonaws.samples.kinesis.streams.join.model.OrderItem;
import com.amazonaws.samples.kinesis.streams.join.model.Product;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;

/**
 * Generates random orders and creates a json file to be uploaded to S3.
 *
 */

public class OrderGenerator {

	private static long orderStartId = 10000001;
	private static int itemStartId = 999900001;
	private static AtomicLong orderId;
	private static double orderStartAmount = 1000.00;
	private static double orderAmount = orderStartAmount;
	private static int orderCount = 10000;

	private static String PRODUCTS = "products";
	private static int productCount = 1000;
	private static int productStartId = itemStartId;
	
	
	private final static ObjectMapper JSON = new ObjectMapper();
	static {
		JSON.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	

	private static final Log LOG = LogFactory.getLog(OrderGenerator.class);

	public static void main(String[] args)  {
		
		String launchType=""; 
		
	
		Options options = new Options()
		        .addOption("launchType", true, "use keyword orders or products to generate that type of json records")
		        .addOption("orderStartId", true, "Starting Id for orders")
		        
		        .addOption("orderCount", true, "Count of orders to generate")
		        .addOption("itemStartId", true, "Starting Id for items")
		        .addOption("itemCount", true, "Count of items to generate")
		        .addOption("productStartId", true, "Starting Id for products.. should correlate with itemStartId")
		        .addOption("productCount", true, "Count of products to generate");
		
		CommandLine line=null;
		try {
			line = new DefaultParser().parse(options, args);
		} catch (ParseException e1) {
			LOG.error(e1);
			e1.printStackTrace();
		}
	    
		  
	    if (line.hasOption("help")) {
	      new HelpFormatter().printHelp(MethodHandles.lookup().lookupClass().getName(), options);
	    } 
	    
	   
		launchType = line.getOptionValue("launchType", "orders");
		orderStartId = Long.parseLong(line.getOptionValue("orderStartId", "10000001"));
		orderCount = Integer.parseInt(line.getOptionValue("orderCount", "10000"));
		itemStartId = Integer.parseInt(line.getOptionValue("itemStartId", "999900001"));
		
		productCount = Integer.parseInt(line.getOptionValue("productCount", "1000"));
		productStartId = Integer.parseInt(line.getOptionValue("productStartId", "999900001"));
		
		if(PRODUCTS.equalsIgnoreCase(launchType)) {
			ProductGenerator.genProducts(productStartId, productCount);
			System.exit(0);
		}
			
		
		

		orderId = new AtomicLong(orderStartId);
		
		Faker faker = new Faker();

		LOG.info("Generating " + orderCount + " Orders..");
		long genOrderId=0;
		BufferedWriter ordersBuffWriter=null;
		FileWriter ordersWriter;
		
		try {
		
			ordersWriter = new FileWriter("orders.json"); 
			ordersBuffWriter = new BufferedWriter(ordersWriter);
			
			
			Map<String, Product> productMap = loadProducts("products.json");
			
	
			for (int i = 0; i < orderCount; i++) {
				
				genOrderId = orderId.getAndIncrement();
				
				Product product1 = (Product)productMap.get(getRandomProductId());
				Product product2 = (Product)productMap.get(getRandomProductId());
				
				orderAmount = product1.getProductPrice() + product2.getProductPrice();
						
				generateOrder(faker, genOrderId, orderAmount, ordersBuffWriter);
				
				generateOrderItem(genOrderId,product1, ordersBuffWriter);
				generateOrderItem(genOrderId,product2, ordersBuffWriter);
				
			}
	
			LOG.info("Generated " + orderCount + " Orders");
			
		

		}
		catch(Exception e) {
			LOG.error(e);
		}
		finally {
			try {
				ordersBuffWriter.flush();
				ordersBuffWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	}

	private static void generateOrder(Faker faker, long orderId, double baseAmount, BufferedWriter ordersBuffWriter) {

		String streetName = faker.address().streetName();
		String number = faker.address().buildingNumber();
		String city = faker.address().city();
		String state = faker.address().state();
		String zip = faker.address().zipCode();
		String name = faker.name().fullName();

		Order order = new Order();
		order.setOrderId(orderId);
		order.setOrderDateTime(LocalDateTime.now());
		order.setOrderStatus("New");
		order.setOrderAmount(orderAmount);
		order.setShipToAddress(number + " " + streetName);
		order.setShipToCity(city);
		order.setShipToState(state);
		order.setShipToZip(zip);
		order.setShipToName(name);

		

		try {
			ordersBuffWriter.write(order.toJsonAsString());
			ordersBuffWriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		
		

	}
	
	private static String getRandomProductId() {
		
		Random r = new Random();
		int low = (int)itemStartId;
		int high = (int)itemStartId+productCount;
		int result = r.nextInt(high-low) + low;
		
		return String.valueOf(result);

	}
	
	private static void generateOrderItem(long orderId, Product product, BufferedWriter ordersBuffWriter) {

		OrderItem item = new OrderItem();
		
		
		item.setOrderId(orderId);
		item.setItemId(product.getProductId());
		item.setItemQuantity(1);
		item.setItemAmount(product.getProductPrice());
		item.setOrderDateTime(LocalDateTime.now());
		item.setItemStatus("Ready");
		

		try {
			ordersBuffWriter.write(item.toJsonAsString());
			ordersBuffWriter.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private static Map<String,Product> loadProducts(String jsonFile) {
		
	    String line = "";
	    String cvsSplitBy = ",";
	    Map<String, Product> productList = new HashMap<>();
	    Product product;
	
	    try (BufferedReader br = new BufferedReader(new FileReader(jsonFile))) {
	
	        while ((line = br.readLine()) != null) {
	        	
	            product =  JSON.readValue(line, Product.class);
				productList.put(String.valueOf(product.getProductId()),product);
	            
	        }
	
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return productList;
	}


}