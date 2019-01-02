package io.identifiers.types;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IdentifierDecoderSupportTest {

    @Test
    void decodeIdentifierThrowsOnBadInputs() {
        assertThatThrownBy(() -> IdentifierDecoderSupport.decodeIdentifier(null))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> IdentifierDecoderSupport.decodeIdentifier(""))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> IdentifierDecoderSupport.decodeIdentifier("I'm not an encoded string!"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}