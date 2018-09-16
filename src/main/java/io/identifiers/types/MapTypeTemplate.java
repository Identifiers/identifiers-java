package io.identifiers.types;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import io.identifiers.IdentifierType;

final class MapTypeTemplate<T> implements TypeTemplate<SortedMap<String, T>> {

    private final MapIdentifierEncoder<T> mapEncoder;
    private final TypeTemplate<T> valueTypeTemplate;

    MapTypeTemplate(MapIdentifierEncoder<T> mapEncoder, TypeTemplate<T> valueTypeTemplate) {
        this.mapEncoder = mapEncoder;
        this.valueTypeTemplate = valueTypeTemplate;
    }

     SortedMap<String, T> initialValueMap(SortedMap<String, T> map) {
         if (isValueMutable()) {
             map.replaceAll((key, value) -> valueTypeTemplate.value(value));
         }
         return map;
     }

    @Override
    public boolean isValueMutable() {
        return valueTypeTemplate.isValueMutable();
    }

    @Override
    public IdentifierType type() {
        return mapEncoder.getType();
    }

    @Override
    public SortedMap<String, T> value(SortedMap<String, T> valueMap) {
        return isValueMutable()
            ? valueMap.entrySet().stream()
                .collect(Collectors.collectingAndThen(
                    Collectors.toMap(
                        Map.Entry::getKey,
                        (entry) -> valueTypeTemplate.value(entry.getValue()),
                        (e1, e2) -> e1,
                        TreeMap::new),
                    Collections::unmodifiableSortedMap))
            : valueMap;
    }

    @Override
    public String toDataString(SortedMap<String, T> valueMap) {
        return mapEncoder.toDataString(valueMap);
    }

    @Override
    public String toHumanString(SortedMap<String, T> valueMap) {
        return mapEncoder.toHumanString(valueMap);
    }

    @Override
    public String valueString(SortedMap<String, T> valueMap) {
        return valueMap.entrySet().stream()
            .map((entry) -> entry.getKey() + '=' + valueTypeTemplate.valueString(entry.getValue()))
            .collect(Collectors.joining(", ", "{", "}"));
    }

    @Override
    public int valueHashCode(SortedMap<String, T> valueMap) {
        return valueMap.entrySet().stream()
                .mapToInt((entry) -> entry.getKey().hashCode() ^ valueTypeTemplate.valueHashCode(entry.getValue()))
                .sum();
    }

    @Override
    public boolean valuesEqual(SortedMap<String, T> map1, SortedMap<String, T> map2) {
        if (map1.keySet().equals(map2.keySet())) {
            return map1.entrySet().stream()
                .allMatch((entry) -> valueTypeTemplate.valuesEqual(entry.getValue(), map2.get(entry.getKey())));
        }

        return false;
    }
}
