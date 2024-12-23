package com.petstore.apitests;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class PetApiTests extends BaseTest {

    private static final int PET_ID = 12345; // Random Pet ID
    private static final String PET_JSON = "{ \"id\": " + PET_ID + ", \"name\": \"Doggie\", \"status\": \"available\" }";

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
                .pathParam("petId", PET_ID)
                .when()
                .get("{petId}");

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("Doggie"), "Pet name should match");
    }

    // (U)pdate - crUd
    // PUT
    @Test(dependsOnMethods = "readPet")
    public void updatePet() {
        String updatedPetJson = "{ \"id\": " + PET_ID + ", \"name\": \"UpdatedDoggie\", \"status\": \"available\" }";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updatedPetJson)
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
                .pathParam("petId", PET_ID)
                .when()
                .delete("{petId}");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    // #endregion


    // #region NEGATIVE TESTS

    // Try accessing nonexistent (in this case, now deleted) pet
    @Test(dependsOnMethods = "deletePet")
    public void readNonExistentPet() {
        Response response = RestAssured.given()
                .pathParam("petId", PET_ID)
                .when()
                .get("{petId}");

        Assert.assertEquals(response.getStatusCode(), 404, "Should return 404 for non-existent pet");
    }

    // Try to create a new pet without a name
    @Test
    public void createPetWithoutName() {
        String petJson = "{ \"id\": 1, \"name\": null, \"status\": \"available\" }"; // Adjusted to raw JSON

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(petJson)
                .when()
                .post();

        Assert.assertEquals(response.getStatusCode(), 400, "Expecting Bad Request when name is null");
    }

    // #endregion
}