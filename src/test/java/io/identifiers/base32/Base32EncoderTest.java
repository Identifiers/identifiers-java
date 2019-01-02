package io.identifiers.base32;

import org.junit.jupiter.api.Test;

import static io.identifiers.base32.Base32Encoder.encode;
import static org.assertj.core.api.Assertions.assertThat;

class Base32EncoderTest {

    @Test
    void handlesEmptyValues() {
        String actual = encode(new byte[0]);
        assertThat(actual).isEqualTo("0"); //check digit
    }

    @Test
    void encodesKnownSingleBytes() {
        String actual = encode(new byte[] {'m'});
        assertThat(actual).isEqualTo("dm=");

        actual = encode(new byte[] {-1});
        assertThat(actual).isEqualTo("zw~");
    }

    @Test
    void encodesShortByteArrayWithNoRemainder() {
        String actual = encode("green".getBytes());
        assertThat(actual).isEqualTo("cxs6asbeb");
    }

    @Test
    void encodesShortByteArrayWithRemainder() {
        String actual = encode("yellow".getBytes());
        assertThat(actual).isEqualTo("f5jprv3few2");

        byte[] bytes = {-20, -43, 54};
        actual = encode(bytes);
        assertThat(actual).isEqualTo("xkakcp");
    }
}
