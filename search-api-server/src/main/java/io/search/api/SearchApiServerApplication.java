package io.search.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"io.search"})
public class SearchApiServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchApiServerApplication.class, args);
	}

}
