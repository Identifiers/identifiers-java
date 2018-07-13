package io.identifiers.types;

import io.identifiers.IdentifierType;

class TypeTemplateImpl<T> implements TypeTemplate<T> {

    private final IdentifierType type;
    private final IdentifierEncoder<T> encoder;


    TypeTemplateImpl(
            IdentifierType type,
            IdentifierEncoder<T> encoder) {

        this.type = type;
        this.encoder = encoder;
    }

    @Override
    public IdentifierType type() {
        return type;
    }

    @Override
    public T value(final T value) {
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
    public String valueString(final T value) {
        return value.toString();
    }

    @Override
    public int valueHashCode(final T value) {
        return value.hashCode();
    }

    @Override
    public boolean valuesEqual(final T value1, final T value2) {
        return value1.equals(value2);
    }
}
