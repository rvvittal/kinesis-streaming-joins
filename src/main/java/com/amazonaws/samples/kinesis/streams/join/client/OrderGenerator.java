package com.amazonaws.samples.kinesis.streams.join.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
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
	private static AtomicLong orderId = new AtomicLong(orderStartId);
	private static AtomicLong itemId = new AtomicLong(orderStartId+1);
	private static double orderStartAmount = 1000.00;
	private static double orderAmount = orderStartAmount;
	private static int orderCount = 10000;

	private static final Log LOG = LogFactory.getLog(OrderGenerator.class);

	public static void main(String[] args) throws Exception {

		if (args != null && args.length == 3) {
			orderStartId = Long.parseLong(args[0]);
			orderStartAmount = Double.parseDouble(args[1]);
			orderAmount = orderStartAmount;
			orderCount = Integer.parseInt(args[2]);
		} else {
			LOG.error("OrderStartId, OrderBaseAmount, OrderCount arguments required");
			System.exit(0);
		}

		Faker faker = new Faker();

		LOG.info("Generating " + orderCount + " Orders..");
		long genOrderId=0;
		double itemAmount=0;
		

		for (int i = 0; i < orderCount; i++) {
			
			genOrderId = orderId.getAndIncrement();
			generateOrder(faker, genOrderId, orderAmount);
			itemAmount = orderAmount/2;
			generateOrderItem(genOrderId,itemAmount);
			generateOrderItem(genOrderId,itemAmount);
			orderAmount = orderAmount + 10;
		}

		LOG.info("Generated " + orderCount + " Orders");

		

	}

	private static void generateOrder(Faker faker, long orderId, double baseAmount) {

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

		try (FileWriter writer = new FileWriter("orders.json", true); BufferedWriter bw = new BufferedWriter(writer)) {

			bw.write(order.toJsonAsString());
			bw.newLine();

		} catch (IOException e) {
			LOG.error("IOException: %s%n", e);
		}
		
		

	}
	
	private static void generateOrderItem(long orderId, double itemAmount) {

		OrderItem item = new OrderItem();
		
		item.setOrderId(orderId);
		item.setItemId(itemId.getAndIncrement());
		item.setItemQuantity(1);
		item.setItemAmount(itemAmount);
		item.setOrderDateTime(LocalDateTime.now());
		item.setItemStatus("Ready");
		

		try (FileWriter writer = new FileWriter("orders.json", true); BufferedWriter bw = new BufferedWriter(writer)) {

			bw.write(item.toJsonAsString());
			bw.newLine();

		} catch (IOException e) {
			LOG.error("IOException: %s%n", e);
		}

	}

}