package com.javatechie.spring.mongo.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.javatechie.spring.mongo.api.model.Employee;
import com.javatechie.spring.mongo.api.model.EmployeeDetails;
import com.javatechie.spring.mongo.api.repository.EmployeeRepository;

@Service
public class MyUserDetailsService implements UserDetailsService{
	@Autowired
	EmployeeRepository employeeRepository;
	@Override
	public EmployeeDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("load");
		// TODO Auto-generated method stub
		Optional<Employee> emp=employeeRepository.findByName(username);
		
		emp.orElseThrow(()->new UsernameNotFoundException("User not found!"));
		//System.out.println(emp);
		EmployeeDetails empD=emp.map(EmployeeDetails::new).get();
		System.out.println(empD);
		return empD;
	}

}
