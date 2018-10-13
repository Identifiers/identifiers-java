package io.identifiers.types;

final class ImmutableIdentifier<T> extends AbstractIdentifierWithTemplate<T> {

    ImmutableIdentifier(TypeTemplate<T> typeTemplate, T value) {
        super(typeTemplate, value);
    }
}
