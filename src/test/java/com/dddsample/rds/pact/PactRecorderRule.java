package com.dddsample.rds.pact;

import au.com.dius.pact.consumer.*;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.Interaction;
import au.com.dius.pact.model.PactSpecVersion;
import au.com.dius.pact.model.RequestResponseInteraction;
import au.com.dius.pact.model.RequestResponsePact;
import org.apache.commons.lang3.StringUtils;
import org.junit.rules.ExternalResource;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by azhu on 27/06/2017.
 */
public class PactRecorderRule extends ExternalResource {
    protected final String provider;
    protected final Object target;
    private Map<String, RequestResponsePact> pacts;
    private Map<String, RequestResponseInteraction> interactionMap = new HashMap<>();

    public PactRecorderRule(String provider, Object target) {
        this.target = target;
        this.provider = provider;
    }


    public Statement apply(final Statement base, final Description description) {
        return new Statement() {
            public void evaluate() throws Throwable {
                PactVerifications pactVerifications = (PactVerifications) description.getAnnotation(PactVerifications.class);
                if (pactVerifications != null) {
                    evaluatePactVerifications(pactVerifications, base);
                } else {
                    PactVerification pactDef = (PactVerification) description.getAnnotation(PactVerification.class);
                    if (pactDef == null) {
                        base.evaluate();
                    } else {
                        Map<String, RequestResponsePact> pacts = getPacts(pactDef.fragment());
                        Optional pact;
                        if (pactDef.value().length == 1 && StringUtils.isEmpty(pactDef.value()[0])) {
                            pact = pacts.values().stream().findFirst();
                        } else {
                            Stream var10000 = Arrays.stream(pactDef.value());
                            pacts.getClass();
                            pact = var10000.map(pacts::get).filter(Objects::nonNull).findFirst();
                        }

                        if (!pact.isPresent()) {
                            base.evaluate();
                        } else {
                            PactVerificationResult result = runPactTest(base, (RequestResponsePact) pact.get());
                            validateResult(result, pactDef);
                        }
                    }
                }
            }
        };
    }

    private void evaluatePactVerifications(PactVerifications pactVerifications, Statement base) throws Throwable {
        Optional<PactVerification> possiblePactVerification = this.findPactVerification(pactVerifications);
        if (!possiblePactVerification.isPresent()) {
            base.evaluate();
        } else {
            PactVerification pactVerification = (PactVerification) possiblePactVerification.get();
            Optional<Method> possiblePactMethod = this.findPactMethod(pactVerification);
            if (!possiblePactMethod.isPresent()) {
                throw new UnsupportedOperationException("Could not find method with @Pact for the provider " + this.provider);
            } else {
                Method method = (Method) possiblePactMethod.get();
                Pact pactAnnotation = (Pact) method.getAnnotation(Pact.class);
                PactDslWithProvider dslBuilder = ConsumerPactBuilder.consumer(pactAnnotation.consumer()).hasPactWith(this.provider);

                RequestResponsePact pact;
                try {
                    pact = (RequestResponsePact) method.invoke(this.target, new Object[]{dslBuilder});
                    interactionMap.put(method.getName(), pact.getInteractions().get(0));
                } catch (Exception var11) {
                    throw new RuntimeException("Failed to invoke pact method", var11);
                }

                PactVerificationResult result = this.runPactTest(base, pact);
                this.validateResult(result, pactVerification);
            }
        }
    }

    private Optional<PactVerification> findPactVerification(PactVerifications pactVerifications) {
        PactVerification[] pactVerificationValues = pactVerifications.value();
        return Arrays.stream(pactVerificationValues).filter((p) -> {
            String[] providers = p.value();
            if (providers.length != 1) {
                throw new IllegalArgumentException("Each @PactVerification must specify one and only provider when using @PactVerifications");
            } else {
                String provider = providers[0];
                return provider.equals(this.provider);
            }
        }).findFirst();
    }

