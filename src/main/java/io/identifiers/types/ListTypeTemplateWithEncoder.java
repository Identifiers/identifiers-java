package io.identifiers.types;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import io.identifiers.IdentifierType;


final class ListTypeTemplateWithEncoder<T> implements TypeTemplate<List<T>> {

    private final IdentifierEncoder<List<T>> listEncoder;
    private final TypeTemplate<T> valueTypeTemplate;


    ListTypeTemplateWithEncoder(IdentifierEncoder<List<T>> listEncoder, TypeTemplate<T> valueTypeTemplate) {
        this.listEncoder = listEncoder;
        this.valueTypeTemplate = valueTypeTemplate;
    }

    @Override
    public List<T> initialValue(List<T> values) {
        // expects values list to be copied from source list by factory
        if (isValueMutable()) {
            values.replaceAll(valueTypeTemplate::value);
        }
        return Collections.unmodifiableList(values);
    }

    @Override
    public boolean isValueMutable() {
        return valueTypeTemplate.isValueMutable();
    }

    @Override
    public IdentifierType type() {
        return listEncoder.getType();
    }

    @Override
    public List<T> value(List<T> values) {
        return isValueMutable()
            ? values.stream()
                .map(valueTypeTemplate::value)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList))
            : values;
    }

    @Override
    public String toDataString(List<T> values) {
        return listEncoder.toDataString(values);
    }

    @Override
    public String toHumanString(List<T> values) {
        return listEncoder.toHumanString(values);
    }

    @Override
    public String valueString(List<T> values) {
        return values.stream()
            .map(valueTypeTemplate::valueString)
            .collect(Collectors.joining(", ", "[", "]"));
    }

    @Override
    public int valueHashCode(List<T> values) {
        return values.stream()
            .mapToInt(valueTypeTemplate::valueHashCode)
            .reduce(type().hashCode(), (hashCode, value) -> 31 * hashCode + value);
    }

    @Override
    public boolean valuesEqual(List<T> values1, List<T> values2) {
        int size = values1.size();
        if (values2.size() == size) {
            return IntStream.range(0, size)
                .allMatch(pos -> valueTypeTemplate.valuesEqual(values1.get(pos), values2.get(pos)));
        }
        return false;
    }
}
