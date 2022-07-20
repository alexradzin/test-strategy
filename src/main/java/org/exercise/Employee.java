package org.exercise;

import javax.persistence.Entity;

@Entity
public class Employee {
    private final String firstName;
    private final String lastName;

    public Employee(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
