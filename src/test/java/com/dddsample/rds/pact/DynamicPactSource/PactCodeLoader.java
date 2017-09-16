package com.dddsample.rds.pact.DynamicPactSource;

import au.com.dius.pact.consumer.ConsumerPactBuilder;
import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;
import au.com.dius.pact.model.RequestResponsePact;
import au.com.dius.pact.provider.junit.loader.PactLoader;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by azhu on 25/06/2017.
 */
public class PactCodeLoader implements PactLoader{

    private Class testClass;

    public PactCodeLoader(final Class<?> testClass) {
        this.testClass = testClass;
    }

    @Override
    public List<au.com.dius.pact.model.Pact> load(String providerName) throws IOException {
        LinkedList<au.com.dius.pact.model.Pact> pacts = new LinkedList<>();
        for (Method m: testClass.getMethods()) {
            if (conformsToSignature(m)) {
//            if (conformsToSignature(m) && methodMatchesFragment(m, fragment)) {
                Pact pact = m.getAnnotation(Pact.class);
                if (StringUtils.isEmpty(pact.provider()) || providerName.equals(pact.provider())) {
                    PactDslWithProvider dslBuilder = ConsumerPactBuilder.consumer(pact.consumer())
                            .hasPactWith(providerName);
                    try {
                        pacts.add((RequestResponsePact) m.invoke(null, dslBuilder));
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to invoke pact method", e);
                    }
                }
            }
        }
        return pacts;
    }

    private boolean methodMatchesFragment(Method m, String fragment) {
        return StringUtils.isEmpty(fragment) || m.getName().equals(fragment);
    }

    /**
     * validates method signature as described at {@link Pact}
     */
    private boolean conformsToSignature(Method m) {
        Pact pact = m.getAnnotation(Pact.class);
        boolean conforms =
                pact != null
                        && RequestResponsePact.class.isAssignableFrom(m.getReturnType())
                        && m.getParameterTypes().length == 1
                        && m.getParameterTypes()[0].isAssignableFrom(PactDslWithProvider.class);

        if (!conforms && pact != null) {
            throw new UnsupportedOperationException("Method " + m.getName() +
                    " does not conform required method signature 'public RequestResponsePact xxx(PactDslWithProvider builder)'");
        }
        return conforms;
    }

}
