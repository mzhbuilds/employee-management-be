package com.org.dto;

import com.org.enums.Department;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeRequest(
        @NotBlank(message = "First name is required") @Size(max = 50) String firstName,
        @NotBlank(message = "Last name is required") @Size(max = 50) String lastName,
        @NotNull(message = "Date of birth is required") @Past(message = "Date of birth must be in the past") LocalDate dateOfBirth,
        @NotNull(message = "Department is required") Department department,
        @NotNull(message = "Salary is required") @Positive(message = "Salary must be greater than 0") BigDecimal salary,
        Long managerId // optional
) {}
