package org.exercise;

import org.springframework.stereotype.Service;

@Service
public class HumanResources {
    private final EmployeesRepository repository;

    public HumanResources(EmployeesRepository repository) {
        this.repository = repository;
    }

    public Employee recruit(Employee employee) {
        return repository.save(employee);
    }

    public Iterable<Employee> list() {
        return repository.findAll();
    }

    public Employee get(long id) {
        return repository.findById(id).orElse(null);
    }

    public void fire(long id) {
        repository.deleteById(id);
    }
}
