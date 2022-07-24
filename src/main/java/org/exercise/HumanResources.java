package org.exercise;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Stream;

import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

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

    public void subordinate(long managerId, long employeeId) {
        Employee manager = getById(managerId);
        Employee employee = getById(employeeId);
        manager.setSubordinates(ofNullable(manager.getSubordinates())
            .map(existing -> Stream.concat(existing.stream(), Stream.of(employee)).collect(toList()))
            .orElse(singletonList(employee)));
        employee.setManager(manager);
        repository.save(manager);
        repository.save(employee);
        // The following line works exactly and 2 previous but it causes mocked test to fail
        //repository.saveAll(Arrays.asList(manager, employee));
    }

    private Employee getById(long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Employee " + id));
    }
}
