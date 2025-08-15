package com.chatApplication.wireline;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
public class WirelineApplication {

	public static void main(String[] args) {
		
		 Dotenv dotenv = Dotenv.configure()
                              .ignoreIfMissing()
                              .load();

        // Inject into system properties so Spring can read
        dotenv.entries().forEach(entry ->
            System.setProperty(entry.getKey(), entry.getValue())
        );

		//Dotenv dotenv2 = Dotenv.configure().load();
        // System.out.println("✅ Mongo URI from .env: " + dotenv2.get("MONGODB_URI"));
        // System.out.println("Redis :" + dotenv2.get("REDIS_HOST"));
        // System.out.println("Redis :" + dotenv2.get("REDIS_PASSWORD"));
        // System.out.println("Mongo_DB:"+ System.getenv("MONGODB_URI"));
        // System.out.println("Redis_host: " + System.getenv("REDIS_HOST"));
		SpringApplication.run(WirelineApplication.class, args);

        


	}

}
