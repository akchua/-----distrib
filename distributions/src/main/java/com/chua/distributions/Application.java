package com.chua.distributions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author  Adrian Jasper K. Chua
 * @version 1.0
 * @since   Nov 13, 2016
 */
@SpringBootApplication
@EnableScheduling
@ImportResource({"classpath:META-INF/spring/hibernate.xml", "classpath:META-INF/spring/beans.xml"})
public class Application {

	public static void main(String ... args) throws Exception {
		ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
		
		context.registerShutdownHook();
	}
}
