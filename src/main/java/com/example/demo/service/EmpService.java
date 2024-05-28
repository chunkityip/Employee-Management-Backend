package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.model.Employee;

public interface EmpService {
	
public String postEmp(Employee emp);

	//A method to get all Employee ifo
	//Since we are just doing linear searching , List is easier
	public List<Employee> getAllEmployees();

	//A method to get Employee email
	//Since we are just doing linear searching , List is easier
	public List<Employee> getByMail(String email);

	//A method to delete employee by their id
	//Since we are just doing linear searching , List is easier
	public List<Employee> deleteById(int id);

	//A method to register Employee
	public String putEmp(Employee emp);

	//A method to allow user to login by their username and password
	public Employee validateUser(String username, String password);

	//A method to get all Employee info by page
	//Since we like to display Employee base by page , page as key will be better
	public Map<String, Object> getAllEmpInPage(int pageNo, int pageSize, String sortBy);

	//A method to check Employee info base on their first name
	//Since we are just doing linear searching , List is easier
	public List<Employee> getByFirstname(String firstname);

	//A method to check Employee experience
	//Since we are just doing linear searching , List is easier
	public List<Employee> getEmployeeByExperience(int experience);

}
