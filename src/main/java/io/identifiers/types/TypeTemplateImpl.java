package io.identifiers.types;

import io.identifiers.IdentifierType;
import java.util.function.Function;
import java.util.function.Supplier;

class TypeTemplateImpl<T> implements TypeTemplate<T> {

    private final IdentifierType type;
    private final IdentifierEncoder<T> encoder;
    private final Function<T, Supplier<T>> valueSupplierƒ;


    private static <T> Function<T, Supplier<T>> unsupportedSupplier() {
        return (value) -> {
            throw new UnsupportedOperationException(String.format("no supplier available for %s", value));
        };
    }


    TypeTemplateImpl(
            IdentifierType type,
            IdentifierEncoder<T> encoder) {

        this(type, encoder, unsupportedSupplier());
    }


    TypeTemplateImpl(
            IdentifierType type,
            IdentifierEncoder<T> encoder,
            Function<T, Supplier<T>> valueSupplierƒ) {

        this.type = type;
        this.encoder = encoder;
        this.valueSupplierƒ = valueSupplierƒ;
    }

    @Override
    public IdentifierType type() {
        return type;
    }

    @Override
    public Supplier<T> valueSupplier(T data) {
        return valueSupplierƒ.apply(data);
    }

    @Override
    public String toDataString(T value) {
        return encoder.toDataString(value);
    }

    @Override
    public String toHumanString(T value) {
        return encoder.toHumanString(value);
    }
}
