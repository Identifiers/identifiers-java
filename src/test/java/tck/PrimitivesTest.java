package tck;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import io.identifiers.Factory;
import io.identifiers.Identifier;
import io.identifiers.IdentifierType;
import io.identifiers.TypeCodeModifiers;
import org.junit.jupiter.api.Test;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

class PrimitivesTest {
    private static final String PRIMITIVES_DIR = "target/tck/primitives/";

    @Test
    void testString() throws IOException {
        JsonArray tests = getTck("string");
        Function<Object, JsonValue> valueTransformer = s -> Json.value((String) s);
        testTck(tests, valueTransformer);
    }

    @Test
    void testBoolean() throws IOException {
        JsonArray tests = getTck("boolean");
        Function<Object, JsonValue> valueTransformer = b -> Json.value((Boolean) b);
        testTck(tests, valueTransformer);
    }

    @Test
    void testInteger() throws IOException {
        JsonArray tests = getTck("integer");
        Function<Object, JsonValue> valueTransformer = i -> Json.value((Integer) i);
        testTck(tests, valueTransformer);
    }

    @Test
    void testFloat() throws IOException {
        JsonArray tests = getTck("float");
        Function<Object, JsonValue> valueTransformer = f -> Json.value((Double) f);
        testTck(tests, valueTransformer);
    }

    @Test
    void testLong() throws IOException {
        JsonArray tests = getTck("long");
        Function<Object, JsonValue> valueTransformer = l -> {
            Long theLong = (Long) l;
            return Json.object()
                .set("high", (int) (theLong >> 32))
                .set("low", theLong.intValue());
        };
        testTck(tests, valueTransformer);
    }

    private JsonArray getTck(String testName) throws IOException {
        JsonArray tests;
        try (Reader reader = new FileReader(String.format("%s%s.json", PRIMITIVES_DIR, testName))) {
            JsonValue tck = Json.parse(reader);
            tests = tck.asArray();
        }
        return tests;
    }

    void testTck(JsonArray tests, Function<Object, JsonValue> valueTransformer) {
        tests.forEach(test -> {
            JsonObject testObject = test.asObject();
            roundTripTest(testObject, valueTransformer, true);
            roundTripTest(testObject, valueTransformer, false);
        });
    }

    @SuppressWarnings("unchecked")
    void roundTripTest(JsonObject test, Function<Object, JsonValue> valueTransformer, boolean isHuman) {
        String encoded = test.get(isHuman ? "mixedHuman" : "data").asString();
        Identifier<?> id = Factory.decodeFromString(encoded);
        IdentifierType idType = id.type();

        assertThat(test.get("type").asString()).isEqualTo(idType.toString());

        Object idValue = id.value();
        JsonValue value = test.get("value");

        if (TypeCodeModifiers.LIST_TYPE_CODE == (idType.code() & TypeCodeModifiers.LIST_TYPE_CODE)) {
            List<JsonValue> transformed = ((Collection<?>) idValue).stream()
                .map(valueTransformer)
                .collect(Collectors.toList());

            assertThat(transformed).hasSameElementsAs(value.asArray());
        } else if (TypeCodeModifiers.MAP_TYPE_CODE == (idType.code() & TypeCodeModifiers.MAP_TYPE_CODE)) {
            Map<String, JsonValue> transformed = ((Map<String, ?>) idValue).entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    entry -> valueTransformer.apply(entry.getValue())));

            Map<String, JsonValue> valueMap = StreamSupport.stream(value.asObject().spliterator(), false)
                .collect(Collectors.toMap(
                    JsonObject.Member::getName,
                    JsonObject.Member::getValue));

            assertThat(transformed).isEqualTo(valueMap);
        }
        else {
            assertThat(valueTransformer.apply(idValue)).isEqualTo(value);
        }

        if (isHuman) {
            String toString = id.toHumanString();
            assertThat(toString).isEqualTo(test.get("human").asString());
        } else {
            String toString = id.toDataString();
            assertThat(toString).isEqualTo(encoded);
        }
    }
}
