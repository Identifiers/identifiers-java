package io.identifiers;

import io.identifiers.types.ImmutableIdentifier;
import io.identifiers.types.SupplyingValueIdentifier;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FactoryTest {

    @Test
    void testForStringFactory() {
        assertImmutableIdentifier(Factory.forString, IdentifierType.STRING, "expected value");
    }

    @Test
    void testForBooleanFactory() {
        assertImmutableIdentifier(Factory.forBoolean, IdentifierType.BOOLEAN, true);
    }

    @Test
    void testForIntegerFactory() {
        assertImmutableIdentifier(Factory.forInteger, IdentifierType.INTEGER, 87);
    }

    @Test
    void testForFloatFactory() {
        assertImmutableIdentifier(Factory.forFloat, IdentifierType.FLOAT, -0.554f);
    }

    @Test
    void testForLongFactory() {
        assertImmutableIdentifier(Factory.forLong, IdentifierType.LONG, 10024L);
    }

    @Test
    void testForBytesFactory() {
        assertSupplyingValueIdentifier(Factory.forBytes, IdentifierType.BYTES, new byte[] { 0, -22, 123});
    }

    private <T> void assertImmutableIdentifier(IdentifierFactory<T> factory, IdentifierType expectedType, T expectedValue) {
        Identifier<T> actualIdentifier = createAndAssertIdentifier(factory, expectedType, expectedValue);

        assertThat(actualIdentifier)
                .isInstanceOf(ImmutableIdentifier.class)
                .isNotInstanceOf(SupplyingValueIdentifier.class);
    }

    private <T> void assertSupplyingValueIdentifier(IdentifierFactory<T> factory, IdentifierType expectedType, T expectedValue) {
        Identifier<T> actualIdentifier = createAndAssertIdentifier(factory, expectedType, expectedValue);

        assertThat(actualIdentifier)
                .isInstanceOf(SupplyingValueIdentifier.class);
    }

    private <T> Identifier<T> createAndAssertIdentifier(IdentifierFactory<T> factory, IdentifierType expectedType, T expectedValue) {
        Identifier<T> actualIdentifier = factory.create(expectedValue);

        assertThat(actualIdentifier.type()).isEqualTo(expectedType);
        assertThat(actualIdentifier.value()).isEqualTo(expectedValue);

        return actualIdentifier;
    }
}