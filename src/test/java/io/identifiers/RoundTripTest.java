package io.identifiers;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class RoundTripTest {

    void roundTrip(Identifier id) {
        String encoded = id.toDataString();
        Identifier decoded = Factory.decodeFromString(encoded);
        assertThat(decoded).isEqualTo(id);

        encoded = id.toHumanString();
        decoded = Factory.decodeFromString(encoded);
        assertThat(decoded).isEqualTo(id);
    }

    @Test
    void testString() {
        roundTrip(Factory.forString.create(""));
        roundTrip(Factory.forString.create("Boaty McBoatFace"));
    }

    @Test
    void testBoolean() {
        roundTrip(Factory.forBoolean.create(true));
        roundTrip(Factory.forBoolean.create(Boolean.FALSE));
    }

    @Test
    void testInteger() {
        roundTrip(Factory.forInteger.create(0));
        roundTrip(Factory.forInteger.create(-99474));
    }

    @Test
    void testFloat() {
        roundTrip(Factory.forFloat.create(0.0095f));
        roundTrip(Factory.forFloat.create(new Float(-100.2)));
    }

    @Test
    void testLong() {
        roundTrip(Factory.forLong.create(Long.MAX_VALUE));
        roundTrip(Factory.forLong.create(Long.MIN_VALUE));
        roundTrip(Factory.forLong.create(new Long(100)));
    }

    @Test
    void testBytes() {
        roundTrip(Factory.forBytes.create(new byte[] {}));
//        roundTrip(Factory.forBytes.create(new byte[] {-127, 0, 127}));
    }
}
