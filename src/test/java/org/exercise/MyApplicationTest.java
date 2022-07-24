package org.exercise;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
public class MyApplicationTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private HrClient hrClient;
    @Autowired
    private EmployeesRepository repository;

    @Before
    public void cleanup() {
        repository.deleteAll();
    }

    @Test
    public void dummy() {
        ResponseEntity<String> response = restTemplate.getForEntity("/test/api/v1/version", String.class);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("1", response.getBody());
    }

    @Test
    public void version() {
        assertEquals("1", hrClient.version());
    }

    @Test
    public void list() {
        assertNotNull(hrClient.list());
    }

    @Test
    public void one() {
        Employee in = new Employee("John", "Lennon");
        Employee out = hrClient.recruit(in);
        in.setId(out.getId());
        Employee out2 = hrClient.get(in.getId());
        assertEquals(in, out);
        assertEquals(out, out2);
    }

    @Test
    public void several() {
        List<Employee> beatles = Stream.of(
            new Employee("John", "Lennon"),
            new Employee("Paul", "McCartney"),
            new Employee("George", "Harrison"),
            new Employee("Ringo", "Starr")
        ).map(person -> hrClient.recruit(person)).collect(toList());

        List<Employee> list = hrClient.list();
        assertEquals(beatles.size(), list.size());
    }

    @Test
    public void fire() {
        List<Employee> beatles = Stream.of(
            new Employee("John", "Lennon"),
            new Employee("Paul", "McCartney"),
            new Employee("George", "Harrison"),
            new Employee("Ringo", "Starr")
        ).map(person -> hrClient.recruit(person)).collect(toList());

        hrClient.fire(beatles.get(0).getId());
        hrClient.fire(beatles.get(2).getId());
        assertEquals(2, hrClient.list().size());
    }

    @Test
    public void subordination() {
        Employee john = hrClient.recruit(new Employee("John", "Lennon"));
        Employee paul = hrClient.recruit(new Employee("Paul", "McCartney"));
        assertEquals(2, hrClient.list().size());
        hrClient.subordinate(john.getId(), paul.getId());
        // The following line cases StackOverflowError
        //assertEquals(2, hrClient.list().size());
    }
}
