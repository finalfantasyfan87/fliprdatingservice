package com.dating.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ZodiacCompatibilityServiceTest {

    private final ZodiacCompatibilityService service = new ZodiacCompatibilityService();

    @ParameterizedTest(name = "{0} and {1} score 90 in both argument orders")
    @CsvSource({"aries,leo", "cancer,pisces", "gemini,aquarius", "taurus,capricorn"})
    @DisplayName("Signs listed in the compatibility table score 90 regardless of order")
    void tablePairsScoreNinetyInEitherOrder(String first, String second) {
        StepVerifier.create(service.getCompatibilityScore(first, second))
                .expectNext(90)
                .verifyComplete();
        StepVerifier.create(service.getCompatibilityScore(second, first))
                .expectNext(90)
                .verifyComplete();
    }

    @Test
    @DisplayName("Two different signs that are not in the table score 50 in either order")
    void unlistedPairScoresFifty() {
        StepVerifier.create(service.getCompatibilityScore("aries", "cancer"))
                .expectNext(50)
                .verifyComplete();
        StepVerifier.create(service.getCompatibilityScore("cancer", "aries"))
                .expectNext(50)
                .verifyComplete();
    }

    @Test
    @DisplayName("A sign paired with itself scores 60")
    void sameSignScoresSixty() {
        StepVerifier.create(service.getCompatibilityScore("leo", "leo"))
                .expectNext(60)
                .verifyComplete();
    }

    @Test
    @DisplayName("Letter case and surrounding spaces are ignored")
    void caseAndWhitespaceAreIgnored() {
        StepVerifier.create(service.getCompatibilityScore("  ARIES ", "Leo"))
                .expectNext(90)
                .verifyComplete();
    }

    @ParameterizedTest(name = "rejects unknown sign \"{0}\"")
    @ValueSource(strings = {"notasign", "", "   "})
    @DisplayName("Unknown or blank signs are rejected with IllegalArgumentException")
    void unknownOrBlankSignIsRejected(String badSign) {
        StepVerifier.create(service.getCompatibilityScore(badSign, "leo"))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @ParameterizedTest(name = "rejects null sign in position {arguments}")
    @NullSource
    @DisplayName("A null sign is rejected with IllegalArgumentException")
    void nullSignIsRejected(String nullSign) {
        StepVerifier.create(service.getCompatibilityScore(nullSign, "leo"))
                .expectError(IllegalArgumentException.class)
                .verify();
    }
}
