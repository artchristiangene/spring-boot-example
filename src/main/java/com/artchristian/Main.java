package com.artchristian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

import javax.management.ValueExp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SpringBootApplication
@RestController
public class Main {

	private static List<Customer> customers;

	static {
		customers = new ArrayList<>();
		Customer alex = new Customer(
				1,
				"Art",
				"art@gmail.com",
				33
		);
		customers.add(alex);
		Customer mina = new Customer(
				2,
				"Mina",
				"mina@gmail.com",
				32
		);
		customers.add(mina);
	}

	public static void main(String[] args) {
		System.out.println(customers);
		SpringApplication.run(Main.class, args);
	}

	static class Customer {
		private Integer id;
		private String name;
		private String email;
		private Integer age;

		public Customer(Integer id, String name, String email, Integer age) {
			this.id = id;
			this.name = name;
			this.email = email;
			this.age = age;
		}

		public Customer() {
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public Integer getAge() {
			return age;
		}

		public void setAge(Integer age) {
			this.age = age;
		}

		@Override
		public String toString() {
			return "Customer{" +
					"id=" + id +
					", name='" + name + '\'' +
					", email='" + email + '\'' +
					", age=" + age +
					'}';
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || getClass() != o.getClass()) return false;
			Customer customer = (Customer) o;
			return Objects.equals(id, customer.id) && Objects.equals(name, customer.name) && Objects.equals(email, customer.email) && Objects.equals(age, customer.age);
		}

		@Override
		public int hashCode() {
			return Objects.hash(id, name, email, age);
		}
	}



}
