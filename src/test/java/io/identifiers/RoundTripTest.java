package io.identifiers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class RoundTripTest {

    private void roundTrip(Identifier id) {
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
        roundTrip(Factory.forString.createList("Poseidon", "Boaty McBoatFace"));
    }

    @Test
    void testBoolean() {
        roundTrip(Factory.forBoolean.create(true));
        roundTrip(Factory.forBoolean.create(Boolean.FALSE));
        roundTrip(Factory.forBoolean.createList(Arrays.asList(true, false, true, true)));
    }

    @Test
    void testInteger() {
        roundTrip(Factory.forInteger.create(0));
        roundTrip(Factory.forInteger.create(-99474));
        roundTrip(Factory.forInteger.createList(Arrays.asList(200, 6, 41, -335434).iterator()));
    }

    @Test
    void testFloat() {
        roundTrip(Factory.forFloat.create(0.0095f));
        roundTrip(Factory.forFloat.create((float) -100.2));
        roundTrip(Stream.of(0.1f, 22.9974f, -55.17f, 0.0f)
            .collect(Factory.forFloat.toListIdentifier()));
    }

    @Test
    void testLong() {
        roundTrip(Factory.forLong.create(Long.MAX_VALUE));
        roundTrip(Factory.forLong.create(Long.MIN_VALUE));
        roundTrip(Factory.forLong.create(100L));
        roundTrip(Factory.forLong.createList(1001L, 222L, -333L, 400004499384L));
    }

    @Test
    void testBytes() {
        roundTrip(Factory.forBytes.create(new byte[] {}));
        roundTrip(Factory.forBytes.create(new byte[] {-127, 0, 127}));
        roundTrip(Factory.forBytes.createList(new byte[] {-1, 22, 120, 19}, new byte[] { -127, 0, 127 }));
    }
}
