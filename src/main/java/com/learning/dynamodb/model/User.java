package com.learning.dynamodb.model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

/**
 * User data model for DynamoDB.
 * This class represents a User entity with standard attributes.
 */
@DynamoDbBean
public class User {
    
    private String userId;
    private String firstName;
    private String lastName;
    private Integer age;
    private String address;
    
    /**
     * Default constructor required by DynamoDB Enhanced Client.
     */
    public User() {
    }
    
    /**
     * Constructor with all fields.
     */
    public User(String userId, String firstName, String lastName, Integer age, String address) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.address = address;
    }
    
    /**
     * Gets the user ID (partition key).
     */
    @DynamoDbPartitionKey
    public String getUserId() {
        return userId;
    }
    
    /**
     * Sets the user ID.
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    /**
     * Gets the first name.
     */
    public String getFirstName() {
        return firstName;
    }
    
    /**
     * Sets the first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    /**
     * Gets the last name.
     */
    public String getLastName() {
        return lastName;
    }
    
    /**
     * Sets the last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    /**
     * Gets the age.
     */
    public Integer getAge() {
        return age;
    }
    
    /**
     * Sets the age.
     */
    public void setAge(Integer age) {
        this.age = age;
    }
    
    /**
     * Gets the address.
     */
    public String getAddress() {
        return address;
    }
    
    /**
     * Sets the address.
     */
    public void setAddress(String address) {
        this.address = address;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", address='" + address + '\'' +
                '}';
    }
}
