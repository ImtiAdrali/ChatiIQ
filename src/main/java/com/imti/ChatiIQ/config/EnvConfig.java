package com.imti.ChatiIQ.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EnvConfig {

    @PostConstruct
    public void dotenv() {
       Dotenv dotenv = Dotenv.load();

//       System.setProperty("DB_URL", dotenv.get("DB_URL"));
//       System.setProperty("DB_USERNAME", dotenv.get("DB_USERNAME"));
//       System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));
//
//       System.setProperty("OLLAMA_URL", dotenv.get("OLLAMA_URL"));


    }

}
