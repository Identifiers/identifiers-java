package tck;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.Test;

import io.identifiers.Factory;
import io.identifiers.Identifier;
import io.identifiers.IdentifierType;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;


class CompositesTCKTest {

    private static final String COMPOSITES_DIR = "target/tck/composites/";

    private final Map<String, Function<? super JsonValue, ?>> valueTransformers = new HashMap<>();
    {
        valueTransformers.put("string", JsonValue::asString);
        valueTransformers.put("boolean", JsonValue::asBoolean);
        valueTransformers.put("integer", JsonValue::asInt);
        valueTransformers.put("float", JsonValue::asDouble);
        valueTransformers.put("long", (jv) -> {
            String str = jv.asString();
            return Long.parseLong(str);
        });
        valueTransformers.put("bytes", collectToList(JsonValue::asInt));

        // semantic types
        valueTransformers.put("uuid", (jv) -> UUID.fromString(jv.asString()));
        valueTransformers.put("datetime", (jv) -> Instant.parse(jv.asString()));
        valueTransformers.put("geo", JsonValue::asObject); //not right, need to add geo support
    }

    private Function<JsonValue, List<Object>> collectToList(Function<? super JsonValue, ?> itemTxr) {
        return (jv) -> jv.asArray().values().stream()
            .map(itemTxr)
            .collect(Collectors.toList());
    }

    private Function<JsonValue, Map<String, Object>> collectToMap(Function<? super JsonValue, ?> itemTxr) {
        return (jv) -> StreamSupport.stream(jv.asObject().spliterator(), false)
            .collect(Collectors.toMap(
                JsonObject.Member::getName,
                t -> itemTxr.apply(t.getValue())));
    }

    private final Function<? super JsonValue, ?> valueTransformer = jsonValue -> {
        JsonObject jsonObject = jsonValue.asObject();
        String type = jsonObject.get("type").asString();
        Function<? super JsonValue, ?> transformer = valueTransformers.get(type);

        if (transformer == null && type.endsWith("-list")) {
            Function<? super JsonValue, ?> itemTxr = valueTransformers.get(trimEnd(type, "-list"));
            if (itemTxr != null) {
                transformer = collectToList(itemTxr);
            }
        }

        if (transformer == null && type.endsWith("-map")) {
            Function<? super JsonValue, ?> itemTxr = valueTransformers.get(trimEnd(type, "-map"));
            if (itemTxr != null) {
                transformer = collectToMap(itemTxr);
            }
        }

        assertThat(transformer).overridingErrorMessage("no transformer for type %s", type)
            .isNotNull();
        return transformer.apply(jsonObject.get("value"));
    };



    private String trimEnd(String input, String ending) {
        return input.substring(0, input.length() - ending.length());
    }

    @Test
    void testList() throws IOException {
        JsonArray tests = getTck("list");
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

        if (IdentifierType.Modifiers.LIST_TYPE == (idType.code() & IdentifierType.Modifiers.LIST_TYPE)) {
            List<Object> expectedList = collectToList(valueTransformer).apply(value);
            assertThat(expectedList).hasSameElementsAs((Iterable<Object>) actual);
        } else if (IdentifierType.Modifiers.MAP_TYPE == (idType.code() & IdentifierType.Modifiers.MAP_TYPE)) {
            Map<String, Object> expectedMap = collectToMap(valueTransformer).apply(value);
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
