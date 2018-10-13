package io.identifiers.types;

import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import io.identifiers.IdentifierType;

final class MapTypeTemplateWithEncoder<T> implements TypeTemplate<Map<String, T>> {

    private final IdentifierEncoder<Map<String, T>> mapEncoder;
    private final TypeTemplate<T> valueTypeTemplate;

    MapTypeTemplateWithEncoder(IdentifierEncoder<Map<String, T>> mapEncoder, TypeTemplate<T> valueTypeTemplate) {
        this.mapEncoder = mapEncoder;
        this.valueTypeTemplate = valueTypeTemplate;
    }

     @Override
     public Map<String, T> initialValue(Map<String, T> valueMap) {
         // expects valueMap to be copied into a SortedMap from source map by factory
         if (isValueMutable()) {
             valueMap.replaceAll((key, value) -> valueTypeTemplate.value(value));
         }
         return Collections.unmodifiableSortedMap((SortedMap<String, T>) valueMap);
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
    public Map<String, T> value(Map<String, T> valueMap) {
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
    public String toDataString(Map<String, T> valueMap) {
        return mapEncoder.toDataString(valueMap);
    }

    @Override
    public String toHumanString(Map<String, T> valueMap) {
        return mapEncoder.toHumanString(valueMap);
    }

    @Override
    public String valueString(Map<String, T> valueMap) {
        return valueMap.entrySet().stream()
            .map((entry) -> entry.getKey() + '=' + valueTypeTemplate.valueString(entry.getValue()))
            .collect(Collectors.joining(", ", "{", "}"));
    }

    @Override
    public int valueHashCode(Map<String, T> valueMap) {
        return valueMap.entrySet().stream()
            .mapToInt((entry) -> entry.getKey().hashCode() ^ valueTypeTemplate.valueHashCode(entry.getValue()))
            .sum();
    }

    @Override
    public boolean valuesEqual(Map<String, T> map1, Map<String, T> map2) {
        if (map1.size() == map2.size()) {
            return map1.entrySet().stream()
                .allMatch((entry) -> valueTypeTemplate.valuesEqual(entry.getValue(), map2.get(entry.getKey())));
        }
        return false;
    }
}
