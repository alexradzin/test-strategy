package org.exercise;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // for restTemplate
public class MyApplicationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void dummy() {
        ResponseEntity<String> response = restTemplate.getForEntity("/test/api/v1/version", String.class);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("1", response.getBody());
    }
}