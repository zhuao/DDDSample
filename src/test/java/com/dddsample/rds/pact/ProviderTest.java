package com.dddsample.rds.pact;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslJsonArray;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.RequestResponsePact;
import au.com.dius.pact.provider.junit.PactRunner;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactSource;
import au.com.dius.pact.provider.junit.target.HttpTarget;
import au.com.dius.pact.provider.junit.target.Target;
import au.com.dius.pact.provider.junit.target.TestTarget;
import com.dddsample.rds.pact.DynamicPactSource.PactCodeLoader;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

@RunWith(PactRunner.class)
@Provider("EC2")
@PactSource(PactCodeLoader.class)
public class ProviderTest {

    @TestTarget
    public final Target target = new HttpTarget("localhost",9001);

    @Pact(provider = "EC2", consumer = "EC2-Client")
    public static RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        PactDslJsonBody requestBody = new PactDslJsonBody()
                .stringType("flavor_id", "m2.xlarge")
                .stringType("az", "SZ");


        PactDslJsonBody responseBody = new PactDslJsonBody()
                .stringType("az", "SZ")
                .stringType("flavor_id", "m2.xlarge")
                .stringType("status", "Created");

        return builder.given("Instance-happy").uponReceiving("Instance-Create")
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
    public static RequestResponsePact getInstances(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        PactDslJsonBody responseBody = PactDslJsonArray.arrayMinLike(1, 3)
                .stringType("az", "SH")
                .stringType("flavor_id")
                .stringType("status");




//        DslPart responseBody = new PactDslJsonArray()
//                .template(personTemplate, 18)
//                .stringType("az", "SZ")
//                .stringType("flavor_id", "m2.xlarge")
//                .stringType("status", "Created")
                ;


        return builder.given("Instance-happy").uponReceiving("Instance-Get")
                .path("/instances")
                .method("GET")
                .headers(headers)
                .willRespondWith()
                .status(200)
                .body(responseBody)
                .toPact();

    }

    //TODO:the order to run
    //TODO: assert response data
//    @State("Instance-Create")
    public void should_pass() {
    }

    @State("Instance-happy")
    public void should_find_all_instance() {

    }


}
