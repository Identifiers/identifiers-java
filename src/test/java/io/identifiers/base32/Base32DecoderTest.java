package io.identifiers.base32;

import org.junit.jupiter.api.Test;

import static io.identifiers.base32.Base32Decoder.decode;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class Base32DecoderTest {

    @Test
    void handlesEmptyValue() {
        byte[] actual = decode("");
        assertThat(actual).isEmpty();
    }

    @Test
    void convertsSingleByteValues() {
        // m
        byte[] actual = decode("dm=");
        assertThat(actual).containsExactly(109);

        // 'ÿ'
        actual = decode("zw~");
        assertThat(actual).containsExactly(-1);
    }

    @Test
    void convertsShortByteArrays() {
        byte[] actual = decode("cxs6asbeb");
        assertThat(actual).containsExactly("green".getBytes());

        actual = decode("xkakcp");
        assertThat(actual).containsExactly(-20, -43, 54);
    }

    @Test
    void understandsAliasCharacters() {
        String testEnc = "00011111abcdefghjkmnpqrstvwxyzabcdefghjkmnpqrstvwxyzh";
        String aliasedEnc = "0Oo1iIlLabcdefghjkmnpqrstvwxyzABCDEFGHJKMNPQRSTVWXYZH";
        byte[] testDec = decode(testEnc);
        byte[] aliasedDec = decode(aliasedEnc);
        assertThat(testDec).containsExactly(aliasedDec);
    }

    @Test
    void throwsOnUnsupportedChars() {
        assertThatThrownBy(() -> decode("cxu6asbeb"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void throwsOnIncorrectChecksums() {
        assertThatThrownBy(() -> decode("cxs6asbe="))
            .isInstanceOf(IllegalArgumentException.class);
    }
}