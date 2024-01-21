package com.example.StocksPortfolioApplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableCaching
public class StocksPortfolioApplication {

	public static void main(String[] args) {
		SpringApplication.run(StocksPortfolioApplication.class, args);
	}

}
