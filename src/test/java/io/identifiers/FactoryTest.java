package io.identifiers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FactoryTest {

    @Test
    void testForStringFactory() {
        createAndAssertIdentifier(Factory.forString, IdentifierType.STRING, "expected value");
    }

    @Test
    void testForBooleanFactory() {
        createAndAssertIdentifier(Factory.forBoolean, IdentifierType.BOOLEAN, true);
    }

    @Test
    void testForIntegerFactory() {
        createAndAssertIdentifier(Factory.forInteger, IdentifierType.INTEGER, 87);
    }

    @Test
    void testForFloatFactory() {
        createAndAssertIdentifier(Factory.forFloat, IdentifierType.FLOAT, -0.554f);
    }

    @Test
    void testForLongFactory() {
        createAndAssertIdentifier(Factory.forLong, IdentifierType.LONG, 10024L);
    }

    @Test
    void testForBytesFactory() {
        createAndAssertIdentifier(Factory.forBytes, IdentifierType.BYTES, new byte[] { 0, -22, 123});
    }

    private <T> void createAndAssertIdentifier(SingleIdentifierFactory<T> factory, IdentifierType expectedType, T expectedValue) {
        Identifier<T> actualIdentifier = factory.create(expectedValue);

        assertThat(actualIdentifier.type()).isEqualTo(expectedType);
        assertThat(actualIdentifier.value()).isEqualTo(expectedValue);
    }
}