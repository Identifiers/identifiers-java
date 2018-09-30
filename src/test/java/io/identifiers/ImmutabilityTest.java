package io.identifiers;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.util.Maps;

class ImmutabilityTest {

    @Test
    void testStringListImmutability() {
        String expected = "a";
        List<String> values = Arrays.asList(expected, "b");
        ListIdentifier<String> idList = Factory.forString.createList(values);
        values.set(0, "z");
        assertThat(idList.value()).startsWith(expected);
    }

    @Test
    void testStringMapImmutability() {
        String key = "b";
        String expected = "a";
        Map<String, String> map = Maps.newHashMap(key, expected);
        MapIdentifier<String> idList = Factory.forString.createMap(map);
        map.put(key, "z");
        assertThat(idList.value()).containsEntry(key, expected);
    }

    @Test
    void testBooleanListImmutability() {
        boolean expected = true;
        List<Boolean> values = Arrays.asList(expected, true);
        ListIdentifier<Boolean> idList = Factory.forBoolean.createList(values);
        values.set(0, false);
        assertThat(idList.value()).startsWith(expected);
    }

    @Test
    void testBooleanMapImmutability() {
        String key = "b";
        boolean expected = true;
        Map<String, Boolean> map = Maps.newHashMap(key, expected);
        MapIdentifier<Boolean> idList = Factory.forBoolean.createMap(map);
        map.put(key, !expected);
        assertThat(idList.value()).containsEntry(key, expected);
    }

    @Test
    void testIntegerListImmutability() {
        int expected = 0;
        List<Integer> values = Arrays.asList(expected, 44);
        ListIdentifier<Integer> idList = Factory.forInteger.createList(values);
        values.set(0, -1);
        assertThat(idList.value()).startsWith(expected);
    }


    @Test
    void testIntegeMapImmutability() {
        String key = "b";
        int expected = 0;
        Map<String, Integer> map = Maps.newHashMap(key, expected);
        MapIdentifier<Integer> idList = Factory.forInteger.createMap(map);
        map.put(key, 1);
        assertThat(idList.value()).containsEntry(key, expected);
    }

    @Test
    void testFloatListImmutability() {
        double expected = 1.1;
        List<Double> values = Arrays.asList(expected, 13.244);
        ListIdentifier<Double> idList = Factory.forFloat.createList(values);
        values.set(0, -100.1);
        assertThat(idList.value()).startsWith(expected);
    }

    @Test
    void testFloatMapImmutability() {
        String key = "b";
        double expected = 1.3;
        Map<String, Double> map = Maps.newHashMap(key, expected);
        MapIdentifier<Double> idList = Factory.forFloat.createMap(map);
        map.put(key, expected + 1.2);
        assertThat(idList.value()).containsEntry(key, expected);
    }

    @Test
    void testLongListImmutability() {
        long expected = 1;
        List<Long> values = Arrays.asList(expected, 6684L, -1002944L);
        ListIdentifier<Long> idList = Factory.forLong.createList(values);
        values.set(0, -1L);
        assertThat(idList.value()).startsWith(expected);
    }

    @Test
    void testLongMapImmutability() {
        String key = "b";
        long expected = 1;
        Map<String, Long> map = Maps.newHashMap(key, expected);
        MapIdentifier<Long> idList = Factory.forLong.createMap(map);
        map.put(key, expected - 1);
        assertThat(idList.value()).containsEntry(key, expected);
    }

    @Test
    void testForBytesImmutability() {
        byte[] bytes = { 0, 26, -39 };
        Identifier<byte[]> id = Factory.forBytes.create(bytes);
        bytes[0] = 1;
        assertThat(id.value()[0]).isEqualTo((byte) 0);
    }

    @Test
    void testForBytesListImmutability() {
        byte[] bytes1 = { 0, 26, -39 };
        byte[] bytes2 = { 54, -127, -81 };
        ListIdentifier<byte[]> idList = Factory.forBytes.createList(bytes1, bytes2);
        bytes1[0] = 1;
        assertThat(idList.value().get(0)[0]).isEqualTo((byte) 0);
    }

    @Test
    void testForBytesMapImmutability() {
        String key = "a";
        byte[] bytes = { 0, 26, -39 };
        Map<String, byte[]> map = Maps.newHashMap(key, bytes);
        MapIdentifier<byte[]> idMap = Factory.forBytes.createMap(map);
        bytes[0] = 1;
        assertThat(idMap.value().get(key)[0]).isEqualTo((byte) 0);
    }

    @Test
    void testForCompositeListImmutability() {
        byte[] bytes = { 0, 26, -39 };
        Identifier<byte[]> bytesId = Factory.forBytes.create(bytes);
        ListIdentifier<Identifier<?>> listId = Factory.forComposite.createList(bytesId);
        Identifier<String> stringId = Factory.forString.create("ice");
        assertThatThrownBy(() -> listId.value().set(0, stringId)).isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void testForCompositeMapImmutability() {
        Identifier<Boolean> booleanId = Factory.forBoolean.create(true);
        MapIdentifier<Identifier<?>> mapId = Factory.forComposite.createMap(Collections.singletonMap("k", booleanId));
        Identifier<Integer> intId = Factory.forInteger.create(44);
        assertThatThrownBy(() -> mapId.value().put("k", intId)).isInstanceOf(UnsupportedOperationException.class);
    }
}