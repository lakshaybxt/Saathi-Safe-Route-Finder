package com.saathi.saathi_be;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@Slf4j
public class SaathiBeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SaathiBeApplication.class, args);
	}

}