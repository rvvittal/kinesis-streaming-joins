package com.amazonaws.samples.kinesis.streams.join.client;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.amazonaws.samples.kinesis.streams.join.model.Product;
import com.github.javafaker.Faker;

public class ProductGenerator {
	
	private static final Log LOG = LogFactory.getLog(ProductGenerator.class);
	


	public static void genProducts(int productStartId, int productCount ) {
		
		LOG.info("Generating " + productCount +" Products..");
		
		AtomicLong itemId = new AtomicLong(productStartId);
		
		Faker faker = new Faker();
		Product product;
		
		List <Product> productList  = new ArrayList<Product>();
		
		for(int i=0; i < productCount; i++ ) {
			
			product = new Product(itemId.getAndIncrement(), faker.commerce().productName(), faker.commerce().price());
			productList.add(product);
					
		}
		
		BufferedWriter bw=null;
		
		try  {
			
			
			FileWriter writer = new FileWriter("products.json"); 
			bw = new BufferedWriter(writer);

			for(Product p : productList) {
				bw.write(p.toJsonAsString());
				bw.newLine();
			}
			

		} catch (IOException e) {
			LOG.error("IOException: %s%n", e);
		}
		finally {
			try {
				bw.flush();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			LOG.info("Generated " + productCount +" Products..");
			
		}
		
		

	}
	
	

	
	
}


