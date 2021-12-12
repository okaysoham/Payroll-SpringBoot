package com.PayrollJavaSpringBoot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class LoadDatabase {

  private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

  @Bean
  CommandLineRunner initDatabase(EmployeeRepostiory repostiory) {
    return args -> {
      log.info("Preloading " + repostiory.save(new Employee("Soham Mukherjee", "Backend Developer")));
      log.info("Preloading " + repostiory.save(new Employee("Sougato Bagchi", "International Student")));
    };
  }

}
