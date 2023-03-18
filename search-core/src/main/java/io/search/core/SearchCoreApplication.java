package io.search.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"io.search"})
public class SearchCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchCoreApplication.class, args);
	}

}
