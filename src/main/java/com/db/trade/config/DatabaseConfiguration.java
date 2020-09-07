package com.db.trade.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

/*
 * Used Mongo DB to persist and retrieval of data 
 */

@Configuration
@ComponentScan(basePackages = "com.db.trade")
@EnableMongoRepositories("com.db.trade.repository")
public class DatabaseConfiguration {

	private static final String DB_NAME = "TRADE";
	
	public @Bean MongoClient mongoClient() {
		return MongoClients.create("mongodb://localhost:27017");
	}
	@Bean
	public MongoTemplate mongoTemplate() throws IOException {

		MongoTemplate mongoTemplate = new MongoTemplate(mongoClient(), DB_NAME);
		return mongoTemplate;
	}

}
