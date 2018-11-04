package io.identifiers;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AssertTest {

    @Test
    void testArgumentExists() {
        Assert.argumentExists("hello", "should succeed");
        assertThatThrownBy(() -> Assert.argumentExists(null, "should fail"))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testState() {
        Assert.state(true, "should succeed");
        assertThatThrownBy(() -> Assert.state(false, "should fail"));
    }
}