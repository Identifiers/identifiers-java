package io.identifiers.types;

import java.util.Collections;
import java.util.List;

import io.identifiers.IdentifierType;
import io.identifiers.ListIdentifier;

final class ImmutableListIdentifier<T> implements ListIdentifier<T> {

    private final ListTypeTemplate<T> typeTemplate;
    private final List<T> values;

    ImmutableListIdentifier(ListTypeTemplate<T> typeTemplate, List<T> values) {
        this.typeTemplate = typeTemplate;
        // expects values list to be copied from source list by factory
        this.values = Collections.unmodifiableList(typeTemplate.initialValues(values));
    }

    @Override
    public IdentifierType type() {
        return typeTemplate.type();
    }

    @Override
    public List<T> value() {
        return typeTemplate.value(values);
    }

    @Override
    public String toDataString() {
        return typeTemplate.toDataString(values);
    }

    @Override
    public String toHumanString() {
        return typeTemplate.toHumanString(values);
    }

    @Override
    public String toString() {
        return String.format("ID«%s»:%s", type(), typeTemplate.valueString(values));
    }

    @Override
    public int hashCode() {
        return typeTemplate.valueHashCode(values);
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof ImmutableListIdentifier) {
            ImmutableListIdentifier<T> otherListId = (ImmutableListIdentifier<T>) obj;
            return type() == otherListId.type()
                && typeTemplate.valuesEqual(values, otherListId.values);
        }
        return false;
    }
}
