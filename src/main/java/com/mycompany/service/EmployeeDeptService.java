package com.mycompany.service;

import com.mycompany.exception.BusinessException;
import com.mycompany.model.Department;
import com.mycompany.model.Employee;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmployeeDeptService {

    private final Map<Long, Department> departments = new HashMap<>();
    private final Map<Long, Employee> employees = new HashMap<>();

    @PostConstruct
    public void initData() {
        departments.put(101L, new Department(101L, "Engineering", 2L));
        departments.put(102L, new Department(102L, "Marketing", 3L));
        departments.put(103L, new Department(103L, "HR", 4L));

        employees.put(1L, new Employee(1L, "Shubham - CEO", null, null));
        employees.put(2L, new Employee(2L, "Mayur - Eng Head", 1L, 101L));
        employees.put(3L, new Employee(3L, "Sandeep - Mkt Head", 1L, 102L));
        employees.put(4L, new Employee(4L, "Aniket - HR Head", 1L, 103L));
    }

    public Collection<Employee> getAllEmployees() {
        return employees.values();
    }

    public Collection<Department> getAllDepartments() {
        return departments.values();
    }

    public void addDepartment(Department department) {
        if (department.getId() == null || departments.containsKey(department.getId())) {
            throw new BusinessException("Invalid or duplicate department ID");
        }
        departments.put(department.getId(), department);
    }

    public void addEmployee(Employee employee) {
        validateEmployee(employee);
        employees.put(employee.getId(), employee);
    }

    public void moveEmployee(Long empId, Long newDeptId, Long newManagerId) {
        Employee emp = employees.get(empId);
        if (emp == null || !departments.containsKey(newDeptId)) {
            throw new BusinessException("Invalid employee or department ID");
        }
        emp.setDepartmentId(newDeptId);
        emp.setManagerId(newManagerId);
        if (!isHierarchyValid(emp)) {
            throw new BusinessException("Hierarchy violation: new department doesn't match management chain");
        }
    }

    public void changeDepartmentHead(Long deptId, Long newHeadId) {
        Department dept = departments.get(deptId);
        if (dept == null || !employees.containsKey(newHeadId)) {
            throw new BusinessException("Invalid department or employee ID");
        }

        Employee newHead = employees.get(newHeadId);
        if (!Objects.equals(newHead.getDepartmentId(), deptId)) {
            throw new BusinessException("New head must belong to the department");
        }

        Long oldHeadId = dept.getHeadId();
        dept.setHeadId(newHeadId);
        updateManagerChain(deptId, oldHeadId, newHeadId);
    }

    private void validateEmployee(Employee employee) {
        if (employee.getId() == null || employees.containsKey(employee.getId())) {
            throw new BusinessException("Invalid or duplicate employee ID");
        }
        if (employee.getManagerId() != null && !employees.containsKey(employee.getManagerId())) {
            throw new BusinessException("Manager must be a valid employee");
        }
        if (employee.getDepartmentId() != null && !departments.containsKey(employee.getDepartmentId())) {
            throw new BusinessException("Invalid department ID");
        }
        if (!isHierarchyValid(employee)) {
            throw new BusinessException("Invalid employee hierarchy");
        }
    }

    private boolean isHierarchyValid(Employee emp) {
        if (emp.getManagerId() == null) return emp.getDepartmentId() == null;
        Long deptId = emp.getDepartmentId();
        Long managerId = emp.getManagerId();
        while (managerId != null) {
            Employee manager = employees.get(managerId);
            if (manager == null) return false;
            if (manager.getId().equals(1L)) return true;
            if (!Objects.equals(manager.getDepartmentId(), deptId)) return false;
            managerId = manager.getManagerId();
        }
        return false;
    }

    private void updateManagerChain(Long deptId, Long oldHeadId, Long newHeadId) {
        for (Employee emp : employees.values()) {
            if (Objects.equals(emp.getDepartmentId(), deptId) && emp.getManagerId().equals(oldHeadId)) {
                emp.setManagerId(newHeadId);
            }
        }
    }
}