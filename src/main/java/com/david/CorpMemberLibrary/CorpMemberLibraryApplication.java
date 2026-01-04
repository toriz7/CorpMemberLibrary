package com.david.CorpMemberLibrary;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CorpMemberLibraryApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorpMemberLibraryApplication.class, args);
	}

}
