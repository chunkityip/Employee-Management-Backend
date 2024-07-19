package com.example.demo.service.impl;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.demo.dao.EmpRepository;
import com.example.demo.model.Employee;
import com.example.demo.service.EmpService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Service;
import org.apache.commons.io.IOUtils;

import org.springframework.web.multipart.MultipartFile;

@Service
public class ServiceImpl implements EmpService {

	@Autowired
	public EmpRepository repository;

	@Autowired
	private GridFsTemplate template;

	@Autowired
	private GridFsOperations operations;

	//A method to register Employee
	//It will first check is the email or id already exist. If yes , return message
	//If no , register as new employee
	@Override
	public String postEmp(Employee emp) {
		if (emp == null || emp.getEmail() == null || emp.getFirstname() == null || emp.getId() == 0) {
			throw new NullPointerException("Employee fields cannot be null");
		}

		if (repository.existsByEmail(emp.getEmail())) {
			return "Employee with this mailid: " + emp.getEmail() + " already exist";
		}
		else if(repository.existsById(emp.getId())) {
			return "Employee with this id: " + emp.getId() + " already exist";
		}
		else {
			repository.save(emp);
			return "Hi " + emp.getFirstname() + " your Registration process successfully completed";
		}
	}


	//A method to get all the employee info
	@Override
	public List<Employee> getAllEmployees() {
		//Creates a Sort object that specifies sorting in ascending order based on the "email" property of the entities.

        /*
        Sort.by: This is a static factory method used to create a Sort object.
        It allows you to specify the sorting criteria for your query results

        Sort.Direction.ASC: This specifies the direction of the sorting, in this case, ascending order.
        ASC stands for ascending, meaning that the results will be sorted from smallest to
        largest or from A to Z (for alphabetical sorting)
         */

		return repository.findAll(Sort.by(Sort.Direction.ASC, "email"));
	}

	//A method to get Employee Email
	//If we found the email , return it
	//else , return null
	@Override
	public List<Employee> getByMail(String email) {
		if (repository.existsByEmail(email))
			return repository.getByEmail(email);
		else
			return null;
	}

	//A method to cancel registration by their id
	@Override
	public List<Employee> deleteById(int id) {
		repository.deleteById(id);
		//Since I want to display all employee info after deleteById()
		return repository.findAll();
	}

	//A method to update Employee info
	//If existById() is true , return a massage as updated success
	//else , return message as not fund
	@Override
	public String putEmp(Employee emp) {

		if (repository.existsById(emp.getId())) {
			//Query q = Query.query(Criteria.where("email").is(emp.getEmail()));
			repository.save(emp);
			return "Hi " + emp.getFirstname() + " your profile details successfully updated";
		} else
			return "Employee details not Found";
	}

	//A method to validate user by their username which is email and password to login
	@Override
	public Employee validateUser(String username, String password) {

		Employee emp = repository.findByEmail(username);
		if (emp!=null && emp.getPassword().equals(password)) {
			return emp;
		} else {
			return null;
		}
	}

	//A method to get Employee info base on the first name
	@Override
	public List<Employee> getByFirstname(String firstname) {
		return repository.findByFirstnameStartingWith(firstname);
	}

	//A method to get Employee experience
	@Override
	public List<Employee> getEmployeeByExperience(int experience) {
		return repository.findByExperience(experience);
	}

	//A method to get all Employee info base on page
	@Override
	public Map<String, Object> getAllEmpInPage(int pageNo, int pageSize, String sortBy) {
		//Since the page is following ascending order , we need order structure so LinkedHashMap
		//Think of LinkedHashMap as the ordered version of HashMap while HashMap didn't guarantee order

		Map<String, Object> response = new LinkedHashMap<String, Object>();
		Sort sort = Sort.by(sortBy);
		PageRequest page = PageRequest.of(pageNo, pageSize, sort);
		Page<Employee> employeesPage = repository.findAll(page);
		response.put("data", employeesPage.getContent());
		response.put("Total Number of pages", employeesPage.getTotalPages());
		response.put("current page number", employeesPage.getNumber());
		response.put("Total Number of employees", employeesPage.getTotalElements());
		return response;
	}

}
