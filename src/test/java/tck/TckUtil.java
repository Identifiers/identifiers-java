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

import io.identifiers.Factory;
import io.identifiers.Identifier;
import io.identifiers.IdentifierType;
import io.identifiers.semantic.Geo;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

class TckUtil {

    static void testStringEncoding(final JsonObject test, final boolean isHuman, final String encoded, final Identifier<?> id) {
        if (isHuman) {
            String toString = id.toHumanString();
            assertThat(toString).isEqualTo(test.get("human").asString());
        } else {
            String toString = id.toDataString();
            assertThat(toString).isEqualTo(encoded);
        }
    }

    static JsonArray getTck(String dirName, String testName) throws IOException {
        JsonArray tests;
        String filename = String.format("spec/tck/files/%s/%s.json", dirName, testName);
        System.out.printf("loading TCK %s%n", filename);
        try (Reader reader = new FileReader(filename)) {
            JsonValue tck = Json.parse(reader);
            tests = tck.asArray();
        }
        return tests;
    }

    private static final Map<String, Function<? super JsonValue, ?>> valueTransformers = new HashMap<>();

    static {
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

    static Function<? super JsonValue, ?> valueTransformer = (jsonValue) -> {
        JsonObject jsonObject = jsonValue.asObject();
        String type = jsonObject.get("type").asString();
        JsonValue value = jsonObject.get("value");
        Function<? super JsonValue, ?> transformer = valueTransformerFor(type);
        return transformer.apply(value);
    };

    static Function<? super JsonValue, ?> valueTransformerFor(String type) {
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

        return transformer;
    }

    static Function<? super JsonValue, List<Object>> collectToList(Function<? super JsonValue, ?> itemTxr) {
        return (ja) -> ja.asArray().values().stream()
            .map(itemTxr)
            .collect(Collectors.toList());
    }

    static Function<? super JsonValue, Map<String, Object>> collectToMap(Function<? super JsonValue, ?> itemTxr) {
        return (jo) -> StreamSupport.stream(jo.asObject().spliterator(), false)
            .collect(Collectors.toMap(
                JsonObject.Member::getName,
                t -> itemTxr.apply(t.getValue())));
    }

    private static String trimEnd(String input, String ending) {
        return input.substring(0, input.length() - ending.length());
    }

    static Object maybeWrapByteArrays(Object value) {
        if (org.assertj.core.util.Arrays.isArray(value)) {
            Class<?> type = value.getClass().getComponentType();
            if (type.equals(byte.class)) {
                byte[] bytes = (byte[]) value;
                return new ByteArrayEquals(bytes);
            }
        }
        if (value instanceof List) {
            return ((List<?>) value).stream()
                .map(TckUtil::maybeWrapByteArrays)
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

    @SuppressWarnings("unchecked")
    static void roundTripTest(JsonObject test, Function<? super JsonValue, ?> valueTransformer, boolean isHuman) {
        String encoded = test.get(isHuman ? "mixedHuman" : "data").asString();
        Identifier<?> id = Factory.decodeFromString(encoded);
        IdentifierType idType = id.type();

        assertThat(test.get("type").asString()).isEqualTo(idType.toString());

        Object actual = maybeWrapByteArrays(id.value());
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

        testStringEncoding(test, isHuman, encoded, id);
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
