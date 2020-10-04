package com.javatechie.spring.mongo.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.javatechie.spring.mongo.api.model.Employee;
import com.javatechie.spring.mongo.api.repository.EmployeeRepository;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeRepository repository;

//	@PostMapping("/v1/employees")
	@KafkaListener(topics = {"psl.employee.createOrUpdate"}, groupId = "employees")
	public String saveEmployee(Employee employee) {
		repository.save(employee);
		System.out.println("Employee saved!!");
		return "Added employee with id : " + employee.getId();
	}

	@GetMapping("/v1/employees")
	public List<Employee> getEmployees() {
		return repository.findAll();
	}

	@GetMapping("/v1/employees/{id}")
	public Optional<Employee> getEmployee(@PathVariable String id) {
		return repository.findById(id);
	}

	@KafkaListener(topics = {"psl.employee.delete"}, groupId = "employees")
	public String deleteEmployee( String id) {
		repository.deleteById(id);
		return "employee deleted with id : " + id;
	}
	
	@KafkaListener(topics = {"psl.employee.update"}, groupId = "employees")
	public ResponseEntity<Employee> updateEmployee( Employee employee) {
		
		Employee updatedEmployee=repository.save(employee);
		return ResponseEntity.ok(updatedEmployee);
		
	}

}
