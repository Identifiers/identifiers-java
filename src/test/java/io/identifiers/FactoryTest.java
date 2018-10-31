package io.identifiers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.identifiers.semantic.Geo;

class FactoryTest {

    private static final String KEY = "aKey";

    @Test
    void testForStringFactory() {
        createAndAssertIdentifier(Factory.forString, IdentifierType.STRING, "expected value");
        createAndAssertListIdentifier(Factory.forString, IdentifierType.STRING_LIST, "expected", "value");
        createAndAssertMapIdentifier(Factory.forString, IdentifierType.STRING_MAP, Collections.singletonMap(KEY, "expected"));
    }

    @Test
    void testForBooleanFactory() {
        createAndAssertIdentifier(Factory.forBoolean, IdentifierType.BOOLEAN, true);
        createAndAssertListIdentifier(Factory.forBoolean, IdentifierType.BOOLEAN_LIST, true, false);
        createAndAssertMapIdentifier(Factory.forBoolean, IdentifierType.BOOLEAN_MAP, Collections.singletonMap(KEY, true));
    }

    @Test
    void testForIntegerFactory() {
        createAndAssertIdentifier(Factory.forInteger, IdentifierType.INTEGER, 87);
        createAndAssertListIdentifier(Factory.forInteger, IdentifierType.INTEGER_LIST, 3, 2, 1);
        createAndAssertMapIdentifier(Factory.forInteger, IdentifierType.INTEGER_MAP, Collections.singletonMap(KEY, 1));
    }

    @Test
    void testForFloatFactory() {
        createAndAssertIdentifier(Factory.forFloat, IdentifierType.FLOAT, -0.554);
        createAndAssertListIdentifier(Factory.forFloat, IdentifierType.FLOAT_LIST, 1.0, -2.2, 9986755.456);
        createAndAssertMapIdentifier(Factory.forFloat, IdentifierType.FLOAT_MAP, Collections.singletonMap(KEY, 1.0));
    }

    @Test
    void testForLongFactory() {
        createAndAssertIdentifier(Factory.forLong, IdentifierType.LONG, 10024L);
        createAndAssertListIdentifier(Factory.forLong, IdentifierType.LONG_LIST, 1L, 2L, 3L, 4L);
        createAndAssertMapIdentifier(Factory.forLong, IdentifierType.LONG_MAP, Collections.singletonMap(KEY, 1L));
    }

    @Test
    void testForBytesFactory() {
        createAndAssertIdentifier(Factory.forBytes, IdentifierType.BYTES, new byte[] { 0, -22, 123});
        createAndAssertListIdentifier(Factory.forBytes, IdentifierType.BYTES_LIST, new byte[] { 0, -22, 123 });
        createAndAssertMapIdentifier(Factory.forBytes, IdentifierType.BYTES_MAP, Collections.singletonMap(KEY, new byte[] { 0 }));
    }

    @Test
    void testForCompositeListFactory() {
        Identifier<String> stringId = Factory.forString.create("testing");
        ListIdentifier<Boolean> booleanListId = Factory.forBoolean.createList(true, false);
        MapIdentifier<byte[]> bytesMapId = Factory.forBytes.createMap(Collections.singletonMap(KEY, new byte[] { 1, 2, 127, -90 }));

        createAndAssertListIdentifier(Factory.forComposite, IdentifierType.COMPOSITE_LIST, booleanListId, stringId, bytesMapId);
    }

    @Test
    void testForCompositeMapFactory() {
        Identifier<Double> floatId = Factory.forFloat.create(1.2);
        ListIdentifier<Long> longListId = Factory.forLong.createList(675754L, -988654L);
        MapIdentifier<Integer> intMapId = Factory.forInteger.createMap(Collections.singletonMap(KEY, 2));
        Map<String, Identifier<?>> idMap = new HashMap<>();
        idMap.put("a", floatId);
        idMap.put("b", longListId);
        idMap.put("c", intMapId);

        createAndAssertMapIdentifier(Factory.forComposite, IdentifierType.COMPOSITE_MAP, idMap);
    }

    @Test
    void testForUuidFactory() {
        UUID u1 = UUID.fromString("b796846a-c98c-11e8-a8d5-f2801f1b9fd1");
        UUID u2 = UUID.fromString("369c0ead-31e0-4ec6-b1e3-1e0da47af4ef");

        createAndAssertIdentifier(Factory.forUuid, IdentifierType.UUID, u1);
        createAndAssertListIdentifier(Factory.forUuid, IdentifierType.UUID_LIST, u1, u2);
        createAndAssertMapIdentifier(Factory.forUuid, IdentifierType.UUID_MAP, Collections.singletonMap(KEY, u2));
    }

    @Test
    void testForDatetimeFactory() {
        Instant i1 = Instant.ofEpochMilli(-1000000);
        Instant i2 = Instant.parse("2018-10-07T15:36:49.152Z");

        createAndAssertIdentifier(Factory.forDatetime, IdentifierType.DATETIME, i1);
        createAndAssertListIdentifier(Factory.forDatetime, IdentifierType.DATETIME_LIST, i1, i2);
        createAndAssertMapIdentifier(Factory.forDatetime, IdentifierType.DATETIME_MAP, Collections.singletonMap(KEY, i2));
    }

    @Test
    void testForGeoFactory() {
        Geo g1 = new Geo(-10.0068585, 2.6567749332);
        Geo g2 = new Geo(-90, 180);

        createAndAssertIdentifier(Factory.forGeo, IdentifierType.GEO, g1);
        createAndAssertListIdentifier(Factory.forGeo, IdentifierType.GEO_LIST, g1, g2);
        createAndAssertMapIdentifier(Factory.forGeo, IdentifierType.GEO_MAP, Collections.singletonMap(KEY, g2));
    }


    private <T> void createAndAssertIdentifier(SingleIdentifierFactory<T> factory, IdentifierType expectedType, T expectedValue) {
        Identifier<T> actualIdentifier = factory.create(expectedValue);

        assertThat(actualIdentifier.type()).isEqualTo(expectedType);
        assertThat(actualIdentifier.value()).isEqualTo(expectedValue);
    }

    @SuppressWarnings("unchecked")
    private <T> void createAndAssertListIdentifier(ListIdentifierFactory<T> factory, IdentifierType expectedType, T... expectedValues) {
        ListIdentifier<T> actualIdentifier = factory.createList(expectedValues);

        assertThat(actualIdentifier.type()).isEqualTo(expectedType);
        assertThat(actualIdentifier.value()).containsExactly(expectedValues);
    }

    private <T> void createAndAssertMapIdentifier(MapIdentifierFactory<T> factory, IdentifierType expectedType, Map<String, T> expectedMap) {
        MapIdentifier<T> actualIdentifier = factory.createMap(expectedMap);

        assertThat(actualIdentifier.type()).isEqualTo(expectedType);
        assertThat(actualIdentifier.value()).containsAllEntriesOf(expectedMap);
    }
}