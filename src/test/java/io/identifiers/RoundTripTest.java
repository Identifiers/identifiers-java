package io.identifiers;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class RoundTripTest {

    private static final String KEY = "aKey";

    private static void roundTrip(Identifier<?> id) {
        String encoded = id.toDataString();
        Identifier<?> decoded = Factory.decodeFromString(encoded);
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
        roundTrip(Factory.forString.createMap(Collections.singletonMap(KEY, "apples")));
    }

    @Test
    void testBoolean() {
        roundTrip(Factory.forBoolean.create(true));
        roundTrip(Factory.forBoolean.create(Boolean.FALSE));
        roundTrip(Factory.forBoolean.createList(Arrays.asList(true, false, true, true)));
        roundTrip(Factory.forBoolean.createMap(Collections.singletonMap(KEY, true)));
    }

    @Test
    void testInteger() {
        roundTrip(Factory.forInteger.create(0));
        roundTrip(Factory.forInteger.create(-99474));
        roundTrip(Factory.forInteger.createList(Arrays.asList(200, 6, 41, -335434).iterator()));
        roundTrip(Factory.forInteger.createMap(Collections.singletonMap(KEY, 77).entrySet().iterator()));
    }

    @Test
    void testFloat() {
        roundTrip(Factory.forFloat.create(0.0095));
        roundTrip(Factory.forFloat.create(-100.2));
        roundTrip(Stream.of(0.1, 22.9974, -55.17, 0.0)
            .collect(Factory.forFloat.toListIdentifier()));
        roundTrip(Stream.of(0.0089)
            .collect(Factory.forFloat.toMapIdentifier((v) -> KEY)));
    }

    @Test
    void testLong() {
        roundTrip(Factory.forLong.create(Long.MAX_VALUE));
        roundTrip(Factory.forLong.create(Long.MIN_VALUE));
        roundTrip(Factory.forLong.create(100L));
        roundTrip(Factory.forLong.create(9007199254740991L)); //  JS Safe Max Integer 2 ^ 53 −1
        roundTrip(Factory.forLong.createList(1001L, 222L, -333L, 400004499384L));
        roundTrip(Factory.forLong.createMap(Collections.singletonMap(KEY, 1L)));
    }

    @Test
    void testBytes() {
        roundTrip(Factory.forBytes.create(new byte[] {}));
        roundTrip(Factory.forBytes.create(new byte[] {-127, 0, 127}));
        roundTrip(Factory.forBytes.createList(new byte[] {-1, 22, 120, 19}, new byte[] {-127, 0, 127}));
        roundTrip(Factory.forBytes.createMap(Collections.singletonMap(KEY, new byte[] {-127, 127})));
    }

    @Test
    void testCompositeList() {
        roundTrip(Factory.forComposite.createList(Factory.forBoolean.create(true), Factory.forInteger.create(1)));
        roundTrip(Factory.forComposite.createList(Arrays.asList(Factory.forString.create("one"), Factory.forFloat.create(11.01))));
        List<Identifier<?>> list = Collections.singletonList(Factory.forLong.create(946L));
        roundTrip(Factory.forComposite.createList(list.iterator()));
        roundTrip(Stream.of(Factory.forBoolean.create(false))
            .collect(Factory.forComposite.toListIdentifier()));
    }

    @Test
    void testCompositeMap() {
        Map<String, Identifier<?>> idMap = new HashMap<>();
        idMap.put("boolean", Factory.forBoolean.create(false));
        idMap.put("long", Factory.forLong.createList(55L, 100L));
        roundTrip(Factory.forComposite.createMap(idMap));
        roundTrip(Factory.forComposite.createMap(idMap.entrySet().iterator()));
        roundTrip(idMap.values().stream()
            .collect(Factory.forComposite.toMapIdentifier((id) -> id.type().name())));
    }

    @Test
    void testUuid() {
        roundTrip(Factory.forUuid.create(UUID.fromString("00000000-0000-0000-0000-000000000000")));
        roundTrip(Factory.forUuid.createList(Arrays.asList(
            UUID.fromString("fca45722-ca53-11e8-a8d5-f2801f1b9fd1"),
            UUID.fromString("1682e1c2-ca54-11e8-a8d5-f2801f1b9fd1"))));
        roundTrip(Factory.forUuid.createMap(Collections.singletonMap(KEY, UUID.fromString("8db37c28-975f-4085-8a7c-e98dd0cbfd55"))));
    }

    @Test
    void testDatetime() {
        roundTrip(Factory.forDatetime.create(Instant.ofEpochSecond(0)));
        roundTrip(Factory.forDatetime.createList(Arrays.asList(
            Instant.ofEpochSecond(1000),
            Instant.ofEpochSecond(2000))));
        roundTrip(Factory.forDatetime.createMap(Collections.singletonMap(KEY, Instant.ofEpochSecond(786748494455L))));
    }
}
