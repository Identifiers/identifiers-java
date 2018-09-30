package io.identifiers;

/**
 * Composite Identifiers are either List or Map, but never single.
 */
public interface CompositeIdentifierFactory extends ListIdentifierFactory<Identifier<?>>, MapIdentifierFactory<Identifier<?>> {
}
