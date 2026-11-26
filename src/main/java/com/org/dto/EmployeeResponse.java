package com.org.dto;

import com.org.enums.Department;
import com.org.model.Employee;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeResponse(
        Long id,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        Department department,
        BigDecimal salary,
        Long managerId,
        String managerName
) {
    // Entity -> DTO (flat, so the self-reference never loops in JSON)
    public static EmployeeResponse from(Employee e) {
        Employee m = e.getManager();
        return new EmployeeResponse(
                e.getId(), e.getFirstName(), e.getLastName(), e.getDateOfBirth(),
                e.getDepartment(), e.getSalary(),
                m != null ? m.getId() : null,
                m != null ? m.getFirstName() + " " + m.getLastName() : null);
    }
}
