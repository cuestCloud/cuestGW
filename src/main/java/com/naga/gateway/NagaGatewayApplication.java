package com.naga.gateway;

import java.util.Calendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;



@SpringBootApplication
//@EnableAutoConfiguration(exclude = { DataSourceAutoConfiguration.class,
//		DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@ComponentScan(basePackages = { NagaGatewayApplication.NAGA_PACKAGE })
@EnableScheduling
@EnableSwagger2
public class NagaGatewayApplication {

	public final static String NAGA_PACKAGE = "com.naga.gateway";

	public static void main(String[] args) {
		checkLicense();
		SpringApplication.run(NagaGatewayApplication.class, args);
	}

	private static void checkLicense() {
		int year = Calendar.getInstance().get(Calendar.YEAR);
		if (year>2021) {
			System.out.println("License Expired");
			System.exit(1);
		}
	}

	@Bean
    public Docket api() { 
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();                                         
    }
	
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}
}
