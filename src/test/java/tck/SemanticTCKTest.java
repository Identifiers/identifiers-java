package tck;

import static tck.TckUtil.roundTripTest;

import java.io.IOException;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

class SemanticTCKTest {

    private static final String SEMANTIC_DIR = "semantic";

    @Test
    void testUuid() throws IOException {
        JsonArray tests = TckUtil.getTck(SEMANTIC_DIR, "uuid");
        testTck(tests, TckUtil.valueTransformerFor("uuid"));
    }

    @Test
    void testDatetime() throws IOException {
        JsonArray tests = TckUtil.getTck(SEMANTIC_DIR, "datetime");
        testTck(tests, TckUtil.valueTransformerFor("datetime"));
    }

    @Test
    void testGeo() throws IOException {
        JsonArray tests = TckUtil.getTck(SEMANTIC_DIR, "geo");
        testTck(tests, TckUtil.valueTransformerFor("geo"));
    }

    void testTck(JsonArray tests, Function<? super JsonValue, ?> valueTransformer) {
        tests.forEach(test -> {
            JsonObject testObject = test.asObject();
            roundTripTest(testObject, valueTransformer, true);
            roundTripTest(testObject, valueTransformer, false);
        });
    }
}
