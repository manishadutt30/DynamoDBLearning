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
│   │                   └── DynamoDBBase.java
│   └── test/
│       └── java/
│           └── com/
│               └── learning/
│                   └── dynamodb/
│                       └── DynamoDBBaseTest.java
└── pom.xml
```

## Features

- **DynamoDBBase**: A base class that provides common DynamoDB operations including:
  - Table management (create, delete, describe, list)
  - Item operations (put, get, update, delete)
  - Scan operations
  - Utility methods for creating attribute values

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

```java
import com.learning.dynamodb.DynamoDBBase;
import software.amazon.awssdk.regions.Region;

public class Example {
    public static void main(String[] args) {
        // Create a DynamoDB base instance
        DynamoDBBase dynamoDB = new DynamoDBBase(Region.US_EAST_1);
        
        try {
            // List all tables
            var tables = dynamoDB.listTables();
            System.out.println("Tables: " + tables.tableNames());
        } finally {
            // Always close the client
            dynamoDB.close();
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