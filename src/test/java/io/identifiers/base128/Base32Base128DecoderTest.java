package io.identifiers.base128;

import org.junit.jupiter.api.Test;

import static io.identifiers.base128.Base128Decoder.decode;
import static org.assertj.core.api.Assertions.assertThat;

class Base32Base128DecoderTest {

    @Test
    void handlesEmptyValue() {
        byte[] actual = decode(Constants.TERMINATOR);
        assertThat(actual).isEmpty();
    }

    @Test
    void convertsSingleByteValues() {
        // 'm'
        byte[] actual = decode("pzþ");
        assertThat(actual).containsExactly(109);

        // 'ÿ'
        actual = decode("ýzþ");
        assertThat(actual).containsExactly(-1);
    }

    @Test
    void convertsShortByteArrays() {
        byte[] actual = decode("mÚÊÔesÈðþ");
        assertThat(actual).containsExactly("greener".getBytes());
    }
}