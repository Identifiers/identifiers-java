package io.identifiers.types;

import java.util.Collections;
import java.util.List;

import io.identifiers.IdentifierType;
import io.identifiers.ListIdentifier;
import io.identifiers.TypeCodeModifiers;

final class ImmutableListIdentifier<T> implements ListIdentifier<T> {

    private final TypeTemplate<List<T>> typeTemplate;
    private final List<T> values;


    ImmutableListIdentifier(TypeTemplate<List<T>> typeTemplate, List<T> values) {
        this.typeTemplate = typeTemplate;
        assert TypeCodeModifiers.LIST_TYPE_CODE == (typeTemplate.type().code() & TypeCodeModifiers.LIST_TYPE_CODE)
            : String.format("Not a LIST type: %s", typeTemplate);
        // Factory takes care of copying incoming values as needed
        this.values = Collections.unmodifiableList(values);
    }

    @Override
    public IdentifierType type() {
        return typeTemplate.type();
    }

    @Override
    public List<T> value() {
        return values;
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
        return String.format("ID«%s»: %s",
            type().name().toLowerCase().replace('_', '-'), // kebab-case)
            typeTemplate.valueString(values));
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
        if (obj instanceof ListIdentifier && ((ListIdentifier) obj).type() == type()) {
            List<T> otherValues = ((ListIdentifier<T>) obj).value();
            return typeTemplate.valuesEqual(values, otherValues);
        }
        return false;
    }
}
