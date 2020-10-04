package com.javatechie.spring.mongo.api.resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.javatechie.spring.mongo.api.model.AuthenticationRequest;
import com.javatechie.spring.mongo.api.model.AuthenticationResponse;
import com.javatechie.spring.mongo.api.model.Employee;
import com.javatechie.spring.mongo.api.repository.EmployeeRepository;
import com.javatechie.spring.mongo.api.service.MyUserDetailsService;
import com.javatechie.spring.mongo.util.JwtUtil;

@RestController
public class EmployeeController {

	@Autowired
	private EmployeeRepository repository;
	
	@Autowired
	KafkaTemplate<String, Object> template;

	List<String> departments=new ArrayList<>(Arrays.asList("Data Practice","IBM","Telecom","Life Sciences")) ;
	List<String> designation=new ArrayList<>(Arrays.asList("SDE-1","SDE-2","SDE-3","Tech Lead")) ;
	
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private MyUserDetailsService userDetailsService;

	@RequestMapping({ "/hello" })
	public String firstPage() {
		return "Hello World";
	}

	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword())
			);
		}
		catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}


		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUserName());

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}


	
	
	@PostMapping("/v1/employees")
	public ResponseEntity<String> saveEmployee(@RequestBody Employee employee) {
		//template.send("psl.employee.createOrUpdate",employee);
		if(!departments.contains(employee.getDepartment())){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No such department exists");
		}
		if(!designation.contains(employee.getDesignation())){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No such designation exists");
		}
		
		template.send("psl.employee.createOrUpdate", employee);
		System.out.println("Saving....");
		 return ResponseEntity.status(HttpStatus.CREATED).body("Employee added successfully");
	}

	@GetMapping("/v1/employees")
	public ResponseEntity<List<Employee>> getEmployees() {
		return ResponseEntity.status(HttpStatus.OK).body(repository.findAll());
	}

	@GetMapping("/v1/employees/{id}")
	public ResponseEntity<Optional<Employee>> getEmployee(@PathVariable String id) {
		return ResponseEntity.status(HttpStatus.OK).body(repository.findById(id));
	}
	
	@DeleteMapping("/v1/employees/{id}")
	public ResponseEntity<String> deleteBook(@PathVariable String id) {
		Optional<Employee> emp = repository.findById(id);
		if(!emp.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No employee with this id");
		template.send("psl.employee.delete",id);
		return ResponseEntity.status(HttpStatus.OK).body("employee deleted with id : "+id);
	}
	
	@PutMapping("/v1/employees/{id}")
	public ResponseEntity<Object> updateEmployee(@PathVariable String id,@RequestBody Employee employee) {
		Optional<Employee> emp = repository.findById(id);
		System.out.println(emp+" lll");
		if(!emp.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No employee with this id");
		if(!departments.contains(employee.getDepartment())){
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No such department exists");
		}
		if(!designation.contains(employee.getDesignation())){
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No such designation exists");
		}
		employee.setId(id);
		template.send("psl.employee.update",employee);
		return ResponseEntity.ok(employee);
		
	}

}
