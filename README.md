# Employee Management – Backend

Spring Boot 3 (Java 17) REST API with Spring Data JPA, Bean Validation and H2.
Frontend: https://github.com/mzhbuilds/employee-management-fe

## Run
```bash
mvn spring-boot:run        # or run EmployeeApplication in IntelliJ
```
API on http://localhost:8080. H2 console: http://localhost:8080/h2-console
(JDBC URL `jdbc:h2:file:./data/employeedb`, user `sa`, empty password)

Tests: `mvn test`

## API
| Method | URL | Description |
|---|---|---|
| GET | `/api/employees` | List all employees |
| GET | `/api/employees/{id}` | Get one employee (404 if missing) |
| POST | `/api/employees` | Create (201; 400 on validation error) |
| PUT | `/api/employees/{id}` | Update |
| GET | `/api/departments` | Department values for the dropdown |

## Design
- `employee` table with self-referencing FK `manager_id -> employee.id` (`@ManyToOne` to `Employee`)
- Layers: controller → service → repository → entity; record DTOs for request/response
- Validation with `@Valid` + global `@RestControllerAdvice` (400 / 404 as JSON)
- Rules: an employee cannot be their own manager; reporting cycles are blocked
- Department enum stored as string; salary as `BigDecimal`
- CORS allows the Vite dev server (http://localhost:5173)
