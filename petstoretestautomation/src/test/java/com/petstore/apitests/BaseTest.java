package com.petstore.apitests;

import org.testng.annotations.BeforeClass;

import io.restassured.RestAssured;

public class BaseTest {
    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://petstore.swagger.io/v2/pet";
    }
}
