package io.identifiers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdentifierTypeTest {

    @Test
    void testEquals() {
        assertThat(IdentifierType.INTEGER).isEqualTo(IdentifierType.INTEGER);
        assertThat(IdentifierType.INTEGER).isNotEqualTo("not an identifier");
    }
}