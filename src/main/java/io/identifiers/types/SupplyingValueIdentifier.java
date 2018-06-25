package io.identifiers.types;

import java.util.function.Supplier;

public class SupplyingValueIdentifier<T> extends ImmutableIdentifier<T> {

    private final Supplier<T> valueSupplier;


    SupplyingValueIdentifier(TypeTemplate<T> typeTemplate, Supplier<T> valueSupplier) {
        super(typeTemplate, valueSupplier.get());
        this.valueSupplier = valueSupplier;
    }

    @Override
    public T value() {
        return valueSupplier.get();
    }
}
