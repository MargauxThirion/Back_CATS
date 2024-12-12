package com.back_cats;

import com.mongodb.*;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

@Component
public class MongoDBConnexion {
        @Autowired
        private MongoTemplate mongoTemplate;
        public void testConnection() {
            try {
                mongoTemplate.getDb().runCommand(new org.bson.Document("ping", 1));
                System.out.println("Successfully connected to MongoDB!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

