package com.zorvyn.financebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class FinancebackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancebackendApplication.class, args);
	}

	
}