    private Optional<Method> findPactMethod(PactVerification pactVerification) {
        String pactFragment = pactVerification.fragment();
        Method[] var3 = this.target.getClass().getMethods();
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Method method = var3[var5];
            Pact pact = (Pact) method.getAnnotation(Pact.class);
            if (pact != null && pact.provider().equals(this.provider) && (pactFragment.isEmpty() || pactFragment.equals(method.getName()))) {
                this.validatePactSignature(method);
                return Optional.of(method);
            }
        }

        return Optional.empty();
    }

    private void validatePactSignature(Method method) {
        boolean hasValidPactSignature = RequestResponsePact.class.isAssignableFrom(method.getReturnType()) && method.getParameterTypes().length == 1 && method.getParameterTypes()[0].isAssignableFrom(PactDslWithProvider.class);
        if (!hasValidPactSignature) {
            throw new UnsupportedOperationException("Method " + method.getName() + " does not conform required method signature 'public RequestResponsePact xxx(PactDslWithProvider builder)'");
        }
    }

    private PactVerificationResult runPactTest(Statement base, RequestResponsePact pact) {
        try {
            base.evaluate();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
        pact.write(getPactDirectory(), PactSpecVersion.V2);
        return PactVerificationResult.Ok.INSTANCE;
    }

    private String getPactDirectory() {
        return System.getProperty("pact.rootDir", "target/pacts");
    }

    protected void validateResult(PactVerificationResult result, PactVerification pactVerification) throws Throwable {
        if (!result.equals(PactVerificationResult.Ok.INSTANCE)) {
            if (result instanceof PactVerificationResult.Error) {
                PactVerificationResult.Error error = (PactVerificationResult.Error) result;
                if (error.getMockServerState() != PactVerificationResult.Ok.INSTANCE) {
                    throw new AssertionError("Pact Test function failed with an exception, possibly due to " + error.getMockServerState(), ((PactVerificationResult.Error) result).getError());
                } else {
                    throw new AssertionError("Pact Test function failed with an exception: " + error.getError().getMessage(), error.getError());
                }
            } else {
                throw new PactMismatchesException(result);
            }
        }
    }

    private Map<String, RequestResponsePact> getPacts(String fragment) {
        if (this.pacts == null) {
            this.pacts = new HashMap();
            Method[] var2 = this.target.getClass().getMethods();
            int var3 = var2.length;

            for (int var4 = 0; var4 < var3; ++var4) {
                Method m = var2[var4];
                if (this.conformsToSignature(m) && this.methodMatchesFragment(m, fragment)) {
                    Pact pact = (Pact) m.getAnnotation(Pact.class);
                    if (StringUtils.isEmpty(pact.provider()) || this.provider.equals(pact.provider())) {
                        PactDslWithProvider dslBuilder = ConsumerPactBuilder.consumer(pact.consumer()).hasPactWith(this.provider);

                        try {
                            RequestResponsePact invokedPact = (RequestResponsePact) m.invoke(this.target, new Object[]{dslBuilder});
                            interactionMap.put(fragment, invokedPact.getInteractions().get(0));
                            this.pacts.put(this.provider, invokedPact);
                        } catch (Exception var9) {
                            throw new RuntimeException("Failed to invoke pact method", var9);
                        }
                    }
                }
            }
        }

        return this.pacts;
    }

    private boolean methodMatchesFragment(Method m, String fragment) {
        return StringUtils.isEmpty(fragment) || m.getName().equals(fragment);
    }

    private boolean conformsToSignature(Method m) {
        Pact pact = (Pact) m.getAnnotation(Pact.class);
        boolean conforms = pact != null && RequestResponsePact.class.isAssignableFrom(m.getReturnType()) && m.getParameterTypes().length == 1 && m.getParameterTypes()[0].isAssignableFrom(PactDslWithProvider.class);
        if (!conforms && pact != null) {
            throw new UnsupportedOperationException("Method " + m.getName() + " does not conform required method signature 'public RequestResponsePact xxx(PactDslWithProvider builder)'");
        } else {
            return conforms;
        }
    }

    public RequestResponseInteraction getPactInteraction(String fragment) {
        return interactionMap.get(fragment);
    }
}
