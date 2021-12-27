package com.PayrollJavaSpringBoot;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmployeeController {

  private final EmployeeRepostiory repository;
  private final EmployeeModelAssembler assembler;

  EmployeeController(EmployeeRepostiory repository, EmployeeModelAssembler assembler) {
    this.assembler = assembler;
    this.repository = repository;
  }

  // Get all the employee details
  @GetMapping("/employees")
  CollectionModel<EntityModel<Employee>> all() {
    List<EntityModel<Employee>> employees = repository.findAll().stream()
            .map(assembler::toModel).collect(Collectors.toList());

    return CollectionModel.of(employees,
            linkTo(methodOn(EmployeeController.class).all()).withSelfRel());
  }

  // Set the employee details in db
  @PostMapping("/employees")
  ResponseEntity<?> newEmployee(@RequestBody Employee newEmployee) {

    EntityModel<Employee> entityModel = assembler.toModel(repository.save(newEmployee));
    return ResponseEntity
            .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
            .body(entityModel);

  }

  // Pull single employee details
  @GetMapping("/employees/{id}")
  EntityModel<Employee> one(@PathVariable Long id) {
    Employee employee = repository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException(id));
    return assembler.toModel(employee);
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
