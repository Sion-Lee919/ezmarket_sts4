package com.ezmarket;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = {"ezmarket", "google","com.ezmarket.config"})
@MapperScan(basePackages = {"ezmarket", "google"})

public class EzmarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(EzmarketApplication.class, args);
	}

}
