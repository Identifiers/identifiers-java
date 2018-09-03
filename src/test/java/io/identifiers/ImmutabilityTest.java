package io.identifiers;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ImmutabilityTest {

    @Test
    void testStringListImmutability() {
        final String expected = "a";
        List<String> values = Arrays.asList(expected, "b");
        ListIdentifier<String> idList = Factory.forString.createList(values);
        values.set(0, "z");
        assertThat(idList.value()).startsWith(expected);
    }

    @Test
    void testBooleanListImmutability() {
        final boolean expected = true;
        List<Boolean> values = Arrays.asList(expected, true);
        ListIdentifier<Boolean> idList = Factory.forBoolean.createList(values);
        values.set(0, false);
        assertThat(idList.value()).startsWith(expected);
    }

    @Test
    void testIntegerListImmutability() {
        final int expected = 0;
        List<Integer> values = Arrays.asList(expected, 44);
        ListIdentifier<Integer> idList = Factory.forInteger.createList(values);
        values.set(0, -1);
        assertThat(idList.value()).startsWith(expected);
    }

    @Test
    void testFloatListImmutability() {
        final float expected = 1.1f;
        List<Float> values = Arrays.asList(expected, 13.244f);
        ListIdentifier<Float> idList = Factory.forFloat.createList(values);
        values.set(0, -100f);
        assertThat(idList.value()).startsWith(expected);
    }

    @Test
    void testLongListImmutability() {
        final long expected = 1;
        List<Long> values = Arrays.asList(expected, 6684L, -1002944L);
        ListIdentifier<Long> idList = Factory.forLong.createList(values);
        values.set(0, -1L);
        assertThat(idList.value()).startsWith(expected);
    }

    @Test
    void testForBytesImmutability() {
        byte[] bytes = { 0, 26, -39 };
        Identifier<byte[]> idList = Factory.forBytes.create(bytes);
        bytes[0] = 1;
        assertThat(idList.value()[0]).isEqualTo((byte) 0);
    }

    @Test
    void testForBytesListImmutability() {
        byte[] bytes1 = { 0, 26, -39 };
        byte[] bytes2 = { 54, -127, -81 };
        ListIdentifier<byte[]> idList = Factory.forBytes.createList(bytes1, bytes2);
        bytes1[0] = 1;
        assertThat(idList.value().get(0)[0]).isEqualTo((byte) 0);
    }
}