package org.exercise;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HrControllerTest {
    private final HumanResources hr = mock(HumanResources.class);
    private final HrController controller = new HrController(hr);

    @Test
    public void recruit() {
        Employee in = new Employee("John", "Lennon");
        Employee out = new Employee("John", "Lennon");
        when(hr.recruit(in)).thenReturn(out);
        assertSame(out, controller.recruit(in));
    }

    @Test
    public void list() {
        List<Employee> list = Arrays.asList(new Employee("John", "Lennon"), new Employee("Paul", "McCartney"));
        when(hr.list()).thenReturn(list);
        assertEquals(list, controller.list());
    }

    @Test
    public void getExisting() {
        Employee john = new Employee("John", "Lennon");
        when(hr.get(1L)).thenReturn(john);
        assertSame(john, controller.get(1L));
    }

    @Test
    public void getNotExisting() {
        when(hr.get(1L)).thenReturn(null);
        assertNull(controller.get(1L));
    }

    @Test
    public void fire() {
        controller.fire(123L);
        verify(hr).fire(123L);
    }

    @Test
    public void version() {
        assertEquals("1", controller.version());
    }
}