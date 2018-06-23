package io.identifiers.base32;

import org.junit.jupiter.api.Test;

import static io.identifiers.base32.Encoder.encode;
import static org.assertj.core.api.Assertions.assertThat;

class EncoderTest {

    @Test
    void handlesEmptyValues() {
        String actual = encode(new byte[0]);
        assertThat(actual).isEqualTo(Constants.PREFIX);
    }

    @Test
    void encodesKnownSingleBytes() {
        String actual = encode(new byte[] {'m'});
        assertThat(actual).isEqualTo("_dm=");

        actual = encode(new byte[] {-1});
        assertThat(actual).isEqualTo("_zw~");
    }

    @Test
    void encodesShortByteArrayWithNoRemainder() {
        String actual = encode("green".getBytes());
        assertThat(actual).isEqualTo("_cxs6asbeb");

        actual = encode("yellow".getBytes());
        assertThat(actual).isEqualTo("_f5jprv3few2");

        byte[] bytes = {-20, -43, 54};
        actual = Encoder.encode(bytes);
        assertThat(actual).isEqualTo("_xkakcp");
    }
}
