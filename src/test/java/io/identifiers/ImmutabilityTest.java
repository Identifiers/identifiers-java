package io.identifiers;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.util.Maps;

class ImmutabilityTest {

    private static final String KEY = "a-key";

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
        String expected = "a";
        Map<String, String> map = Maps.newHashMap(KEY, expected);
        MapIdentifier<String> idMap = Factory.forString.createMap(map);
        map.put(KEY, "z");
        assertThat(idMap.value()).containsEntry(KEY, expected);
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
        boolean expected = true;
        Map<String, Boolean> map = Maps.newHashMap(KEY, expected);
        MapIdentifier<Boolean> idMap = Factory.forBoolean.createMap(map);
        map.put(KEY, !expected);
        assertThat(idMap.value()).containsEntry(KEY, expected);
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
    void testIntegerMapImmutability() {
        int expected = 0;
        Map<String, Integer> map = Maps.newHashMap(KEY, expected);
        MapIdentifier<Integer> idMap = Factory.forInteger.createMap(map);
        map.put(KEY, 1);
        assertThat(idMap.value()).containsEntry(KEY, expected);
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
        double expected = 1.3;
        Map<String, Double> map = Maps.newHashMap(KEY, expected);
        MapIdentifier<Double> idMap = Factory.forFloat.createMap(map);
        map.put(KEY, expected + 1.2);
        assertThat(idMap.value()).containsEntry(KEY, expected);
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
        long expected = 1;
        Map<String, Long> map = Maps.newHashMap(KEY, expected);
        MapIdentifier<Long> idMap = Factory.forLong.createMap(map);
        map.put(KEY, expected - 1);
        assertThat(idMap.value()).containsEntry(KEY, expected);
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
        byte[] bytes = { 0, 26, -39 };
        Map<String, byte[]> map = Maps.newHashMap(KEY, bytes);
        MapIdentifier<byte[]> idMap = Factory.forBytes.createMap(map);
        bytes[0] = 1;
        assertThat(idMap.value().get(KEY)[0]).isEqualTo((byte) 0);
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
        MapIdentifier<Identifier<?>> mapId = Factory.forComposite.createMap(Maps.newHashMap(KEY, booleanId));
        Identifier<Integer> intId = Factory.forInteger.create(44);
        assertThatThrownBy(() -> mapId.value().put(KEY, intId)).isInstanceOf(UnsupportedOperationException.class);
    }


    @Test
    void testUuidListImmutability() {
        UUID expected = UUID.fromString("7af7d312-b148-42b4-a20c-0f0e38ed5f0b");
        List<UUID> values = Arrays.asList(expected, UUID.randomUUID(), UUID.randomUUID());
        ListIdentifier<UUID> idList = Factory.forUuid.createList(values);
        values.set(0, UUID.randomUUID());
        assertThat(idList.value()).startsWith(expected);
    }

    @Test
    void testUuidMapImmutability() {
        UUID expected = UUID.fromString("ca059f89-8fbd-446a-bbaa-be1fdfc5ed42");
        Map<String, UUID> map = Maps.newHashMap(KEY, expected);
        MapIdentifier<UUID> idMap = Factory.forUuid.createMap(map);
        map.put(KEY, UUID.randomUUID());
        assertThat(idMap.value()).containsEntry(KEY, expected);
    }

    @Test
    void testDatetimeListImmutability() {
        Instant expected = Instant.ofEpochSecond(0);
        List<Instant> values = Arrays.asList(expected, Instant.now());
        ListIdentifier<Instant> idList = Factory.forDatetime.createList(values);
        values.set(0, Instant.ofEpochSecond(1));
        assertThat(idList.value()).startsWith(expected);
    }

    @Test
    void testDatetimeMapImmutability() {
        Instant expected = Instant.ofEpochSecond(0);
        Map<String, Instant> map = Maps.newHashMap(KEY, expected);
        MapIdentifier<Instant> idMap = Factory.forDatetime.createMap(map);
        map.put(KEY, Instant.now());
        assertThat(idMap.value()).containsEntry(KEY, expected);
    }
}