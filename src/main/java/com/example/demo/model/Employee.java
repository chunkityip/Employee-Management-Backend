package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Document
public class Employee {
	
    @Id
    private int id;
    private String firstname;
    private String lastname;
    private String password;
    private LocalDate dob;
    private long mobileno;
    private String email;
    private int experience;
    private String domain;
	
}

