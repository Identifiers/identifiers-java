package io.identifiers.types;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.identifiers.IdentifierType;


class ListTypeTemplate<T> implements TypeTemplate<List<T>> {

    private final ListIdentifierEncoder<T> listEncoder;
    private final TypeTemplate<T> valueTypeTemplate;


    ListTypeTemplate(ListIdentifierEncoder<T> listEncoder, TypeTemplate<T> valueTypeTemplate) {
        this.listEncoder = listEncoder;
        this.valueTypeTemplate = valueTypeTemplate;
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
        return valueTypeTemplate.isValueMutable()
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
            for (int i = 0; i < size; i++) {
                if (!valueTypeTemplate.valuesEqual(values1.get(i), values2.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
