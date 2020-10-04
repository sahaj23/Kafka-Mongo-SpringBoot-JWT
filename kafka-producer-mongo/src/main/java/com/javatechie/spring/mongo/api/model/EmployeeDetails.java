package com.javatechie.spring.mongo.api.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class EmployeeDetails implements UserDetails{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
    private String password;
    private List<GrantedAuthority> authorities;

    public EmployeeDetails(Employee emp){
        this.userName = emp.getName();
        this.password = emp.getPassword();
        this.authorities = Arrays.stream(emp.getRoles().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        //System.out.println(emp);
    }
    public EmployeeDetails(){}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String toString() {
		return "EmployeeDetails [userName=" + userName + ", password=" + password + ", authorities=" + authorities
				+ "]";
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
