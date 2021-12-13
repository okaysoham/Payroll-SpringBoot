package com.PayrollJavaSpringBoot;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

  private final EmployeeRepostiory repository;

  EmployeeController(EmployeeRepostiory repository) {
    this.repository = repository;
  }

  // Get all the employee details
  @GetMapping("/employees")
  List<Employee> all() {
    return repository.findAll();
  }

  // Set the employee details in db
  @PostMapping("/employees")
  Employee newEmployee(@RequestBody Employee newEmployee) {
    return repository.save(newEmployee);
  }

  // Pull single employee details
  @GetMapping("/employees/{id}")
  Employee one(@PathVariable Long id) {
    return repository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException(id));
  }

  // Replace Employee details
  @PutMapping("/employees/{id}")
  Employee replaceEmployee(@RequestBody Employee newEmployee, @PathVariable Long id) {
    return repository.findById(id)
            .map(employee -> {
              employee.setName(newEmployee.getName());
              employee.setRole(newEmployee.getRole());
              return repository.save(employee);
            })
            .orElseGet(() -> {
              newEmployee.setId(id);
              return repository.save(newEmployee);
            });
  }

  // Delete an existing employee
  @DeleteMapping("/employees/{id}")
  void deleteEmployee(@PathVariable Long id) {
    repository.deleteById(id);
  }

}