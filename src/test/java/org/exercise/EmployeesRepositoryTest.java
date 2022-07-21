package org.exercise;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application.properties")
public class EmployeesRepositoryTest {
    @Autowired
    private EmployeesRepository repository;

    @Before
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void one() {
        Employee in = new Employee("John", "Lennon");
        Employee saveResult = repository.save(in);
        long id = saveResult.getId();
        Employee expected = new Employee(id, "John", "Lennon");
        assertEquals(expected, saveResult);
        Optional<Employee> found = repository.findById(id);
        assertTrue(found.isPresent());
        assertEquals(expected, found.get());
    }

    @Test
    public void several() {
        Set<Long> expected = employeeIds(repository.saveAll(Arrays.asList(
            new Employee("John", "Lennon"),
            new Employee("Paul", "McCartney"),
            new Employee("George", "Harrison"),
            new Employee("Ringo", "Starr")
        )));
        Set<Long> found = employeeIds(repository.findAll());
        assertEquals(expected, found);
    }

    private Set<Long> employeeIds(Iterable<Employee> employees) {
        Set<Long> ids = new HashSet<>();
        for (Employee e : employees) {
            ids.add(e.getId());
        }
        return ids;
    }
}
