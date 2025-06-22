package com.javatechie.crud.example;

import com.javatechie.crud.example.entity.Product;
import com.javatechie.crud.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class SpringBootCrudExample2Application {

	@Autowired
	private ProductRepository repository;

	@Bean
	public CommandLineRunner init() {
		return args -> {
			List<Product> products = Stream.of(
				new Product(1, "Laptop", 1, 60000),
				new Product(2, "Phone", 1, 80000),
				new Product(3, "Book", 5, 1000),
				new Product(4, "Headset", 2, 2000)
			).collect(Collectors.toList());
			repository.saveAll(products);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCrudExample2Application.class, args);
	}

}
