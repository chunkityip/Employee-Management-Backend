package com.example.demo.dao;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Employee;

@Repository
public interface EmpRepository extends MongoRepository<Employee, Integer> {

	//MongoRepository provides all the necessary methods which help to create
	// a CRUD application , and it also supports the custom derived query methods
	boolean existsByEmail(String email);

	//A method to look for Employee by their first name
	List<Employee> findByFirstnameStartingWith(String firstname);

	//A method to look for Employee work experience
	/*
	@Query annotation is used to define custom queries to retrieve data from a MongoDB database
	{$gte:?0}: This is a MongoDB query operator used to specify a condition.
	{$gte} stands for "greater than or equal to."
	 */
	@Query(value = "{'experience':{$gte:?0}}")
	List<Employee> findByExperience(int experience);

	//A method to look for Employee e-mail
	Employee findByEmail(String username);

	//A method to return Employee email
	List<Employee> getByEmail(String email);
}
