package org.exercise;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HumanResourcesTest {
    private final EmployeesRepository repository = mock(EmployeesRepository.class);
    private final HumanResources humanResources = new HumanResources(repository);

    @Test
    public void recruit() {
        Employee in = new Employee("John", "Lennon");
        Employee out = new Employee("John", "Lennon");
        when(repository.save(in)).thenReturn(out);
        assertSame(out, humanResources.recruit(in));
    }

    @Test
    public void list() {
        List<Employee> list = Arrays.asList(new Employee("John", "Lennon"), new Employee("Paul", "McCartney"));
        when(repository.findAll()).thenReturn(list);
        assertEquals(list, humanResources.list());
    }

    @Test
    public void getExisting() {
        Employee john = new Employee("John", "Lennon");
        when(repository.findById(1L)).thenReturn(Optional.of(john));
        assertSame(john, humanResources.get(1L));
    }

    @Test
    public void getNotExisting() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertNull(humanResources.get(1L));
    }

    @Test
    public void fire() {
        humanResources.fire(123L);
        verify(repository).deleteById(123L);
    }
}