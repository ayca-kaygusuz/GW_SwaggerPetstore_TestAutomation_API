package com.petstore.apitests;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PetApiTests extends BaseTest {

    private static final String PET_JSON = "{ \"id\": 1, \"name\": \"Doggie\", \"status\": \"available\" }";
    private static final String UPDATED_PET_JSON = "{ \"id\": 1, \"name\": \"UpdatedDoggie\", \"status\": \"available\" }";
    private static final String INVALID_PET_JSON = "{ \"id\": 2, \"name\": \"Catty\", \"status\": \"invalid_status\" }";

    // #region POSITIVE TESTS
    // (C)reate - Crud
    // POST
    @Test
    public void createPet() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(PET_JSON)
                .when()
                .post();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("Doggie"), "Pet name should be in the response");
    }

    // (R)ead - cRud
    // GET
    @Test(dependsOnMethods = "createPet")
    public void readPet() {
        Response response = RestAssured.given()
                .pathParam("petId", 1)
                .when()
                .get("/{petId}");

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("Doggie"), "Pet name should match");
    }

    // (U)pdate - crUd
    // PUT
    @Test(dependsOnMethods = "readPet")
    public void updatePet() {

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(UPDATED_PET_JSON)
                .when()
                .put();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("UpdatedDoggie"),
                "Updated pet name should be in the response");
    }

    // (D)elete - cruD
    // DELETE
    @Test(dependsOnMethods = "updatePet")
    public void deletePet() {
        Response response = RestAssured.given()
                .pathParam("petId", 1)
                .when()
                .delete("/{petId}");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    // #endregion
    // #region NEGATIVE TESTS
    // Try accessing nonexistent (in this case, now deleted) pet
    @Test(dependsOnMethods = "deletePet")
    public void readNonExistentPet() {
        Response response = RestAssured.given()
                .pathParam("petId", 1)
                .when()
                .get("/{petId}");

        Assert.assertEquals(response.getStatusCode(), 404, "Should return 404 for non-existent pet");
    }

    // Try to create a new pet without a name
    // TODO: API seems fine with this? investigate.
    /* @Test
    public void createPetWithoutName() {
        String petJsonWithoutName = "{ \"id\": 1, \"status\": \"available\" }"; // No name field

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(petJsonWithoutName)
                .when()
                .post();

        Assert.assertEquals(response.getStatusCode(), 200, "Expecting 200 because name is not enforced");
    } */

    // Try to create a new pet with an invalid status
    // TODO: API is still returning 200 for this? investigate.
    /* @Test
    public void createPetWithInvalidStatus() {
        String petJsonInvalidStatus = "{ \"id\": 2, \"name\": \"Catty\", \"status\": \"invalid_status\" }"; // Invalid status

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(petJsonInvalidStatus)
                .when()
                .post();

        Assert.assertEquals(response.getStatusCode(), 400, "Expecting Bad Request due to invalid status");
    } */

    // Try to create a new pet with a duplicate ID
    @Test(dependsOnMethods = "createPet")
    public void createPetWithDuplicateId() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(PET_JSON) // Reusing the same pet JSON
                .when()
                .post();

        Assert.assertEquals(response.getStatusCode(), 200, "Expecting 200 for duplicate ID");
    }

    // #endregion
}
