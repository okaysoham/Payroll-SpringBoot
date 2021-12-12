package com.PayrollJavaSpringBoot;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepostiory extends JpaRepository<Employee, Long> {

}
