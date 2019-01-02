package io.identifiers.base128;

import org.junit.jupiter.api.Test;

import static io.identifiers.base128.Base128Decoder.decode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Base128DecoderTest {

    @Test
    void handlesEmptyValue() {
        byte[] actual = decode("");
        assertThat(actual).isEmpty();
    }

    @Test
    void convertsSingleByteValues() {
        // 'm'
        byte[] actual = decode("pz");
        assertThat(actual).containsExactly(109);

        // 'ÿ'
        actual = decode("ýz");
        assertThat(actual).containsExactly(-1);
    }

    @Test
    void convertsShortByteArrays() {
        byte[] actual = decode("mÚÊÔesÈð");
        assertThat(actual).containsExactly("greener".getBytes());
    }

    @Test
    void throwsOnUnsupportedChars() {
        assertThatThrownBy(() -> decode("mÚÊÔ+sÈð"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}