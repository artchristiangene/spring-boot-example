package com.artchristian;

import com.artchristian.customer.Customer;
import com.artchristian.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Main {
	public static void main(String[] args) {

		SpringApplication.run(Main.class, args);
	}

	@Bean
	CommandLineRunner runner(CustomerRepository customerRepository){
		return args -> {
			Customer alex = new Customer(
					1,
					"Art",
					"art@gmail.com",
					33
			);
			Customer mina = new Customer(
					2,
					"Mina",
					"mina@gmail.com",
					32
			);
			List<Customer> customers = List.of(alex, mina);
			//customerRepository.saveAll(customers);

		};
	}
}
