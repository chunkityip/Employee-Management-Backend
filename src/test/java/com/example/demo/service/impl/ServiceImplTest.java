package com.example.demo.service.impl;

import com.example.demo.dao.EmpRepository;
import com.example.demo.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceImplTest {

    @Mock
    private EmpRepository repository;

    @InjectMocks
    private ServiceImpl service;

    private Employee emp;

    @BeforeEach
    void setUp() {
        emp = new Employee();
        emp.setId(1);
        emp.setEmail("test@example.com");
        emp.setFirstname("John");
    }

    @Test
    void testPostEmpIfEmailAlreadyExists() {
        // Since it has email , so it should return true as boolean existsByEmail(String email)
        when(repository.existsByEmail(emp.getEmail())).thenReturn(true);

        String result = service.postEmp(emp);

        assertEquals("Employee with this mailid: test@example.com already exist", result);

        // Verify a Method was Called
        verify(repository).existsByEmail(emp.getEmail());

        // Verify a Method Was Not Called
        verify(repository , never()).existsById(anyInt());
        verify(repository, never()).save(any(Employee.class));
    }

    @Test
    void testPostEmpIfIdAlreadyExists() {
        when(repository.existsById(emp.getId())).thenReturn(true);

        String result = service.postEmp(emp);

        assertEquals("Employee with this id: 1 already exist", result);
        verify(repository).existsById(emp.getId());

        verify(repository).existsByEmail(anyString());
        verify(repository, never()).save(any(Employee.class));
    }

    @Test
    void testPostEmpSuccess() {
        when(repository.existsByEmail(emp.getEmail())).thenReturn(false);
        when(repository.existsById(emp.getId())).thenReturn(false);

        String result = service.postEmp(emp);

        assertEquals("Hi John your Registration process successfully completed", result);
        verify(repository).existsByEmail(emp.getEmail());
        verify(repository).existsById(emp.getId());
        verify(repository).save(emp);
    }

    @Test
    void testPostEmpIfNullValues() {
        Employee nullEmp = new Employee();
        nullEmp.setId(0);
        nullEmp.setEmail(null);
        nullEmp.setFirstname(null);

        assertThrows(NullPointerException.class, () -> {
            service.postEmp(nullEmp);
        });

        verify(repository, never()).existsByEmail(anyString());
        verify(repository, never()).existsById(anyInt());
        verify(repository, never()).save(any(Employee.class));
    }
}