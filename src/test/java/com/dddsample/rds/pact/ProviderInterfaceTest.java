package com.dddsample.rds.pact;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponseInteraction;
import au.com.dius.pact.model.RequestResponsePact;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

/**
 * Created by azhu on 27/06/2017.
 */
public class ProviderInterfaceTest {



    @Rule
    public PactRecorderRule interceptorRule = new PactRecorderRule("EC2", this);

    @Pact(provider = "EC2", consumer = "EC2-Client")
    public RequestResponsePact instanceCreationPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        PactDslJsonBody requestBody = new PactDslJsonBody()
                .stringType("flavor_id", "m2.xlarge")
                .stringType("az", "SZ");


        PactDslJsonBody responseBody = new PactDslJsonBody()
                .stringType("az", "SZ")
                .stringType("flavor_id", "m2.xlarge")
                .stringType("status", "Created")
                .stringType("instance_id", "013499b6-e581-4ea4-aea0-4a648db2520b");

        return builder.uponReceiving("Instance-Create")
                .path("/instances")
                .method("POST")
                .body(requestBody)
                .headers(headers)
                .willRespondWith()
                .status(201)
                .body(responseBody)
                .toPact();

    }

    @Pact(provider = "EC2", consumer = "EC2-Client")
    public RequestResponsePact getInstance(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        PactDslJsonBody responseBody = PactDslJsonArray.arrayMinLike(1, 3)
                .stringType("az", "SH")
                .stringType("instance_id", "013499b6-e581-4ea4-aea0-4a648db2520b")
                .stringType("flavor_id", "m2.xlarge")
                .stringType("status", "Created");


        return builder.uponReceiving("Instance-Get")
                .path("/instances/013499b6-e581-4ea4-aea0-4a648db2520b")
                .method("GET")
                .headers(headers)
                .willRespondWith()
                .status(200)
                .body(responseBody)
                .toPact();

    }

    @Before
    public void setup() {
        RestAssured.baseURI  = "http://localhost";
        RestAssured.port     = 9001;
        RestAssured.basePath = "/";
    }

    @Test
    @PactVerification(fragment = "instanceCreationPact")
    public void should_create_instance_successfully() {
        RequestResponseInteraction interaction = interceptorRule.getPactInteraction("instanceCreationPact");

        given()
                .headers(interaction.getRequest().getHeaders())
                .body(interaction.getRequest().getBody().getValue())
                .post(interaction.getRequest().getPath())

        .then()
                .statusCode(201)
                .body("az", is("SZ"))
                .body("flavor_id", is("m2.xlarge"));

    }

    @Test
    @PactVerification(fragment = "getInstance")
    public void should_get_instance_successfully() {
        RequestResponseInteraction interaction = interceptorRule.getPactInteraction("getInstance");

        given()
                .headers(interaction.getRequest().getHeaders())
                .get(interaction.getRequest().getPath())
        .then()
                .statusCode(200)
                .body("az", is("SZ"))
                .body("flavor_id", is("m2.xlarge"))
                .body("instance_id", is("013499b6-e581-4ea4-aea0-4a648db2520b"));

    }

}
