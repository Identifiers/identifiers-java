package tck;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.identifiers.Factory;
import io.identifiers.Identifier;
import io.identifiers.IdentifierType;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;


class CompositesTest {

    private static final String COMPOSITES_DIR = "target/tck/composites/";

    private Map<String, Function<? super JsonValue, ?>> valueTransformers = new HashMap<>();
    {
        valueTransformers.put("string", JsonValue::asString);
        valueTransformers.put("boolean", JsonValue::asBoolean);
        valueTransformers.put("integer", JsonValue::asInt);
        valueTransformers.put("float", JsonValue::asDouble);
        valueTransformers.put("long", jv -> {
            String str = jv.asString();
            return Long.parseLong(str);
        });

        // need to provide semantic types too
    }

    private Function<? super JsonValue, ?> valueTransformer = jsonValue -> {
        JsonObject jsonObject = jsonValue.asObject();
        String type = jsonObject.get("type").asString();
        Function<? super JsonValue, ?> transformer = valueTransformers.get(type);
        assertThat(transformer).isNotNull(); // String.format("no transformer for type %s", type)
        return transformer.apply(jsonObject.get("value"));
    };

    @Test @Disabled("need to implement semantic IDs first")
    void testList() throws IOException {
        JsonArray tests = getTck("list");
        testTck(tests);
    }

    void testTck(JsonArray tests) {
        tests.forEach(test -> {
            JsonObject testObject = test.asObject();
            roundTripTest(testObject, true);
            roundTripTest(testObject, false);
        });
    }

    @SuppressWarnings("unchecked")
    void roundTripTest(JsonObject test, boolean isHuman) {
        String encoded = test.get(isHuman ? "mixedHuman" : "data").asString();
        Identifier<?> id = Factory.decodeFromString(encoded);
        IdentifierType idType = id.type();

        assertThat(test.get("type").asString()).isEqualTo(idType.toString());

        Object actual = id.value();
        JsonValue value = test.get("value");

        if (IdentifierType.LIST_TYPE == (idType.code() & IdentifierType.LIST_TYPE)) {
            List<Object> expectedList = StreamSupport.stream(value.asArray().spliterator(), false)
                .map(valueTransformer)
                .collect(Collectors.toList());

            assertThat(expectedList).hasSameElementsAs((Iterable<Object>) actual);
        } else if (IdentifierType.MAP_TYPE == (idType.code() & IdentifierType.MAP_TYPE)) {
            Map<String, Object> expectedMap = StreamSupport.stream(value.asObject().spliterator(), false)
                .collect(Collectors.toMap(
                    JsonObject.Member::getName,
                    t -> valueTransformer.apply(t.getValue())));

            assertThat(actual).isEqualTo(expectedMap);
        } else {
            throw new IllegalArgumentException(String.format("Cannot process type %s", idType));
        }

        if (isHuman) {
            String toString = id.toHumanString();
            assertThat(toString).isEqualTo(test.get("human").asString());
        } else {
            String toString = id.toDataString();
            assertThat(toString).isEqualTo(encoded);
        }
    }


    private JsonArray getTck(String testName) throws IOException {
        JsonArray tests;
        try (Reader reader = new FileReader(String.format("%s%s.json", COMPOSITES_DIR, testName))) {
            JsonValue tck = Json.parse(reader);
            tests = tck.asArray();
        }
        return tests;
    }
}
