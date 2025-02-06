package myconfig;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PathConfig implements WebMvcConfigurer {
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		System.out.println("패스컨피그실행");
		if (System.getProperty("os.name").contains("Win")) {
			registry.addResourceHandler("/upload/**")
			.addResourceLocations("file:///c:/ezwel/upload/");		
		} else {
			registry.addResourceHandler("/upload/**")
			.addResourceLocations("file:////Users/minsu/Documents/ezwel/upload/");
		}
	}

}

