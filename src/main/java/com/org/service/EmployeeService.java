package com.org.service;

import com.org.dto.EmployeeRequest;
import com.org.dto.EmployeeResponse;
import com.org.exception.NotFoundException;
import com.org.model.Employee;
import com.org.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) { // constructor injection
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public List<EmployeeResponse> findAll() {
        return employeeRepository.findAll().stream().map(EmployeeResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public EmployeeResponse findById(Long id) {
        return EmployeeResponse.from(getOrThrow(id));
    }

    public EmployeeResponse create(EmployeeRequest request) {
        Employee e = new Employee();
        apply(e, request);
        return EmployeeResponse.from(employeeRepository.save(e));
    }

    public EmployeeResponse update(Long id, EmployeeRequest request) {
        Employee e = getOrThrow(id);
        apply(e, request);
        return EmployeeResponse.from(employeeRepository.save(e));
    }

    private void apply(Employee e, EmployeeRequest r) {
        e.setFirstName(r.firstName().trim());
        e.setLastName(r.lastName().trim());
        e.setDateOfBirth(r.dateOfBirth());
        e.setDepartment(r.department());
        e.setSalary(r.salary());
        e.setManager(resolveManager(e, r.managerId()));
    }

    private Employee resolveManager(Employee e, Long managerId) {
        if (managerId == null) return null;
        if (managerId.equals(e.getId())) {
            throw new IllegalArgumentException("An employee cannot be their own manager");
        }
        Employee manager = getOrThrow(managerId);
        // Prevent cycles: walk up the manager chain, it must never reach this employee
        for (Employee m = manager; m != null; m = m.getManager()) {
            if (e.getId() != null && e.getId().equals(m.getId())) {
                throw new IllegalArgumentException("This manager would create a reporting cycle");
            }
        }
        return manager;
    }

    private Employee getOrThrow(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee " + id + " not found"));
    }
}
