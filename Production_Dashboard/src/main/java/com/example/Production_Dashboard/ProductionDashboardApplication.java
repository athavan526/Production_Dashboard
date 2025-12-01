package com.example.Production_Dashboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication(scanBasePackages =  {"com.example.app", "Engine", "DashBoard"})
@EntityScan(basePackages = {"Dto"})

public class ProductionDashboardApplication extends SpringBootServletInitializer {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ProductionDashboardApplication.class);
    }
	public static void main(String[] args) {
		SpringApplication.run(ProductionDashboardApplication.class, args);
	}

}