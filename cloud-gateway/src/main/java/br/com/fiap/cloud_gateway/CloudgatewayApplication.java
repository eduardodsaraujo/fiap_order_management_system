package br.com.fiap.cloud_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class CloudgatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudgatewayApplication.class, args);
	}

	@Bean
	public RouteLocator routes(RouteLocatorBuilder builder){
		return builder
				.routes()
				.route( r -> r.path("/customer-management/**").filters(f -> f.stripPrefix(1)).uri("lb://customer-management"))
				.route( r -> r.path("/delivery-logistics/**").filters(f -> f.stripPrefix(1)).uri("lb://delivery-logistics"))
				.route( r -> r.path("/product-management/**").filters(f -> f.stripPrefix(1)).uri("lb://product-management"))
				.route( r -> r.path("/product-import/**").filters(f -> f.stripPrefix(1)).uri("lb://product-import"))
				.route( r -> r.path("/order-management/**").filters(f -> f.stripPrefix(1)).uri("lb://order-management"))
				.build();
	}

}
