package com.petstore.apitests;

import static org.junit.Assert.assertTrue;
import org.junit.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PetApiTests extends BaseTest{

    private static final int PET_ID = 12345; // Random Pet ID
    private static final String PET_JSON = 
        "{ \"id\": " + PET_ID + ", \"name\": \"Doggie\", \"status\": \"available\" }";

    // (C)reate
    @Test
    public void createPet() {
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(PET_JSON)
                .when()
                .post();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().
            contains("Doggie"), "Pet name should be in the response");
    }

    // (R)ead
    @Test(dependsOnMethods = "createPet")
    public void readPet() {
        Response response = RestAssured.given()
                .pathParam("petId", PET_ID)
                .when()
                .get("{petId}");

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().
            contains("Doggie"), "Pet name should match");
    }

    // (U)pdate
    @Test(dependsOnMethods = "readPet")
    public void updatePet() {
        String updatedPetJson = 
            "{ \"id\": " + PET_ID + ", \"name\": \"UpdatedDoggie\", \"status\": \"available\" }";

        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updatedPetJson)
                .when()
                .put();

        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(response.getBody().asString().contains("UpdatedDoggie"),
                "Updated pet name should be in the response");
    }

    // (D)elete
    @Test(dependsOnMethods = "updatePet")
    public void deletePet() {
        Response response = RestAssured.given()
                .pathParam("petId", PET_ID)
                .when()
                .delete("{petId}");

        Assert.assertEquals(response.getStatusCode(), 200);
    }

    // try accessing now deleted pet
    @Test
    public void readNonExistentPet() {
        Response response = RestAssured.given()
                .pathParam("petId", PET_ID)
                .when()
                .get("{petId}");

        Assert.assertEquals(response.getStatusCode(), 404, "Should return 404 for non-existent pet");
    }
}
