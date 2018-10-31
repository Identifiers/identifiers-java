package tck;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.Instant;
import java.util.Arrays;
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
import io.identifiers.semantic.Geo;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;


class CompositesTCKTest {

    private static final String COMPOSITES_DIR = "target/tck/composites/";

    @Test
    void testList() throws IOException {
        JsonArray tests = getTck("list");
        testTck(tests);
    }

    @Test
    void testMap() throws IOException {
        JsonArray tests = getTck("map");
        testTck(tests);
    }

    void testTck(Iterable<JsonValue> tests) {
        tests.forEach(test -> {
            JsonObject testObject = test.asObject();
            roundTripTest(testObject, true);
            roundTripTest(testObject, false);
        });
    }


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
        valueTransformers.put("bytes", (jv) ->
            new ByteArrayEquals(
                jv.asArray().values().stream()
                    .map(JsonValue::asInt)
                    .map((b) -> (byte) (b & 0xff))
                    .toArray(Byte[]::new)));

        // semantic types
        valueTransformers.put("uuid", (jv) -> UUID.fromString(jv.asString()));
        valueTransformers.put("datetime", (jv) -> Instant.parse(jv.asString()));
        valueTransformers.put("geo", (jv) -> {
            JsonObject members = jv.asObject();
            return new Geo(members.getDouble("latitude", Double.MAX_VALUE), members.getDouble("longitude", Double.MAX_VALUE));
        });
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
            assertListValue((List<Identifier<?>>) actual, value);
        } else if (actual instanceof Map) {
            assertMapValue((Map<String, Identifier<?>>) actual, value);
        } else {
            throw new IllegalArgumentException(String.format("Cannot process type %s", idType));
        }
        TckUtil.testStringEncoding(test, isHuman, encoded, id);
    }

    private void assertListValue(List<Identifier<?>> actual, JsonValue value) {
        List<Object> expectedList = collectToList(valueTransformer).apply(value);
        List<Object> actualList = actual.stream()
            .map(Identifier::value)
            .map(this::maybeWrapByteArrays)
            .collect(Collectors.toList());
        assertThat(actualList).containsExactlyElementsOf(expectedList);
    }

    private void assertMapValue(Map<String, Identifier<?>> actual, JsonValue value) {
        Map<String, Object> expectedMap = collectToMap(valueTransformer).apply(value);
        Map<String, Object> actualMap = actual.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                entry -> maybeWrapByteArrays(entry.getValue().value())));
        assertThat(actualMap).containsAllEntriesOf(expectedMap); //bug actual map is a map of IDs, not values
    }

    private Object maybeWrapByteArrays(Object value) {
        if (org.assertj.core.util.Arrays.isArray(value)) {
            Class<?> type = value.getClass().getComponentType();
            if (type.equals(byte.class)) {
                byte[] bytes = (byte[]) value;
                return new ByteArrayEquals(bytes);
            }
        }
        if (value instanceof List) {
            return ((List<?>) value).stream()
                .map(this::maybeWrapByteArrays)
                .collect(Collectors.toList());
        }
        if (value instanceof Map) {
            return ((Map<?, ?>) value).entrySet().stream()
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    v -> maybeWrapByteArrays(v.getValue())));
        }

        return value;
    }


    private JsonArray getTck(String testName) throws IOException {
        JsonArray tests;
        try (Reader reader = new FileReader(String.format("%s%s.json", COMPOSITES_DIR, testName))) {
            JsonValue tck = Json.parse(reader);
            tests = tck.asArray();
        }
        return tests;
    }

    static class ByteArrayEquals {

        final Byte[] bytes;

        ByteArrayEquals(byte[] primBytes) {
            bytes = new Byte[primBytes.length];
            Arrays.setAll(bytes, n -> primBytes[n]);
        }

        ByteArrayEquals(Byte[] bytes) {
            this.bytes = bytes;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (org.assertj.core.util.Arrays.isArray(obj)) {
                return Arrays.equals(bytes, org.assertj.core.util.Arrays.asObjectArray(obj));
            }
            if (obj instanceof ByteArrayEquals) {
                return Arrays.equals(bytes, ((ByteArrayEquals) obj).bytes);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(bytes);
        }

        @Override
        public String toString() {
            return Arrays.toString(bytes);
        }
    }
}
