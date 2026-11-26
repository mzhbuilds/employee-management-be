package com.org.controller;

import com.org.dto.EmployeeRequest;
import com.org.dto.EmployeeResponse;
import com.org.enums.Department;
import com.org.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173") // Vite dev server port
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public List<EmployeeResponse> list() {
        return employeeService.findAll();
    }

    @GetMapping("/employees/{id}")
    public EmployeeResponse get(@PathVariable Long id) {
        return employeeService.findById(id);
    }

    @PostMapping("/employees")
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse create(@Valid @RequestBody EmployeeRequest request) {
        return employeeService.create(request);
    }

    @PutMapping("/employees/{id}")
    public EmployeeResponse update(@PathVariable Long id, @Valid @RequestBody EmployeeRequest request) {
        return employeeService.update(id, request);
    }

    // Feeds the Department dropdown so the list lives in one place (the enum)
    @GetMapping("/departments")
    public Department[] departments() {
        return Department.values();
    }
}
