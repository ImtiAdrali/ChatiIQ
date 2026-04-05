package com.imti.ChatiIQ;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatiIQ {

	public static void main(String[] args) {
		SpringApplication.run(ChatiIQ.class, args);

        System.out.println("PROPERTY: " + System.getProperty("DB_URL"));
	}

}
