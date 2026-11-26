## APIs

GET     /api/employees          - list
GET     /api/employees/{id}     - view
POST    /api/employees          - add
PUT     /api/employees/{id}     - edit
GET     /api/departments        - drop down


## Entities

Employee (
    id PK,
    first_name,
    last_name,
    date_of_birth,
    department,
    salary,
    manager_id FK -> employee.id NULL
    )


