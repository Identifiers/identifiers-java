package io.identifiers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
        createAndAssertIdentifier(Factory.forFloat, IdentifierType.FLOAT, -0.554f);
        createAndAssertListIdentifier(Factory.forFloat, IdentifierType.FLOAT_LIST, 1.0f, -2.2f, 9986755.456f);
        createAndAssertMapIdentifier(Factory.forFloat, IdentifierType.FLOAT_MAP, Collections.singletonMap(KEY, 1.0f));
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
        Identifier<Float> floatId = Factory.forFloat.create(1.2f);
        ListIdentifier<Long> longListId = Factory.forLong.createList(675754L, -988654L);
        MapIdentifier<Integer> intMapId = Factory.forInteger.createMap(Collections.singletonMap(KEY, 2));
        Map<String, Identifier<?>> idMap = new HashMap<>();
        idMap.put("a", floatId);
        idMap.put("b", longListId);
        idMap.put("c", intMapId);

        createAndAssertMapIdentifier(Factory.forComposite, IdentifierType.COMPOSITE_MAP, idMap);
    }

    private <T> void createAndAssertIdentifier(SingleIdentifierFactory<T> factory, IdentifierType expectedType, T expectedValue) {
        Identifier<T> actualIdentifier = factory.create(expectedValue);

        assertThat(actualIdentifier.type()).isEqualTo(expectedType);
        assertThat(actualIdentifier.value()).isEqualTo(expectedValue);
    }

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