package io.identifiers.base128;

import org.junit.jupiter.api.Test;

import static io.identifiers.base128.Base128Encoder.encode;
import static org.assertj.core.api.Assertions.assertThat;

public class Base128EncoderTest {

    @Test
    void handlesEmptyValues() {
        String actual = encode(new byte[0]);
        assertThat(actual).isEqualTo("");
    }

    @Test
    void encodesKnownSingleBytes() {
        String actual = encode(new byte[] {'m'});
        assertThat(actual).isEqualTo("pz");

        actual = encode(new byte[] {-1});
        assertThat(actual).isEqualTo("ýz");
    }

    @Test
    void encodesShortByteArrayWithNoRemainder() {
        String actual = encode("greener".getBytes());
        assertThat(actual).isEqualTo("mÚÊÔesÈð");
    }

    @Test
    void encodesShortByteArrayWithRemainder() {
        String actual = encode("chartreuse".getBytes());
        assertThat(actual).isEqualTo("kØ@KGÏâãtÚêÎ");

        byte[] bytes = {-20, -43, 54};
        actual = encode(bytes);
        assertThat(actual).isEqualTo("ôoZÞ");
    }
}
