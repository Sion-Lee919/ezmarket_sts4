package com.ezmarket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PathConfig implements WebMvcConfigurer {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		System.out.println("패스컨피그실행"); 
		
		if (System.getProperty("os.name").contains("Win")) {
			registry.addResourceHandler("/ezmarketimage/**")
			.addResourceLocations("file:///c:/ezwel/Desktop/downloaded_images/");		
		} else {
			registry.addResourceHandler("/ezmarketimage/**")
			.addResourceLocations("file:////Users/minsu/Documents/ezwel/Desktop/downloaded_images/");
		}
	}

}

