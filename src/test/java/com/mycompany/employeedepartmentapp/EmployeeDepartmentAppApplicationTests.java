package com.mycompany.employeedepartmentapp;

import com.mycompany.model.Employee;
import com.mycompany.service.EmployeeDeptService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmployeeDepartmentAppApplicationTests {

	@Autowired
	private EmployeeDeptService service;

	@Test
	void contextLoads() {
	}

	@Test
	void testChangeDepartmentHead() {
		Employee e = new Employee(5L, "Niraj", 2L, 101L);
		service.addEmployee(e);

		Employee newHead = new Employee(6L, "Hariom", 1L, 101L);
		service.addEmployee(newHead);

		//  Mayur -> Hariom
		service.changeDepartmentHead(101L, 6L);

		Employee updated = service.getAllEmployees().stream().filter(emp -> emp.getId() == 5L).findFirst().get();
		assertEquals(6L, updated.getManagerId(), "Manager should be updated to new head");
	}

}
