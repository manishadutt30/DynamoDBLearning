# DynamoDBLearning

A Java project for learning AWS DynamoDB using AWS SDK 2.

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- AWS Account with DynamoDB access
- AWS credentials configured (via AWS CLI, environment variables, or IAM role)

## Project Structure

```
DynamoDBLearning/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── learning/
│   │               └── dynamodb/
│   │                   ├── DynamoDBBase.java
│   │                   ├── model/
│   │                   │   └── User.java
│   │                   └── examples/
│   │                       └── UserExample.java
│   └── test/
│       └── java/
│           └── com/
│               └── learning/
│                   └── dynamodb/
│                       ├── DynamoDBBaseTest.java
│                       └── model/
│                           └── UserTest.java
└── pom.xml
```

## Features

- **DynamoDBBase**: A base class that provides common DynamoDB operations including:
  - Table management (create, delete, describe, list)
  - Item operations using AttributeValue maps (put, get, update, delete)
  - **Data Model Support**: Work with POJOs using DynamoDB Enhanced Client
  - CRUD operations for data models (save, get, update, delete, scan)
  - Utility methods for creating attribute values

- **Data Models**: Pre-built data model classes with DynamoDB annotations:
  - **User**: Model with userId, firstName, lastName, age, and address fields
  - Easy to create additional data models following the same pattern

- **Examples**: Ready-to-use example code demonstrating:
  - How to work with data models
  - CRUD operations with POJOs
  - Best practices for DynamoDB operations

## Dependencies

- AWS SDK 2 for DynamoDB (v2.20.0)
- AWS SDK 2 DynamoDB Enhanced Client
- JUnit 5 for testing

## Building the Project

```bash
mvn clean compile
```

## Running Tests

```bash
mvn test
```

Note: Tests require AWS credentials to be configured and proper DynamoDB permissions.

## Usage Example

### Working with Data Models (Recommended)

The preferred approach is to use data models (POJOs) with DynamoDB Enhanced Client:

```java
import com.learning.dynamodb.DynamoDBBase;
import com.learning.dynamodb.model.User;
import software.amazon.awssdk.regions.Region;

public class Example {
    public static void main(String[] args) {
        try (DynamoDBBase dynamoDB = new DynamoDBBase(Region.US_EAST_1)) {
            String tableName = "Users";
            
            // Create table for User model (only needed once)
            // dynamoDB.createTableForModel(User.class, tableName);
            
            // Create and save a user
            User user = new User("user001", "John", "Doe", 30, "123 Main St");
            dynamoDB.saveItem(User.class, tableName, user);
            
            // Retrieve the user
            User keyUser = new User();
            keyUser.setUserId("user001");
            User retrievedUser = dynamoDB.getItemByKey(User.class, tableName, keyUser);
            System.out.println("Retrieved: " + retrievedUser);
            
            // Update the user
            retrievedUser.setAge(31);
            dynamoDB.updateItemModel(User.class, tableName, retrievedUser);
            
            // Scan all users
            var allUsers = dynamoDB.scanAllItems(User.class, tableName);
            System.out.println("Total users: " + allUsers.size());
            
            // Delete the user
            dynamoDB.deleteItemByKey(User.class, tableName, keyUser);
        }
    }
}
```

### Creating Your Own Data Models

You can create additional data models by following the User model pattern:

```java
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class YourModel {
    private String id;
    private String field1;
    private String field2;
    
    public YourModel() {} // Required default constructor
    
    @DynamoDbPartitionKey
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    // Add getters and setters for other fields
}
```

### Working with AttributeValue Maps (Low-Level)

For advanced use cases, you can still use the low-level client with AttributeValue maps:

```java
import com.learning.dynamodb.DynamoDBBase;
import software.amazon.awssdk.regions.Region;

public class Example {
    public static void main(String[] args) {
        try (DynamoDBBase dynamoDB = new DynamoDBBase(Region.US_EAST_1)) {
            // List all tables
            var tables = dynamoDB.listTables();
            System.out.println("Tables: " + tables.tableNames());
        }
    }
}
```

## AWS Configuration

Ensure your AWS credentials are configured. You can do this via:

1. AWS CLI: `aws configure`
2. Environment variables: `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY`
3. IAM role (if running on EC2, ECS, or Lambda)

## License

This is a learning project for educational purposes.