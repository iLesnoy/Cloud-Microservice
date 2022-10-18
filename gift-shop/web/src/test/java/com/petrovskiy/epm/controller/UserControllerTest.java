package com.petrovskiy.epm.controller;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;

class UserControllerTest {

    private static WireMockServer wireMockServer;
    private ResponseDefinitionBuilder response = new ResponseDefinitionBuilder();
    /*@Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().port(8080));*/


    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(options().port(8080));
        wireMockServer.start();
        response.withStatus(200);
        WireMock.configureFor("localhost",8080);
        WireMock.stubFor(
                WireMock.get("/api/users").willReturn(response));
    }

    @Test
    void findById() throws URISyntaxException {
         RestAssured.given().when()
                .get(new URI("http://localhost:8080/api/users/1"))
                .then().assertThat().statusCode(200);
    }

    public static void tearDown(){
        if(wireMockServer.isRunning()){
            wireMockServer.shutdownServer();
        }
    }

}