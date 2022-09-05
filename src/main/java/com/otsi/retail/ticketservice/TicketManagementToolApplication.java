package com.otsi.retail.ticketservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class TicketManagementToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketManagementToolApplication.class, args);
	}

	@Bean
	public Docket swaggerPersonApi10() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("com.otsi.retail.ticketservice")).paths(PathSelectors.any())
				.build().apiInfo(new ApiInfoBuilder().version("1.0").title("ticket-management")
						.description("Documentation Ticket Management v1.0").build());
	}

	@Bean
	public RestTemplate getRestResponse() {
		return new RestTemplate();
	}

}
