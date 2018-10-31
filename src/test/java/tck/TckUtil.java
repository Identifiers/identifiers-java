package tck;

import static org.assertj.core.api.Assertions.assertThat;

import io.identifiers.Identifier;

import com.eclipsesource.json.JsonObject;

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
}
