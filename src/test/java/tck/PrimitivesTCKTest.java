package tck;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import io.identifiers.Factory;
import io.identifiers.Identifier;
import io.identifiers.IdentifierType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

class PrimitivesTCKTest {
    private static final String PRIMITIVES_DIR = "primitives";

    @Test
    void testString() throws IOException {
        JsonArray tests = TckUtil.getTck(PRIMITIVES_DIR, "string");
        testTck(tests, TckUtil.valueTransformerFor("string"));
    }

    @Test
    void testBoolean() throws IOException {
        JsonArray tests = TckUtil.getTck(PRIMITIVES_DIR, "boolean");
        testTck(tests, TckUtil.valueTransformerFor("boolean"));
    }

    @Test
    void testInteger() throws IOException {
        JsonArray tests = TckUtil.getTck(PRIMITIVES_DIR, "integer");
        testTck(tests, TckUtil.valueTransformerFor("integer"));
    }

    @Test
    void testFloat() throws IOException {
        JsonArray tests = TckUtil.getTck(PRIMITIVES_DIR, "float");
        testTck(tests, TckUtil.valueTransformerFor("float"));
    }

    @Test
    void testLong() throws IOException {
        JsonArray tests = TckUtil.getTck(PRIMITIVES_DIR, "long");
        testTck(tests, TckUtil.valueTransformerFor("long"));
    }

    @Test
    void testBytes() throws IOException {
        JsonArray tests = TckUtil.getTck(PRIMITIVES_DIR, "bytes");
        testTck(tests, TckUtil.valueTransformerFor("bytes"));
    }

    void testTck(JsonArray tests, Function<? super JsonValue, ?> valueTransformer) {
        tests.forEach(test -> {
            JsonObject testObject = test.asObject();
            roundTripTest(testObject, valueTransformer, true);
            roundTripTest(testObject, valueTransformer, false);
        });
    }

    @SuppressWarnings("unchecked")
    void roundTripTest(JsonObject test, Function<? super JsonValue, ?> valueTransformer, boolean isHuman) {
        String encoded = test.get(isHuman ? "mixedHuman" : "data").asString();
        Identifier<?> id = Factory.decodeFromString(encoded);
        IdentifierType idType = id.type();

        assertThat(test.get("type").asString()).isEqualTo(idType.toString());

        Object actual = TckUtil.maybeWrapByteArrays(id.value());
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
