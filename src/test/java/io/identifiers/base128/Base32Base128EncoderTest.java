package io.identifiers.base128;

import org.junit.jupiter.api.Test;

import static io.identifiers.base128.Base128Encoder.encode;
import static org.assertj.core.api.Assertions.assertThat;

public class Base32Base128EncoderTest {

    @Test
    void handlesEmptyValues() {
        String actual = encode(new byte[0]);
        assertThat(actual).isEqualTo(Constants.TERMINATOR);
    }

    @Test
    void encodesKnownSingleBytes() {
        String actual = encode(new byte[] {'m'});
        assertThat(actual).isEqualTo("pzþ");

        actual = encode(new byte[] {-1});
        assertThat(actual).isEqualTo("ýzþ");
    }

    @Test
    void encodesShortByteArrayWithNoRemainder() {
        String actual = encode("greener".getBytes());
        assertThat(actual).isEqualTo("mÚÊÔesÈðþ");
    }

    @Test
    void encodesShortByteArrayWithRemainder() {
        String actual = encode("chartreuse".getBytes());
        assertThat(actual).isEqualTo("kØ@KGÏâãtÚêÎþ");

        byte[] bytes = {-20, -43, 54};
        actual = encode(bytes);
        assertThat(actual).isEqualTo("ôoZÞþ");
    }
}
