package io.identifiers.types;

import io.identifiers.IdentifierType;

class TypeTemplateWithEncoder<T> implements TypeTemplate<T> {

    private final IdentifierEncoder<T> encoder;

    TypeTemplateWithEncoder(IdentifierEncoder<T> encoder) {
        this.encoder = encoder;
    }

    @Override
    public IdentifierType type() {
        return encoder.getType();
    }

    @Override
    public boolean isValueMutable() {
        return false;
    }

    @Override
    public T value(T value) {
        return value;
    }

    @Override
    public String toDataString(T value) {
        return encoder.toDataString(value);
    }

    @Override
    public String toHumanString(T value) {
        return encoder.toHumanString(value);
    }

    @Override
    public String valueString(T value) {
        return value.toString();
    }

    @Override
    public int valueHashCode(T value) {
        return value.hashCode();
    }

    @Override
    public boolean valuesEqual(T value1, T value2) {
        return value1.equals(value2);
    }
}
