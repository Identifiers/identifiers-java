package tck;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.function.Function;

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
            TckUtil.roundTripTest(testObject, valueTransformer, true);
            TckUtil.roundTripTest(testObject, valueTransformer, false);
        });
    }
}
