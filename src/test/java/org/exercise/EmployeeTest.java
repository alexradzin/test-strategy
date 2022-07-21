package org.exercise;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class EmployeeTest {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void serializationWithId() throws IOException {
        serialization(new Employee(1L, "John", "Lennon"), Employee.class);
    }

    @Test
    public void serializationWithoutId() throws IOException {
        serialization(new Employee("John", "Lennon"), Employee.class);
    }

    @Test
    public void testHashCode() {
        int john = new Employee(1L, "John", "Lennon").hashCode();
        int paul = new Employee(2L, "Paul", "McCartney").hashCode();
        assertTrue(john != 0);
        assertTrue(paul != 0);
        assertNotEquals(john, paul);
    }

    @Test
    public void testToString() {
        String john = new Employee(1L, "John", "Lennon").toString();
        assertTrue(john.contains("John"));
        assertTrue(john.contains("Lennon"));
    }

    private <T> void serialization(T obj, Class<T> clazz) throws IOException {
        assertEquals(obj, objectMapper.readValue(objectMapper.writeValueAsString(obj), clazz));
    }
}
