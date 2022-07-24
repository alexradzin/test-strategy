package org.exercise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@Service
public class HrClient {
    @Autowired
    private TestRestTemplate restTemplate;

    public String version() {
        return restTemplate.getForEntity("/test/api/v1/version", String.class).getBody();
    }

    public Employee recruit(Employee in) {
        return restTemplate.postForEntity("/test/api/v1/employees", in, Employee.class).getBody();
    }

    public void subordinate(long managerId, long employeeId) {
        HttpHeaders headers = new HttpHeaders();
        headers.put(HttpHeaders.CONTENT_TYPE, singletonList("application/json"));
        restTemplate.postForEntity(format("/test/api/v1/subordination/%d/%d", managerId, employeeId), new HttpEntity<>(headers), Void.class);
    }

    public List<Employee> list() {
        Employee[] result = restTemplate.getForEntity("/test/api/v1/employees", Employee[].class).getBody();
        return result == null ? null : asList(result);
    }

    public Employee get(long id) {
        return restTemplate.getForEntity("/test/api/v1/employees/" + id, Employee.class).getBody();
    }

    public void fire(long id) {
        restTemplate.delete("/test/api/v1/employees/" + id);
    }
}
