package org.exercise;

import org.junit.Test;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
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

    @Test
    public void successfulSubordinate() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Employee("The", "Boss")));
        when(repository.findById(2L)).thenReturn(Optional.of(new Employee("The", "Worker")));
        humanResources.subordinate(1, 2);
        verify(repository, times(2)).save(any());
    }

    @Test
    public void subordinateManagerNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        when(repository.findById(2L)).thenReturn(Optional.of(new Employee("The", "Worker")));
        try {
            humanResources.subordinate(1, 2);
            fail();
        } catch (EntityNotFoundException e) {
            assertEquals("Employee 1", e.getMessage());
        }
        verify(repository, never()).save(any());
    }

    @Test
    public void subordinateEmployeeNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(new Employee("The", "Boss")));
        when(repository.findById(2L)).thenReturn(Optional.empty());
        try {
            humanResources.subordinate(1, 2);
            fail();
        } catch (EntityNotFoundException e) {
            assertEquals("Employee 2", e.getMessage());
        }
        verify(repository, never()).save(any());
    }
}