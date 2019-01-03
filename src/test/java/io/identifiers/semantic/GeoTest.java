package io.identifiers.semantic;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class GeoTest {

    private final Geo classUnderTest = new Geo(1, 2);

    @Test
    void testEquals() {
        assertThat(classUnderTest).isEqualTo(classUnderTest);
        assertThat(classUnderTest).isEqualTo(new Geo(1, 2));
        assertThat(classUnderTest).isNotEqualTo(new Geo(2, 2));
        assertThat(classUnderTest).isNotEqualTo("not a Geo");
    }

    @Test
    void testHashCode() {
        assertThat(classUnderTest.hashCode()).isEqualTo(Objects.hash(classUnderTest.getLatitude(), classUnderTest.getLongitude()));
    }

    @Test
    void testToString() {
        assertThat(classUnderTest.toString()).isEqualTo("lat:1.0/long:2.0");
    }
}