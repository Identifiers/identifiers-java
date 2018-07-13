package io.identifiers.types;

import io.identifiers.Identifier;
import io.identifiers.IdentifierType;

import java.util.Objects;

public class ImmutableIdentifier<T> implements Identifier<T> {

    private final TypeTemplate<T> typeTemplate;
    final T value;


    ImmutableIdentifier(TypeTemplate<T> typeTemplate, T value) {
        this.typeTemplate = typeTemplate;
        this.value = value;
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
        return String.format("ID«%s»: %s", type().name().toLowerCase(), typeTemplate.valueString(value));
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
            Identifier other = (Identifier) obj;
            if (other.type() == type()) {
                return typeTemplate.valuesEqual(
                    value,
                    ((ImmutableIdentifier<T>) other).value);
            }
        }
        return false;
    }
}
