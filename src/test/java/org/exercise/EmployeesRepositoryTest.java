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
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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

    @Test
    public void band() {
        Employee john = new Employee("John", "Lennon");
        Employee paul = new Employee("Paul", "McCartney");
        Employee george = new Employee("George", "Harrison");
        Employee ringo = new Employee("Ringo", "Starr");
        List<Employee> others = Arrays.asList(paul, george, ringo);
        others.forEach(e -> e.setManager(john));
        john.setSubordinates(others);

        long johnId = repository.save(john).getId();
        Set<Long> otherIds = employeeIds(repository.saveAll(others));
        Iterable<Employee> all = repository.findAll();

        for (Employee e : all) {
            if (Objects.equals(johnId, e.getId())) {
                assertNull(e.getManager());
                assertEquals(otherIds, employeeIds(e.getSubordinates()));
            } else {
                assertNull(e.getSubordinates());
                assertEquals(johnId, e.getManager().getId().intValue());
            }
        }
    }

    private Set<Long> employeeIds(Iterable<Employee> employees) {
        Set<Long> ids = new HashSet<>();
        for (Employee e : employees) {
            ids.add(e.getId());
        }
        return ids;
    }
}
