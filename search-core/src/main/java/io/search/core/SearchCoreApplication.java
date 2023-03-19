package io.search.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(scanBasePackages = {"io.search"})
public class SearchCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchCoreApplication.class, args);
	}

}
