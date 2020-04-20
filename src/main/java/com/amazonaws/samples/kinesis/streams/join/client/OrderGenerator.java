package com.amazonaws.samples.kinesis.streams.join.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.samples.kinesis.streams.join.model.Order;
import com.amazonaws.samples.kinesis.streams.join.model.OrderItem;
import com.github.javafaker.Faker;

/**
 * Generates random orders and creates a json file to be uploaded to S3.
 *
 */

public class OrderGenerator {

	private static long orderStartId = 10000001;
	private static int itemStartId = 999900001;
	private static AtomicLong orderId;
	private static AtomicLong itemId;
	private static double orderStartAmount = 1000.00;
	private static double orderAmount = orderStartAmount;
	private static int orderCount = 10000;
	private static int itemCount = 1000;
	
	

	private static final Log LOG = LogFactory.getLog(OrderGenerator.class);

	public static void main(String[] args)  {

		if (args != null && args.length >= 3) {
			orderStartId = Long.parseLong(args[0]);
			orderStartAmount = Double.parseDouble(args[1]);
			orderAmount = orderStartAmount;
			orderCount = Integer.parseInt(args[2]);
			if(args.length >= 4) itemStartId = Integer.parseInt(args[3]);
			if(args.length >= 5) itemCount = Integer.parseInt(args[4]);
		} else {
			LOG.error("OrderStartId, OrderBaseAmount, OrderCount arguments required");
			System.exit(0);
		}

		orderId = new AtomicLong(orderStartId);
		itemId = new AtomicLong(itemStartId);
		
		Faker faker = new Faker();

		LOG.info("Generating " + orderCount + " Orders..");
		long genOrderId=0;
		BufferedWriter ordersBuffWriter=null;
		FileWriter ordersWriter;
		
		try {
		
			ordersWriter = new FileWriter("orders.json"); 
			ordersBuffWriter = new BufferedWriter(ordersWriter);
			
			
			Map<String, Product> productMap = loadProducts("products.csv");
			
	
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
		int high = (int)itemStartId+itemCount;
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
	
	private static Map<String,Product> loadProducts(String csvFile) {
		
	    String line = "";
	    String cvsSplitBy = ",";
	    Map<String, Product> productList = new HashMap<>();
	    Product product;
	
	    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
	
	        while ((line = br.readLine()) != null) {
	
	            // use comma as separator
	            String[] item = line.split(cvsSplitBy);
	            
	            product = new Product(Long.valueOf(item[0]), item[1], item[2]);
				productList.put(item[0],product);
	            
	
	        }
	
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return productList;
	}


}