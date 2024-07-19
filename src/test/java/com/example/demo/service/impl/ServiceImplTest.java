package com.example.demo.service.impl;

import com.example.demo.dao.EmpRepository;
import com.example.demo.model.Employee;
import com.example.demo.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceImplTest {

    @Mock
    private EmpRepository repository;

    @InjectMocks
    private ServiceImpl service;

    private Employee emp;
    private User user;

    @BeforeEach
    void setUp() {
        emp = new Employee();
        emp.setId(1);
        emp.setFirstname("John");
        emp.setLastname("Yip");
        emp.setDob(LocalDate.of(1994, 9, 17));
        emp.setEmail("test@example.com");
        emp.setExperience(3);
        emp.setDomain("Backend");
        emp.setPassword("password123");


    }

    @Test
    void testPostEmpIfEmailAlreadyExists() {
        // Since it has email , so it should return true as boolean existsByEmail(String email)
        when(repository.existsByEmail(emp.getEmail())).thenReturn(true);

        String result = service.postEmp(emp);

        // Verify a Method was Called
        verify(repository).existsByEmail(emp.getEmail());

        // Verify a Method Was Not Called
        verify(repository, never()).existsById(anyInt());
        verify(repository, never()).save(any(Employee.class));
        assertEquals("Employee with this mailid: test@example.com already exist", result);
    }

    @Test
    void testPostEmpIfIdAlreadyExists() {
        when(repository.existsById(emp.getId())).thenReturn(true);

        String result = service.postEmp(emp);

        verify(repository).existsById(emp.getId());
        verify(repository).existsByEmail(anyString());
        verify(repository, never()).save(any(Employee.class));
        assertEquals("Employee with this id: 1 already exist", result);
    }

    @Test
    void testPostEmpSuccess() {
        when(repository.existsByEmail(emp.getEmail())).thenReturn(false);
        when(repository.existsById(emp.getId())).thenReturn(false);

        String result = service.postEmp(emp);

        verify(repository).existsByEmail(emp.getEmail());
        verify(repository).existsById(emp.getId());
        verify(repository).save(emp);
        assertEquals("Hi John your Registration process successfully completed", result);
    }

    @Test
    void testPostEmpIfNullValues() {
        Employee nullEmp = new Employee();
        nullEmp.setId(0);
        nullEmp.setEmail(null);
        nullEmp.setFirstname(null);


        verify(repository, never()).existsByEmail(anyString());
        verify(repository, never()).existsById(anyInt());
        verify(repository, never()).save(any(Employee.class));
        assertThrows(NullPointerException.class, () -> {
            service.postEmp(nullEmp);
        });
    }

    @Test
    public void testGetAllEmployeesAsInsertSecondEmployee() {
        Employee employee2 = new Employee();
        employee2.setId(2);
        employee2.setEmail("alex0917lfo@gmail.com");
        employee2.setFirstname("Alex");

        List<Employee> employees = Arrays.asList(emp, employee2);

        when(repository.findAll(any(Sort.class))).thenReturn(employees);
        List<Employee> result = service.getAllEmployees();

        assertEquals(2, result.size());
        assertEquals("test@example.com", result.get(0).getEmail());
        assertEquals("alex0917lfo@gmail.com", result.get(1).getEmail());
        assertEquals("John", result.get(0).getFirstname());
    }

    @Test
    public void testGetByMailIfEmailExists() {
        List<Employee> employeeList = Collections.singletonList(emp);

        when(repository.existsByEmail(emp.getEmail())).thenReturn(true);
        when(repository.getByEmail(emp.getEmail())).thenReturn(employeeList);

        List<Employee> result = service.getByMail(emp.getEmail());

        verify(repository).existsByEmail(emp.getEmail());
        verify(repository).getByEmail(emp.getEmail());
        assertEquals(employeeList, result);
    }

    @Test
    public void testGetByMailIfEmailNotExists() {
        Employee emp2 = new Employee();
        emp2.setEmail(null);

        String email = emp2.getEmail();

        when(repository.existsByEmail(email)).thenReturn(false);

        List<Employee> result = service.getByMail(email);

        verify(repository, never()).getByEmail(email);
        assertNull(result);

    }

    @Test
    public void testDeleteByHavingAId() {
        Employee emp2 = new Employee();
        emp2.setId(2);
        emp2.setEmail("alex0917lfo@gmail.com");
        emp2.setFirstname("Alex");

        List<Employee> employees = new ArrayList<>();
        employees.add(emp);
        employees.add(emp2);

        when(repository.findAll()).thenReturn(employees);

        List<Employee> employee = service.deleteById(emp.getId());

        // Then
        verify(repository).deleteById(emp.getId()); // Verify deleteById is called
        verify(repository).findAll(); // Verify findAll is called
        assertEquals(employees, employee); // Verify the result is as expected
    }

    @Test
    public void testPutEmpIfWeHaveId() {
        Employee emp2 = new Employee();
        emp2.setId(2);
        emp2.setEmail("alex0917lfo@gmail.com");
        emp2.setFirstname("Alex");

        when(repository.existsById(emp2.getId())).thenReturn(true);

        String result = service.putEmp(emp2);

        assertEquals("Hi Alex your profile details successfully updated", result);
        verify(repository).existsById(emp2.getId());
        verify(repository).save(emp2);
    }

    @Test
    public void testPutEmpIfWeDontHaveId() {
        Employee emp2 = new Employee();
        emp2.setEmail("alex0917lfo@gmail.com");
        emp2.setFirstname("Alex");

        when(repository.existsById(emp2.getId())).thenReturn(false);

        String result = service.putEmp(emp2);

        assertEquals("Employee details not Found", result);
        verify(repository).existsById(emp2.getId());
        verify(repository, never()).save(emp2);
    }


    @Test
    public void testValidateUser() {

        when(repository.findByEmail(emp.getEmail())).thenReturn(emp);

        Employee result = service.validateUser(emp.getEmail(), "password123");
        assertEquals(emp , result);
        verify(repository).findByEmail(emp.getEmail());
    }

    @Test
    void testValidateUser_IncorrectPassword() {
        when(repository.findByEmail(emp.getEmail())).thenReturn(emp);

        Employee result = service.validateUser(emp.getEmail(), "wrongpassword");

        assertNull(result);
        verify(repository).findByEmail(emp.getEmail());
    }

    @Test
    void testValidateUser_UserNotFound() {
        when(repository.findByEmail("nonexistent@example.com")).thenReturn(null);

        Employee result = service.validateUser("nonexistent@example.com", "password123");

        assertNotEquals(emp, result);
        assertNull(result);
        verify(repository).findByEmail("nonexistent@example.com");
    }

}

