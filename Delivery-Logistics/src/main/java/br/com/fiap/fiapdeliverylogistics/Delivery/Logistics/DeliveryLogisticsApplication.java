package br.com.fiap.fiapdeliverylogistics.Delivery.Logistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class DeliveryLogisticsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeliveryLogisticsApplication.class, args);
	}

}
