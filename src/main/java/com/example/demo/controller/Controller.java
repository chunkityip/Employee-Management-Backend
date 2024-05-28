package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Employee;
import com.example.demo.model.User;
import com.example.demo.dao.EmpRepository;
import com.example.demo.service.EmpService;

@RestController
@CrossOrigin("*")
public class Controller {
	
	@Autowired
	public EmpRepository repository;
	
	@Autowired
	public EmpService service;

    //An Api endpoint to handle register request
    //@RequestBody allow you to get complex data (such as JSON or XML) from the client in the body of a
    //POST or PUT request.
	@PostMapping("/register")
    public String register(@RequestBody Employee emp) {
        
		return service.postEmp(emp);
    }

    //An Api to get all Employee info
    @GetMapping("/getAllEmployees")
    public List<Employee> findAllEmployees() {
        return service.getAllEmployees();
    }


    //An Api endpoint to request all Employee info
    //@RequestParam allow you to access query parameters when the
    //parameters are optional or when dealing with a large number of parameters
    @GetMapping("/getAllEmployeesInPage")
    public Map<String, Object> getAllEmployeesInPage(@RequestParam(name = "pageNo", defaultValue = "0") int pageNo,
    		@RequestParam(name = "pageSize", defaultValue = "2") int pageSize,
    		@RequestParam(name = "sortBy", defaultValue = "id") String sortBy ) {
        return service.getAllEmpInPage(pageNo, pageSize, sortBy);
    }

    //An Api to login as user
    @PostMapping("/login")
    public Employee empLogin(@RequestBody User user) {
		 return service.validateUser(user.getUsername(), user.getPassword());
		
    }

    //An Api to update employee info
    @PutMapping("/update")
    public String updateEmp(@RequestBody Employee emp) {
    	return service.putEmp(emp);
    }

    //An Api to find Employee base on Email
    //@PathVariable allows you to access dynamic values from the URL and use them in your code
    //It is useful when you want to capture values from the URL and use them in your Spring MVC
    @GetMapping("/findEmpByMail/{email}")
    public List<Employee> findUser(@PathVariable String email) {
  
    	return service.getByMail(email);
    }

    //An Api to get Employee info base on their first name
    //@RequestParam allow you to access query parameters when the
    //parameters are optional or when dealing with a large number of parameters
    @GetMapping("/findByFirstnamePrefix")
    public List<Employee> findEmployee(@RequestParam(name = "firstname") String firstname) {
    	return service.getByFirstname(firstname);
    }

    //An Api to get Employee info base on their experience
    @GetMapping("/getEmpByExperience")
    public List<Employee> getEmployeeByExperience(@RequestParam(name = "experience") int experience) {
    	return service.getEmployeeByExperience(experience);
    }

    //An Api to get all Employee info
    @DeleteMapping("/delete/{id}")
    public List<Employee> cancelRegistration(@PathVariable int id) {
    	return service.deleteById(id);    
    }

}
