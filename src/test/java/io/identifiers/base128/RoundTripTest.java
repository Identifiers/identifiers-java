package io.identifiers.base128;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Random;

import static io.identifiers.base128.Decoder.decode;
import static io.identifiers.base128.Encoder.encode;
import static org.assertj.core.api.Assertions.assertThat;

class RoundTripTest {

    @Test
    void randomBytes() {
        Random random = new Random();
        byte[][] bytes = new byte[500][];
        for (int i = 0; i < 500; i++) {
            bytes[i] = new byte[i + 1];
            random.nextBytes(bytes[i]);
        }

        Arrays.stream(bytes).forEach(this::roundTrip);
    }

    private void roundTrip(byte[] bytes) throws IllegalArgumentException {
        String testEnc = encode(bytes);
        byte[] actual = decode(testEnc);
        assertThat(actual).containsExactly(bytes);
    }
}
