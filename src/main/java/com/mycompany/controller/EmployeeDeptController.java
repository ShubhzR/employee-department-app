package com.mycompany.controller;

import com.mycompany.model.Department;
import com.mycompany.model.Employee;
import com.mycompany.service.EmployeeDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class EmployeeDeptController {

    @Autowired
    private EmployeeDeptService service;

    @GetMapping("/employees")
    public Collection<Employee> getEmployees() {
        return service.getAllEmployees();
    }

    @PostMapping("/employee")
    public void addEmployee(@RequestBody Employee employee) {
        service.addEmployee(employee);
    }

    @PutMapping("/employee/{id}/move/dept/{newDeptId}/manager/{newManagerId}")
    public void moveEmployee(@PathVariable Long id, @PathVariable Long newDeptId, @PathVariable Long newManagerId) {
        service.moveEmployee(id, newDeptId, newManagerId);
    }

    @GetMapping("/departments")
    public Collection<Department> getDepartments() {
        return service.getAllDepartments();
    }

    @PostMapping("/departments")
    public void addDepartment(@RequestBody Department department) {
        service.addDepartment(department);
    }

    @PutMapping("/departments/{id}/head")
    public void changeDepartmentHead(@PathVariable Long id, @RequestParam Long newHeadId) {
        service.changeDepartmentHead(id, newHeadId);
    }
}
