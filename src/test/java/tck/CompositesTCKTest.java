package tck;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import io.identifiers.Factory;
import io.identifiers.Identifier;
import io.identifiers.IdentifierType;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;


class CompositesTCKTest {

    private static final String COMPOSITES_DIR = "composites";

    @Test
    void testList() throws IOException {
        JsonArray tests = TckUtil.getTck(COMPOSITES_DIR, "list");
        testTck(tests);
    }

    @Test
    void testMap() throws IOException {
        JsonArray tests = TckUtil.getTck(COMPOSITES_DIR, "map");
        testTck(tests);
    }

    void testTck(Iterable<JsonValue> tests) {
        tests.forEach(test -> {
            JsonObject testObject = test.asObject();
            roundTripTest(testObject, true);
            roundTripTest(testObject, false);
        });
    }


    @SuppressWarnings("unchecked")
    void roundTripTest(JsonObject test, boolean isHuman) {
        String encoded = test.get(isHuman ? "mixedHuman" : "data").asString();
        System.out.println(encoded);
        Identifier<?> id = Factory.decodeFromString(encoded);
        IdentifierType idType = id.type();

        assertThat(test.get("type").asString()).isEqualTo(idType.toString());

        Object actual = id.value();
        JsonValue value = test.get("value");

        if (actual instanceof List) {
            assertListValue((List<Identifier<?>>) actual, value.asArray());
        } else if (actual instanceof Map) {
            assertMapValue((Map<String, Identifier<?>>) actual, value.asObject());
        } else {
            throw new IllegalArgumentException(String.format("Cannot process type %s", idType));
        }
        TckUtil.testStringEncoding(test, isHuman, encoded, id);
    }

    private void assertListValue(List<Identifier<?>> actual, JsonArray value) {
        List<Object> expectedList = TckUtil.collectToList(TckUtil.valueTransformer).apply(value);
        List<Object> actualList = actual.stream()
            .map(Identifier::value)
            .map(TckUtil::maybeWrapByteArrays)
            .collect(Collectors.toList());
        assertThat(actualList).containsExactlyElementsOf(expectedList);
    }

    private void assertMapValue(Map<String, Identifier<?>> actual, JsonObject value) {
        Map<String, Object> expectedMap = TckUtil.collectToMap(TckUtil.valueTransformer).apply(value);
        Map<String, Object> actualMap = actual.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> TckUtil.maybeWrapByteArrays(entry.getValue().value())));
        assertThat(actualMap).containsAllEntriesOf(expectedMap); //bug actual map is a map of IDs, not values
    }
}
