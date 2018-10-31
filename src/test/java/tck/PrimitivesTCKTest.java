package tck;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import io.identifiers.Factory;
import io.identifiers.Identifier;
import io.identifiers.IdentifierType;
import org.junit.jupiter.api.Test;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

class PrimitivesTCKTest {
    private static final String PRIMITIVES_DIR = "target/tck/primitives/";

    @Test
    void testString() throws IOException {
        JsonArray tests = getTck("string");
        testTck(tests, JsonValue::asString);
    }

    @Test
    void testBoolean() throws IOException {
        JsonArray tests = getTck("boolean");
        testTck(tests, JsonValue::asBoolean);
    }

    @Test
    void testInteger() throws IOException {
        JsonArray tests = getTck("integer");
        testTck(tests, JsonValue::asInt);
    }

    @Test
    void testFloat() throws IOException {
        JsonArray tests = getTck("float");
        testTck(tests, JsonValue::asDouble);
    }

    @Test
    void testLong() throws IOException {
        JsonArray tests = getTck("long");
        Function<JsonValue, Object> valueTransformer = (jv) -> {
            String str = jv.asString();
            return Long.parseLong(str);
        };
        testTck(tests, valueTransformer);
    }

    @Test
    void testBytes() throws IOException {
        JsonArray tests = getTck("bytes");
        Function<JsonValue, Object> valueTransformer = (jv) -> jv.asArray().values().stream()
                .map(JsonValue::asInt)
                .map(b -> (byte) (b & 0xff))
                .toArray(Byte[]::new);
        testTck(tests, valueTransformer);
    }

    private JsonArray getTck(String testName) throws IOException {
        JsonArray tests;
        String filename = String.format("%s%s.json", PRIMITIVES_DIR, testName);
        System.out.printf("loading TCK %s%n", filename);
        try (Reader reader = new FileReader(filename)) {
            JsonValue tck = Json.parse(reader);
            tests = tck.asArray();
        }
        return tests;
    }

    void testTck(JsonArray tests, Function<JsonValue, Object> valueTransformer) {
        tests.forEach(test -> {
            JsonObject testObject = test.asObject();
            roundTripTest(testObject, valueTransformer, true);
            roundTripTest(testObject, valueTransformer, false);
        });
    }

    @SuppressWarnings("unchecked")
    void roundTripTest(JsonObject test, Function<JsonValue, Object> valueTransformer, boolean isHuman) {
        String encoded = test.get(isHuman ? "mixedHuman" : "data").asString();
        Identifier<?> id = Factory.decodeFromString(encoded);
        IdentifierType idType = id.type();

        assertThat(test.get("type").asString()).isEqualTo(idType.toString());

        Object actual = id.value();
        JsonValue value = test.get("value");

        if (actual instanceof List) {
            List<Object> expectedList = value.asArray().values().stream()
                .map(valueTransformer)
                .collect(Collectors.toList());
            assertThat((List<Object>) actual).containsExactlyElementsOf(expectedList);
        } else if (actual instanceof Map) {
            Map<String, Object> expectedMap = StreamSupport.stream(value.asObject().spliterator(), false)
                .collect(Collectors.toMap(
                    JsonObject.Member::getName,
                    t -> valueTransformer.apply(t.getValue())));
            assertThat((Map<String, Object>) actual).containsAllEntriesOf(expectedMap);
        }
        else {
            Object expected = valueTransformer.apply(value);
            assertThat(actual).isEqualTo(expected);
        }

        TckUtil.testStringEncoding(test, isHuman, encoded, id);
    }
}
