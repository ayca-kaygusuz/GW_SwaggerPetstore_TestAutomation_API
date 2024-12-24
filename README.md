
# Pet Store API Test Automation

## Overview
This project contains automated tests for the Pet Store API using Java, TestNG, and RestAssured. It aims to verify the CRUD operations of pets in the Pet Store.

## Prerequisites
- Java 21
- Maven
- TestNG
- RestAssured
- Mockito

## Dependencies
The following dependencies are included in the `pom.xml`:

- **RestAssured**: For API testing.
- **TestNG**: For running the tests.
- **Logback**: For logging.
- **Mockito**: For mocking objects in tests.

## Running the Tests
1. Navigate to the `petstoretestautomation` directory.
2. If this is the first time you are setting up the project, run:
    ```bash
    mvn clean install
    ```
    This command will also run the tests.
3. Otherwise, run the following command to execute the tests:
    ```bash
    mvn test
    ```
4. If you are having dependency-related issues despite running a clean install, try running
    ```bash
    mvn dependency:copy-dependencies
    ```
    And then
    ```bash
    mvn clean install
    ```

## Test Classes
### PetApiTests
This class contains tests for the following operations:
- Create: Tests for creating a pet.
- Read: Tests for reading a pet.
- Update: Tests for updating a pet.
- Delete: Tests for deleting a pet.

## Test Cases
- **Positive Tests**: Verify successful creation, reading, updating, and deletion of pets.
- **Negative Tests**: Verify error handling for invalid inputs.

## Expected Failures
Certain test cases are known to fail due to the server's lack of validation on mandatory fields. These tests are commented out to prevent build failures, but you may uncomment them if you want to see proper failed QA checks for the server.

## Navigation
To access XML files and test reports, navigate to the petstoretestautomation/src/test/resources directory.

## License
This project is licensed under the GNU v3 License - see the LICENSE file for details.
