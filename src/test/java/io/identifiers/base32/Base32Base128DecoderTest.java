package io.identifiers.base32;

import org.junit.jupiter.api.Test;

import static io.identifiers.base32.Base32Decoder.decode;
import static org.assertj.core.api.Assertions.assertThat;

class Base32Base128DecoderTest {

    @Test
    void handlesEmptyValue() {
        byte[] actual = decode(Constants.PREFIX);
        assertThat(actual).isEmpty();
    }

    @Test
    void convertsSingleByteValues() {
        // m
        byte[] actual = decode("_dm=");
        assertThat(actual).containsExactly(109);

        // 'ÿ'
        actual = decode("_zw~");
        assertThat(actual).containsExactly(-1);
    }

    @Test
    void convertsShortByteArrays() {
        byte[] actual = decode("_cxs6asbeb");
        assertThat(actual).containsExactly("green".getBytes());

        actual = decode("_xkakcp");
        assertThat(actual).containsExactly(-20, -43, 54);
    }

    @Test
    void understandsAliasCharacters() {
        String testEnc = "_00011111abcdefghjkmnpqrstvwxyzabcdefghjkmnpqrstvwxyzh";
        String aliasedEnc = "_0Oo1iIlLabcdefghjkmnpqrstvwxyzABCDEFGHJKMNPQRSTVWXYZH";
        byte[] testDec = decode(testEnc);
        byte[] aliasedDec = decode(aliasedEnc);
        assertThat(testDec).containsExactly(aliasedDec);
    }
}