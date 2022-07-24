package org.exercise;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class HrController {
    private final HumanResources hr;

    public HrController(HumanResources hr) {
        this.hr = hr;
    }

    /**
     * curl http://localhost:8080/test/api/v1/version
     * @return version
     */
    @GetMapping("/test/api/v1/version")
    public String version() {
        return "1";
    }

    /**
     * curl -X POST http://localhost:8080/test/api/v1/employees -H 'Content-Type: application/json' -d '{"firstName":"John","lastName":"Lennon"}'
     * @param employee the employee to recruit
     * @return the instance created in DB
     */
    @PostMapping(path = "/test/api/v1/employees", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Employee recruit(@RequestBody Employee employee) {
        return hr.recruit(employee);
    }

    /**
     * curl -X POST http://localhost:8080/test/api/v1/subordination/1/2 -H 'Content-Type: application/json'
     * @param managerId - manager ID
     * @param employeeId - employee ID
     */
    @PostMapping(path = "/test/api/v1/subordination/{managerId}/{employeeId}", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void subordinate(@PathVariable("managerId") long managerId, @PathVariable("employeeId") long employeeId) {
        hr.subordinate(managerId, employeeId);
    }

    /**
     * curl http://localhost:8080/test/api/v1/employees
     * @return all employees
     */
    @GetMapping(path = "/test/api/v1/employees", produces = APPLICATION_JSON_VALUE)
    public Iterable<Employee> list() {
        return hr.list();
    }

    /**
     * curl http://localhost:8080/test/api/v1/employees/1
     * @param id the employee ID
     * @return employee identified by given ID
     */
    @GetMapping(path = "/test/api/v1/employees/{id}", produces = APPLICATION_JSON_VALUE)
    public Employee get(@PathVariable long id) {
        return hr.get(id);
    }

    @GetMapping("employees/{id}")
    public void fire(@PathVariable("id") long id) {
        hr.fire(id);
    }
}
