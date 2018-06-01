package io.identifiers.base32;

import org.junit.jupiter.api.Test;

import static io.identifiers.base32.Decoder.decode;
import static org.assertj.core.api.Assertions.assertThat;

class DecoderTest {

    @Test
    void handlesEmptyValues() {
        byte[] actual = decode(Constants.PREFIX);
        assertThat(actual).isEmpty();
    }

    @Test
    void convertsSingleByteValues() {
        byte[] actual = decode("_dm=");
        assertThat(actual).containsExactly('m');

        actual = decode("_041");
        assertThat(actual).containsExactly(1);
    }

    @Test
    void convertsShortByteArrays() {
        byte[] actual = decode("_cxs6asbeb");
        assertThat(actual).containsExactly("green".getBytes());
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