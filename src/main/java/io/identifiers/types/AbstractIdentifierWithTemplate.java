package io.identifiers.types;

import java.util.Objects;

import io.identifiers.Identifier;
import io.identifiers.IdentifierType;

abstract class AbstractIdentifierWithTemplate<T> implements Identifier<T> {

    private final TypeTemplate<T> typeTemplate;
    protected final T value;

    AbstractIdentifierWithTemplate(TypeTemplate<T> typeTemplate, T value) {
        this.typeTemplate = typeTemplate;
        // template can copy the incoming value as needed for immutability and consistency
        this.value = typeTemplate.initialValue(value);
    }

    @Override
    public IdentifierType type() {
        return typeTemplate.type();
    }


    @Override
    public T value() {
        return typeTemplate.value(value);
    }

    @Override
    public String toDataString() {
        return typeTemplate.toDataString(value);
    }

    @Override
    public String toHumanString() {
        return typeTemplate.toHumanString(value);
    }

    @Override
    public String toString() {
        return String.format("ID«%s»:%s", type(), typeTemplate.valueString(value));
    }

    @Override
    public int hashCode() {
        return Objects.hash(type(), typeTemplate.valueHashCode(value));
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Identifier) {
            Identifier<?> other = (Identifier<?>) obj;
            if (other.type().equals(type())) {
                return typeTemplate.valuesEqual(value, ((AbstractIdentifierWithTemplate<T>) other).value);
            }
        }
        return false;
    }
}
